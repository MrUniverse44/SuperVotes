package me.blueslime.supervotes.modules.event.type;

import me.blueslime.supervotes.SuperVotes;
import me.blueslime.supervotes.modules.SuperModule;
import me.blueslime.supervotes.modules.plugin.Plugin;
import me.blueslime.supervotes.modules.reload.ReloadType;
import me.blueslime.supervotes.modules.utils.sender.Senders;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public abstract class SuperEventTask extends BukkitRunnable implements Listener {
    private final SuperVotes plugin;

    private int minutes;
    private int seconds;

    public SuperEventTask(SuperVotes plugin, int minutes, int seconds) {
        this.plugin = plugin;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public Logger getLogger() {
        return getPlugin().getLogger();
    }

    public Server getServer() {
        return getPlugin().getServer();
    }

    public Senders getSenders() {
        return Senders.build(getOnlinePlayers());
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

    public void loadConfiguration(ReloadType reloadType) {
        getPlugin().build(reloadType);
    }
    public void loadConfiguration(ReloadType reloadType, boolean save) {
        if (save) {
            getPlugin().save(reloadType);
        }
        getPlugin().build(reloadType);
    }

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

    public void checkStartAction() {

    }

    public SuperVotes getPlugin() {
        return plugin;
    }

    public void runTimer() {
        if (minutes != 0 && seconds != 0) {
            seconds--;
            if (seconds == 0) {
                checkTimer();
                minutes--;
                seconds = 59;
            }
        } else {
            checkTimer();
            if (minutes == 0 && seconds == 0) {
                return;
            }
            seconds--;

        }
    }

    public void checkTimer() {
        if (minutes == 0 && (seconds <= 5 && seconds != 0 || seconds == 10 || seconds == 9 || seconds == 8 || seconds == 7 || seconds == 6 ||seconds == 15 || seconds == 30 || seconds == 20)) {
            onTimer(
                seconds
            );
        }
        if (minutes == 0 && seconds == 0) {
            complete();
        }
    }


    public abstract void onTimer(int seconds);

    public abstract void complete();

    public int getSeconds() {
        return seconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public String getTimer() {
        return (minutes < 10 ? "0" + minutes + ":" : minutes + ":") + (seconds < 10 ? "0" + seconds : seconds);
    }
}
