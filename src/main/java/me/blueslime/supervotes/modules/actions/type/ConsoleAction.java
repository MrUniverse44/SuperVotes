package me.blueslime.supervotes.modules.actions.type;

import me.blueslime.supervotes.SuperVotes;
import me.blueslime.supervotes.modules.actions.action.Action;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class ConsoleAction extends Action {
    public ConsoleAction() {
        super("[console]", "<console>", "console:");
    }

    @Override
    public void execute(SuperVotes plugin, Player player, String parameter) {
        if (player == null) {
            plugin.getServer().dispatchCommand(
                plugin.getServer().getConsoleSender(),
                replace(parameter)
            );
            return;
        }
        boolean placeholders = plugin.isPluginEnabled("PlaceholderAPI");

        plugin.getServer().dispatchCommand(
            plugin.getServer().getConsoleSender(),
            placeholders ?
                PlaceholderAPI.setPlaceholders(player, replace(parameter)) :
                replace(parameter)
        );
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
        return "console: say Hello world!";
    }
}
