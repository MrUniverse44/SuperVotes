package me.blueslime.supervotes.modules.placeholders;

import me.blueslime.supervotes.SuperVotes;
import me.blueslime.supervotes.modules.reload.ReloadType;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.logging.Logger;

public abstract class PluginPlaceholders extends PlaceholderExpansion {
    protected final SuperVotes plugin;

    public PluginPlaceholders(SuperVotes plugin) {
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

    public SuperVotes getMainPlugin() {
        return plugin;
    }

    public Collection<? extends Player> getOnlinePlayers() {
        return getServer().getOnlinePlayers();
    }
}
