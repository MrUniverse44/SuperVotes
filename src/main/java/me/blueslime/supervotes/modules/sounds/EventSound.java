package me.blueslime.supervotes.modules.sounds;

import com.cryptomorin.xseries.XSound;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Optional;

public class EventSound {
    private final boolean enabled;
    private final Sound sound;

    private final float volume;
    private final float pitch;
    private final boolean allSeconds;

    public EventSound(ConfigurationSection configuration, String path) {
        this.enabled = configuration.getBoolean(path + ".enabled", true);

        this.sound = XSound.matchXSound(
            configuration.getString(
                path + ".sound", "UI_BUTTON_CLICK"
            )
        ).orElse(
            XSound.UI_BUTTON_CLICK
        ).parseSound();

        this.volume = Float.parseFloat(configuration.get(path + ".volume", "1.0").toString());
        this.pitch = Float.parseFloat(configuration.get(path + ".pitch", "1.0").toString());
        this.allSeconds = configuration.getBoolean(path + ".all-seconds", false);
    }

    public void play(Collection<? extends Entity> collection) {
        if (isEnabled()) {
            for (Entity entity : collection) {
                if (entity instanceof Player) {
                    ((Player)entity).playSound(entity.getLocation(), sound, volume, pitch);
                }
            }
        }
    }

    public boolean isAllSeconds() {
        return allSeconds;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
