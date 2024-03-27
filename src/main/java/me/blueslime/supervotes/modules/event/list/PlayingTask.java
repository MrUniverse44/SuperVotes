package me.blueslime.supervotes.modules.event.list;

import me.blueslime.supervotes.SuperVotes;
import me.blueslime.supervotes.modules.actions.Actions;
import me.blueslime.supervotes.modules.actions.container.ActionContainer;
import me.blueslime.supervotes.modules.event.Events;
import me.blueslime.supervotes.modules.event.type.SuperEventTask;
import me.blueslime.supervotes.modules.sounds.EventSound;
import me.blueslime.supervotes.modules.utils.tools.Tools;
import me.blueslime.utilitiesapi.text.TextReplacer;
import me.blueslime.utilitiesapi.tools.PluginTools;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class PlayingTask extends SuperEventTask {
    private final Map<Double, List<ActionContainer>> itemMap = new ConcurrentHashMap<>();
    private final Location location;
    private final EventSound sound;

    public PlayingTask(SuperVotes plugin, Location location) {
        super(plugin, 0, plugin.getSettings().getInt("event-duration", 60));
        this.sound = new EventSound(plugin.getSettings(), "sounds.playing");
        this.location = location;
        loadItems();
    }

    @Override
    public void run() {
        runTimer();

        runItemRoulette();

        if (sound.isAllSeconds()) {
            sound.play(getOnlinePlayers());
        }

    }

    @Override
    public void onTimer(int seconds) {
        getSenders().addSender(getServer().getConsoleSender()).send(
            getMessages(),
            seconds != 1 ? "broadcasts.end.timer-in-seconds" : "broadcasts.end.timer-in-second",
            TextReplacer.builder()
                .replace("<time>", seconds)
        );

        if (!sound.isAllSeconds()) {
            sound.play(getOnlinePlayers());
        }
    }

    public void loadItems() {
        FileConfiguration configuration = getSettings();

        ConfigurationSection section = configuration.getConfigurationSection("item-chance");

        if (section == null) {
            return;
        }

        for (String chance : section.getKeys(false)) {
            if (PluginTools.isNumber(chance) || Tools.isDouble(chance)) {
                double probability = PluginTools.isNumber(chance) ? (double)Integer.parseInt(chance) : Double.parseDouble(chance);

                if (probability < 0.01 || probability > 100) {
                    getLogger().info("The plugin only supports probability from 0.01 to 100");
                }

                List<ActionContainer> containers = itemMap.computeIfAbsent(probability, l -> new ArrayList<>());

                ConfigurationSection items = configuration.getConfigurationSection("item-chance." + chance);

                if (items == null) {
                    continue;
                }

                for (String item : items.getKeys(false)) {
                    containers.add(
                        ActionContainer.build(
                            configuration,
                            "item-chance." + chance + "." + item
                        )
                    );
                }
            }
        }
    }

    public void runItemRoulette() {
        for (int current = 0; current < getPlugin().getSettings().getInt("items-per-seconds", 3); current++) {
            for (Map.Entry<Double, List<ActionContainer>> entry : itemMap.entrySet()) {
                if (execute(entry.getKey())) {
                    List<ActionContainer> list = entry.getValue();

                    ActionContainer container = list.get(ThreadLocalRandom.current().nextInt(list.size()));

                    if (container.isItem() && location.getWorld() != null) {
                        location.getWorld().dropItemNaturally(location, container.getWrapper().getItem());
                        return;
                    }
                    getModule(Actions.class).execute(container.getActionList());
                    return;
                }
            }
        }
    }

    private boolean execute(double probability) {
        double fraction = probability / 100.0;
        double chance = ThreadLocalRandom.current().nextDouble();

        return chance < fraction;
    }

    @Override
    public void complete() {
        getPlugin().getModule(Events.class).finishEvent();
    }
}
