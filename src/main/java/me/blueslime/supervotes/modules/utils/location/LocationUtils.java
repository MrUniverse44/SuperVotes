package me.blueslime.supervotes.modules.utils.location;

import me.blueslime.supervotes.SuperVotes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtils {

    public static String fromLocation(Location location) {
        return (location.getWorld() == null ? "world" : location.getWorld().getName())
                + ", " + location.getX()
                + ", " + location.getY()
                + ", " + location.getZ()
                + ", " + location.getYaw()
                + ", " + location.getPitch();
    }
    public static Location fromString(String text) {
        if (text.equalsIgnoreCase("NOT_SET") | text.equalsIgnoreCase("NOT-SET") || text.isEmpty()) {
            return null;
        }

        String[] loc = text.split(",", 6);

        World w = Bukkit.getWorld(loc[0]);

        if (w != null && loc.length >= 6) {

            return new Location(
                w,
                Double.parseDouble(loc[1]),
                Double.parseDouble(loc[2]),
                Double.parseDouble(loc[3]),
                Float.parseFloat(loc[4]),
                Float.parseFloat(loc[5])
            );
        }

        SuperVotes.getPlugin(SuperVotes.class).getLogger().info("Can't get world named: " + loc[0]);
        return null;
    }
}
