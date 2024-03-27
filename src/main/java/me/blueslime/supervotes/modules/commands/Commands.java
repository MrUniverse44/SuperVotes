package me.blueslime.supervotes.modules.commands;

import me.blueslime.supervotes.SuperVotes;
import me.blueslime.supervotes.modules.SuperModule;
import me.blueslime.supervotes.modules.commands.list.VotePartyCommand;
import me.blueslime.supervotes.modules.commands.type.SuperCommand;
import me.blueslime.utilitiesapi.commands.AdvancedCommand;
import me.blueslime.utilitiesapi.reflection.utils.storage.PluginStorage;

public class Commands extends SuperModule {
    private final PluginStorage<String, SuperCommand> commandList = PluginStorage.initAsHash();
    public Commands(SuperVotes plugin) {
        super(plugin);
    }

    @Override
    public void load() {
        commandList.clear();

        for (String command : getSettings().getStringList("command.aliases")) {
            if (commandList.contains(command)) {
                getLogger().info("Can't register command '" + command + "' because the command already exists.");
                continue;
            }
            commandList.set(
                command,
                new VotePartyCommand(getPlugin(), command)
            );
        }

        String main = getSettings().getString("command.main", "voteparty");

        commandList.set(
            main,
            new VotePartyCommand(getPlugin(), main)
        );

        commandList.entrySet().forEach(
            e -> e.getValue().register()
        );
    }
}
