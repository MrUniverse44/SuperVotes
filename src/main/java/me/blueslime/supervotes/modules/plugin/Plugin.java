package me.blueslime.supervotes.modules.plugin;

import me.blueslime.supervotes.SuperVotes;
import me.blueslime.supervotes.modules.SuperModule;
import me.blueslime.supervotes.modules.event.Events;
import me.blueslime.supervotes.modules.event.type.SuperEventTask;
import me.blueslime.supervotes.modules.reload.ReloadType;
import me.blueslime.supervotes.modules.utils.consumer.PluginConsumer;
import me.blueslime.supervotes.modules.utils.file.FileUtilities;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class Plugin extends JavaPlugin {
    private final Map<Class<?>, SuperModule> moduleMap = new HashMap<>();

    private FileConfiguration settings;
    private FileConfiguration messages;
    private FileConfiguration votes;

    protected void initialize(SuperVotes plugin) {
        build(ReloadType.ALL);

        registerModules();

        loadModules();
    }

    public void build(ReloadType reloadType) {
        switch (reloadType) {
            case VOTES:
                votes = loadConfiguration(getDataFolder(), "votes.yml");
                break;
            case ALL:
                messages = loadConfiguration(getDataFolder(), "messages.yml");
                settings = loadConfiguration(getDataFolder(), "settings.yml");
                votes = loadConfiguration(getDataFolder(), "votes.yml");
                break;
            case CONFIG:
                settings = loadConfiguration(getDataFolder(), "settings.yml");
                break;
            case MESSAGES:
                messages = loadConfiguration(getDataFolder(), "messages.yml");
                break;
        }
    }

    public void save(ReloadType reloadType) {
        switch (reloadType) {
            case VOTES:
                save(votes, new File(getDataFolder(), "votes.yml"));
                break;
            case ALL:
                save(messages, new File(getDataFolder(), "messages.yml"));
                save(settings, new File(getDataFolder(), "settings.yml"));
                save(votes, new File(getDataFolder(), "votes.yml"));
                break;
            case CONFIG:
                save(settings, new File(getDataFolder(), "settings.yml"));
                break;
            case MESSAGES:
                save(messages, new File(getDataFolder(), "messages.yml"));
                break;
        }
    }

    public Plugin registerModule(SuperModule... modules) {
        if (modules != null && modules.length >= 1) {
            for (SuperModule module : modules) {
                moduleMap.put(module.getClass(), module);
            }
        }
        return this;
    }

    public void finish() {
        getLogger().info("Registered " + moduleMap.size() + " module(s).");
    }

    public void save(FileConfiguration configuration, File file) {
        PluginConsumer.process(
            () -> {
                if (file.exists()) {
                    configuration.save(file);
                } else {
                    if (file.createNewFile()) {
                        configuration.save(file);
                    }
                }
            },
            (e) -> getLogger().info("Can't save configuration file: " + file.getName())
        );
    }

    public void shutdown() {
        getModule(Events.class).shutdown();
    }

    private void loadModules() {
        for (SuperModule module : moduleMap.values()) {
            module.load();
        }
    }

    public abstract void registerModules();

    public abstract void reload();

    private FileConfiguration loadConfiguration(File folder, String child) {
        File file = new File(folder, child);

        if (!file.exists()) {
            FileUtilities.saveResource(file, FileUtilities.build(child));
        }

        if (file.exists()) {
            return YamlConfiguration.loadConfiguration(file);
        } else {
            try {
                if (file.createNewFile()) {
                    return YamlConfiguration.loadConfiguration(file);
                }
            } catch (IOException ignored) { }
            return new YamlConfiguration();
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends SuperModule> T getModule(Class<T> module) {
        return (T) moduleMap.get(module);
    }

    public Map<Class<?>, SuperModule> getModules() {
        return moduleMap;
    }

    public FileConfiguration getSettings() {
        return settings;
    }

    public FileConfiguration getMessages() {
        return messages;
    }

    public FileConfiguration getVotes() {
        return votes;
    }

    public boolean isPluginEnabled(String pluginName) {
        return getServer().getPluginManager().isPluginEnabled(pluginName);
    }
}
