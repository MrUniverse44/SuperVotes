package me.blueslime.supervotes.modules;

import me.blueslime.supervotes.SuperVotes;
import me.blueslime.supervotes.modules.plugin.Plugin;
import me.blueslime.supervotes.modules.reload.ReloadType;
import me.blueslime.supervotes.modules.utils.sender.Senders;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

public abstract class SuperModule {
    private final SuperVotes plugin;

    public SuperModule(SuperVotes plugin) {
        this.plugin = plugin;
    }


    public Logger getLogger() {
        return plugin.getLogger();
    }

    public Server getServer() {
        return plugin.getServer();
    }

    public FileConfiguration getSettings() {
        return plugin.getSettings();
    }

    public FileConfiguration getMessages() {
        return plugin.getMessages();
    }

    public FileConfiguration getVotes() {
        return plugin.getVotes();
    }

    public void loadConfiguration(ReloadType reloadType) {
        plugin.build(reloadType);
    }
    public void loadConfiguration(ReloadType reloadType, boolean save) {
        if (save) {
            plugin.save(reloadType);
        }
        plugin.build(reloadType);
    }

    public void registerModules() {

    }

    public abstract void load();

    public Collection<? extends Player> getOnlinePlayers() {
        return getServer().getOnlinePlayers();
    }

    public SuperVotes getPlugin() {
        return plugin;
    }

    public void update() {

    }

    public <T extends SuperModule> T getModule(Class<T> module) {
        return plugin.getModule(module);
    }

    public Map<Class<?>, SuperModule> getModules() {
        return plugin.getModules();
    }

    public void save(ReloadType reloadType) {
        plugin.save(reloadType);
    }

    public Plugin registerModule(SuperModule... modules) {
        return plugin.registerModule(modules);
    }

    public Senders getSenders() {
        return Senders.build(getOnlinePlayers());
    }

    public void finish() {
        plugin.finish();
    }

    public void save(FileConfiguration configuration, File file) {
        plugin.save(configuration, file);
    }
}
