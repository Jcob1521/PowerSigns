package redsli.me.powersigns.objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Created by redslime on 27.01.2018
 * Simplified version of org.bukkit.Location that can be (de)serialized
 */
public class SerializableLocation {

    private double x;
    private double y;
    private double z;
    private String world;

    public SerializableLocation(Location loc) {
        x = loc.getX();
        y = loc.getY();
        z = loc.getZ();
        world = loc.getWorld().getName();
    }

    public Location toLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    public boolean equals(Location loc) {
        Location loc1 = toLocation();
        return loc.getX() == loc1.getX() && loc.getY() == loc1.getY() && loc.getZ() == loc1.getZ() && loc.getWorld().getName() == loc1.getWorld().getName();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public String getWorld() {
        return world;
    }
}
