package me.blueslime.supervotes.modules.actions.type;

import me.blueslime.supervotes.SuperVotes;
import me.blueslime.supervotes.modules.actions.Actions;
import me.blueslime.supervotes.modules.actions.action.Action;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomPlayerAction extends Action {
    public RandomPlayerAction() {
        super("random-player:","[random-player]","<random-player>");
    }

    @Override
    public void execute(SuperVotes plugin, Player randomPlayer, String parameter) {
        String key = replace(parameter);

        if (!plugin.getSettings().contains("random-player-actions." + key)) {
            plugin.getLogger().info(key + " parameter doesn't exist in random-player-actions path in settings.yml!");
            return;
        }

        List<Player> players = new ArrayList<>(plugin.getServer().getOnlinePlayers());

        if (players.isEmpty()) {
            plugin.getLogger().info("No players are online!");
            return;
        }

        Player player = players.get(
            ThreadLocalRandom.current().nextInt(players.size())
        );

        List<String> completedList = new ArrayList<>();

        boolean placeholders = plugin.isPluginEnabled("PlaceholderAPI");

        for (String param : plugin.getSettings().getStringList("random-player-actions." + key)) {
            param = param.replace("%player%", player.getName())
                .replace("%uuid%", player.getUniqueId().toString())
                .replace("%display_name%", player.getDisplayName());

            if (placeholders) {
                completedList.add(
                    PlaceholderAPI.setPlaceholders(player, param)
                );
                continue;
            }
            completedList.add(
                param
            );
        }

        plugin.getModule(Actions.class).execute(completedList, player);
    }

    /**
     * Check if this action is only for a random player action list
     *
     * @return true if only works with a player specified, false if works with all actions modules
     */
    @Override
    public boolean isOnlyRandomPlayer() {
        return false;
    }

    @Override
    public String getExampleUsage() {
        return "random-player: example";
    }
}
