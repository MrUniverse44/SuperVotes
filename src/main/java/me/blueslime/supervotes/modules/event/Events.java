package me.blueslime.supervotes.modules.event;

import me.blueslime.supervotes.SuperVotes;
import me.blueslime.supervotes.modules.SuperModule;
import me.blueslime.supervotes.modules.event.list.PlayingTask;
import me.blueslime.supervotes.modules.event.timer.Time;
import me.blueslime.supervotes.modules.event.type.SuperEventTask;
import me.blueslime.supervotes.modules.utils.consumer.PluginConsumer;
import me.blueslime.supervotes.modules.utils.location.LocationUtils;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;

public class Events extends SuperModule {

    private SuperEventTask currentEvent = null;
    private Time nextMatchMultiplier = null;
    private Time currentTime = null;

    public Events(SuperVotes plugin) {
        super(plugin);
    }

    @Override
    public void load() {
        nextMatchMultiplier = Time.builder(
            getSettings().getString("time-to-next-event.format", "HOUR"),
            getSettings().getInt("time-to-next-event.value",3)
        );
    }

    public void updateTask() {
        shutdown();

        Location location = LocationUtils.fromString(
            getSettings().getString("settings.drop-location", "NOT_SET")
        );

        if (location == null || location.getWorld() == null) {
            getSenders().addSender(getServer().getConsoleSender()).send(
                    getMessages(),
                    "broadcasts.event.cancelled"
            );
            return;
        }

        PlayingTask task = new PlayingTask(getPlugin(), location);

        setCurrentEvent(
            task
        );

        task.runTaskTimer(getPlugin(), 0L, 20L);

        getCurrentEvent().checkStartAction();
    }

    public void shutdown() {
        if (exists()) {
            PluginConsumer.process(
                () -> HandlerList.unregisterAll(currentEvent),
                (e) -> getLogger().info("Can't unregister events from task")
            );

            PluginConsumer.process(
                currentEvent::cancel,
                (e) -> getLogger().info("Can't cancel task")
            );
        }
    }

    public Time getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Time currentTime) {
        this.currentTime = currentTime;
    }

    public boolean exists() {
        return currentEvent != null;
    }

    public SuperEventTask getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(SuperEventTask currentEvent) {
        this.currentEvent = currentEvent;
    }

    public void finishEvent() {
        shutdown();

        setCurrentTime(
            Time.builder(System.currentTimeMillis(), nextMatchMultiplier)
        );
    }
}
