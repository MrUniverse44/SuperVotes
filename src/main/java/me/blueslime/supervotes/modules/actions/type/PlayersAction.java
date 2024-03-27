package me.blueslime.supervotes.modules.actions.type;

import me.blueslime.supervotes.SuperVotes;
import me.blueslime.supervotes.modules.actions.action.Action;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class PlayersAction extends Action {
    public PlayersAction() {
        super("[players]", "players:", "<players>");
    }

    @Override
    public void execute(SuperVotes plugin, Player randomPlayer, String parameter) {

        boolean placeholders = plugin.isPluginEnabled("PlaceholderAPI");

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (placeholders) {
                player.performCommand(
                    PlaceholderAPI.setPlaceholders(player, replace(parameter))
                );
                continue;
            }
            player.performCommand(
                replace(parameter)
            );
        }
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
        return "players: say Maybe I can use say command";
    }
}
