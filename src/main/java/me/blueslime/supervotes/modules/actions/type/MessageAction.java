package me.blueslime.supervotes.modules.actions.type;

import me.blueslime.supervotes.SuperVotes;
import me.blueslime.supervotes.modules.actions.action.Action;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class MessageAction extends Action {
    public MessageAction() {
        super("[message]", "<message>", "message:");
    }

    @Override
    public void execute(SuperVotes plugin, Player randomPlayer, String parameter) {
        boolean placeholders = plugin.isPluginEnabled("PlaceholderAPI");

        parameter = replace(parameter).replace("\\n", "\n");

        if (placeholders && randomPlayer != null) {
            parameter = PlaceholderAPI.setPlaceholders(randomPlayer, parameter);
        }

        for (Player player : plugin.getServer().getOnlinePlayers()) {

            String message = parameter;

            if (placeholders && randomPlayer == null) {
                message = PlaceholderAPI.setPlaceholders(player, message);
            }

            message = message.replace("\\n", "\n");

            player.sendMessage(message);
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
        return "message: Hello users from the server!";
    }
}
