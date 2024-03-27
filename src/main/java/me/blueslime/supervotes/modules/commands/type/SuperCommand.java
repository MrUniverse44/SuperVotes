package me.blueslime.supervotes.modules.commands.type;

import me.blueslime.supervotes.SuperVotes;
import me.blueslime.supervotes.modules.SuperModule;
import me.blueslime.supervotes.modules.plugin.Plugin;
import me.blueslime.supervotes.modules.reload.ReloadType;
import me.blueslime.supervotes.modules.utils.sender.Senders;
import me.blueslime.utilitiesapi.commands.AdvancedCommand;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

public abstract class SuperCommand extends AdvancedCommand<SuperVotes> {
    public SuperCommand(SuperVotes plugin, String command) {
        super(plugin, command);
    }

    public Logger getLogger() {
        return getPlugin().getLogger();
    }

    public Server getServer() {
        return getPlugin().getServer();
    }

    public FileConfiguration getSettings() {
        return getPlugin().getSettings();
    }

    public FileConfiguration getMessages() {
        return getPlugin().getMessages();
    }

    public FileConfiguration getVotes() {
        return getPlugin().getVotes();
    }

    public Senders getSenders() {
        return Senders.build(getOnlinePlayers());
    }

    public void loadConfiguration(ReloadType reloadType) {
        getPlugin().build(reloadType);
    }
    public void loadConfiguration(ReloadType reloadType, boolean save) {
        if (save) {
            getPlugin().save(reloadType);
        }
        getPlugin().build(reloadType);
    }

    public abstract void load();

    public Collection<? extends Player> getOnlinePlayers() {
        return getServer().getOnlinePlayers();
    }

    public void save(ReloadType reloadType) {
        getPlugin().save(reloadType);
    }

    public Plugin registerModule(SuperModule... modules) {
        return getPlugin().registerModule(modules);
    }

    public void finish() {
        getPlugin().finish();
    }

    public void save(FileConfiguration configuration, File file) {
        getPlugin().save(configuration, file);
    }

    public <T extends SuperModule> T getModule(Class<T> module) {
        return getPlugin().getModule(module);
    }

    public Map<Class<?>, SuperModule> getModules() {
        return getPlugin().getModules();
    }
}
