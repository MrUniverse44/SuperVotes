package me.blueslime.supervotes.modules.event.list;

import me.blueslime.supervotes.SuperVotes;
import me.blueslime.supervotes.modules.event.Events;
import me.blueslime.supervotes.modules.event.type.SuperEventTask;
import me.blueslime.supervotes.modules.sounds.EventSound;
import me.blueslime.utilitiesapi.text.TextReplacer;

public class StartingTask extends SuperEventTask {
    private final EventSound sound;
    public StartingTask(SuperVotes plugin) {
        super(plugin, 0, 25);
        this.sound = new EventSound(plugin.getSettings(), "sounds.starting");
    }

    @Override
    public void run() {
        runTimer();

        if (sound.isAllSeconds()) {
            sound.play(getOnlinePlayers());
        }
    }


    @Override
    public void onTimer(int seconds) {
        getSenders().addSender(getServer().getConsoleSender()).send(
            getMessages(),
            seconds != 1 ? "broadcasts.start.timer-in-seconds" : "broadcasts.start.timer-in-second",
            TextReplacer.builder()
                .replace("<time>", seconds)
        );
        if (!sound.isAllSeconds()) {
            sound.play(getOnlinePlayers());
        }
    }

    @Override
    public void complete() {
        getSenders().addSender(getServer().getConsoleSender()).send(
            getMessages(),
            "broadcasts.event.started",
            TextReplacer.builder()
                    .replace("<time>", 0)
        );

        getModule(Events.class).updateTask();
    }
}
