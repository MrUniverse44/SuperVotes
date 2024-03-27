package me.blueslime.supervotes.modules.commands.list;

import me.blueslime.supervotes.SuperVotes;
import me.blueslime.supervotes.modules.actions.Actions;
import me.blueslime.supervotes.modules.actions.action.Action;
import me.blueslime.supervotes.modules.commands.type.SuperCommand;
import me.blueslime.supervotes.modules.event.Events;
import me.blueslime.supervotes.modules.event.list.StartingTask;
import me.blueslime.supervotes.modules.event.type.SuperEventTask;
import me.blueslime.supervotes.modules.reload.ReloadType;
import me.blueslime.supervotes.modules.utils.location.LocationUtils;
import me.blueslime.supervotes.modules.utils.reflect.Reflect;
import me.blueslime.supervotes.modules.utils.tools.Tools;
import me.blueslime.utilitiesapi.commands.sender.Sender;
import me.blueslime.utilitiesapi.text.TextReplacer;
import me.blueslime.utilitiesapi.tools.PluginTools;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class VotePartyCommand extends SuperCommand {
    public VotePartyCommand(SuperVotes plugin, String command) {
        super(plugin, command);
    }

    @Override
    public void load() {

    }

    @Override
    public void executeCommand(Sender sender, String label, String[] arguments) {
        Player target = sender.isPlayer() ? sender.toPlayer() : null;

        if (!sender.hasPermission("supervoteparty.admin")) {
            sender.send(
                target,
                getMessages(),
                "no-permission",
                TextReplacer.builder()
                    .replace("<permission>", "supervoteparty.admin")
            );
            return;
        }
        if (arguments.length == 0) {
            sender.send(
                target,
                getMessages(),
                "no-arguments"
            );
            return;
        }

        String argument = arguments[0].toLowerCase(Locale.ENGLISH);

        if (argument.equals("add")) {
            int votes = getVotes().getInt("votes.current", 0);
            int max = getVotes().getInt("votes.max", 50);

            votes++;

            getVotes().set("votes.current", votes);

            sender.send(
                target,
                getMessages(),
                "vote-added",
                TextReplacer.builder()
                    .replace("<current>", votes)
                    .replace("<max>", max)
            );

            save(ReloadType.VOTES);

            if (votes >= max) {
                getVotes().set("votes.current", 0);
                save(ReloadType.VOTES);

                Events events = getModule(Events.class);

                if (!events.exists() && (events.getCurrentTime() == null || events.getCurrentTime().reached())) {
                    SuperEventTask task = new StartingTask(getPlugin());

                    getModule(Events.class).setCurrentEvent(task);

                    task.runTaskTimer(getPlugin(), 0L, 20L);

                    getSenders().send(
                        target,
                        getMessages(),
                        "starting-event",
                        TextReplacer.builder()
                            .replace("<current>", votes)
                            .replace("<max>", max)
                    );
                    return;
                }
                return;
            }
            return;
        }
        if (argument.equals("actions")) {
            Actions actions = getModule(Actions.class);

            sender.send("&aShowing plugin available actions:");
            sender.send("&bInternal Actions:");

            for (Action action : actions.getActions()) {
                sender.send("  &e- &6" + action.getClass().getSimpleName() + "'s prefixes: &f" + String.join(", ", action.getPrefixes()));
                sender.send("  &cExample:");
                sender.send("    &6" + action.getExampleUsage());
                if (action.isOnlyRandomPlayer()) {
                    sender.send("  &cWarn: &6Only for Random Player Action List");
                }
            }

            sender.send("&bExternal Actions:");

            for (Action action : actions.getExternalActions()) {
                sender.send("  &e- &6" + action.getClass().getSimpleName() + "'s prefixes: &f" + String.join(", ", action.getPrefixes()));
                sender.send("  &cExample:");
                sender.send("    &6" + action.getExampleUsage());
                if (action.isOnlyRandomPlayer()) {
                    sender.send("  &cWarn: &6Only for Random Player Action List");
                }
            }
            return;
        }
        if (argument.equals("now") || argument.equals("start")) {
            getVotes().set("votes.current", 0);
            save(ReloadType.VOTES);

            int max = getVotes().getInt("votes.max", 50);

            Events events = getModule(Events.class);

            if (!events.exists()) {
                sender.send("&aEvent started!");
                SuperEventTask task = new StartingTask(getPlugin());

                getModule(Events.class).setCurrentEvent(task);

                task.runTaskTimer(getPlugin(), 0L, 20L);

                getSenders().send(
                    target,
                    getMessages(),
                    "starting-event",
                    TextReplacer.builder()
                            .replace("<current>", max)
                            .replace("<max>", max)
                );
                return;
            }
            sender.send("&eEvent already started!");
            return;
        }
        if ((argument.equals("setarea") || argument.equals("setlocation")) && sender.isPlayer()) {
            Player player = sender.toPlayer();

            getSettings().set(
                "settings.drop-location",
                LocationUtils.fromLocation(player.getLocation())
            );

            save(ReloadType.CONFIG);

            sender.send("&aArea has been set in your location.");
            return;
        }
        if (argument.equals("additem") && sender.isPlayer()) {
            if (arguments.length < 2) {
                sender.send("&cTo add an item to the drop party, please use addItem (chance) for the item in your hand.");
                return;
            }

            String number = arguments[1].replace("%", "");

            if (!PluginTools.isNumber(number) && !Tools.isDouble(number)) {
                sender.send(number + " &cis not a number!");
                return;
            }

            double chance = PluginTools.isNumber(number) ? (double)Integer.parseInt(number) : Double.parseDouble(number);

            Player player = sender.toPlayer();

            ItemStack item = Reflect.itemHand(player);

            if (Reflect.isAir(item)) {
                sender.send("&cYou need a item in your hand.");
                return;
            }

            ItemMeta meta = item.getItemMeta();

            String id = String.valueOf(ThreadLocalRandom.current().nextInt());

            if (id.startsWith("-")) {
                id = id.replace("-", "item-");
            } else {
                id = "item-" + id;
            }

            String path = "item-chance." + chance + "." + id + ".";

            String material = item.getType().toString().toUpperCase(Locale.ENGLISH);

            getSettings().set(
                path + "material",
                material
            );

            getSettings().set(
                path + "type",
                "item"
            );

            getSettings().set(
                path + "amount",
                item.getAmount()
            );

            getSettings().set(
                path + "name",
                meta != null ? meta.getDisplayName() : material
            );

            getSettings().set(
                path + "lore",
                meta != null ? meta.getLore() != null ? meta.getLore() : new ArrayList<>() : new ArrayList<>()
            );

            getSettings().set(
                path + "enchantments",
                getEnchantments(meta)
            );

            getPlugin().save(ReloadType.CONFIG);

            sender.send(
                "&aItem: &b" + item.getType() + "&a, created with id: &b" + id + "&a added to drops with chance of: &f" + chance
            );
            return;
        }
        if (argument.equals("reload")) {

            long time = System.currentTimeMillis();

            getPlugin().reload();

            sender.send(
                target,
                getPlugin().getMessages(),
                "reload",
                TextReplacer.builder()
                        .replace("<ms>", String.valueOf(System.currentTimeMillis() - time))
            );
        }
    }

    @SuppressWarnings("deprecation")
    private List<String> getEnchantments(ItemMeta meta) {
        List<String> list = new ArrayList<>();

        if (meta != null) {
            meta.getEnchants().forEach((key, value) -> {
                if (key != null) {
                    list.add(key.getName() + "," + value);
                }
            });
        }

        return list;
    }
}
