package xyz.redslime.powersigns.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import xyz.redslime.powersigns.PowerSignsPlugin;
import xyz.redslime.powersigns.objects.PowerSign;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by redslime on 16.10.2017
 */
public class Utils {

    /**
     * Checks if given string is a double
     * @param str The string to be checked
     * @return Whether the given string is a double/number
     */
    public static boolean isNumber(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the given double is a decimal number
     */
    public static boolean isDecimal(double n) {
        return n % 1 != 0;
    }

    /**
     * Returns whether a PowerSign may created/used in the player's world or the player can bypass it
     *
     * @param p The player
     * @return Whether a PowerSign may created/used in the player's world or the player can bypass it
     */
    public static boolean isAllowedHere(Player p) {
        World w = p.getWorld();
        List<String> exlucdedWorlds = PowerSignsPlugin.instance.getConfig().getStringList("exclude-worlds");
        if(exlucdedWorlds.isEmpty())
            return true;
        boolean allowed = true;
        for(String exludedWorld : exlucdedWorlds)
            if(w.getName().equalsIgnoreCase(exludedWorld))
                allowed = false;
        return allowed || p.hasPermission("powersigns.sign.disabled.bypass");
    }

    /**
     * Saves all PowerSigns to the data json file
     */
    public static void savePowerSigns() {
        try {
            File data = PowerSignsPlugin.dataFile;
            if(!data.exists())
                data.createNewFile();
            Gson g = new GsonBuilder().create();
            String json = g.toJson(PowerSign.powerSigns);
            BufferedWriter writer = new BufferedWriter(new FileWriter(data));
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads all PowerSigns from the data json file
     */
    public static void loadPowerSigns() {
        try {
            File data = PowerSignsPlugin.dataFile;
            if(data.exists()) {
                Gson g = new GsonBuilder().create();
                BufferedReader reader = new BufferedReader(new FileReader(data));
                PowerSign.powerSigns = g.fromJson(reader, new TypeToken<List<PowerSign>>() {
                }.getType());
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param p The player to get amount of
     * @return How many PowerSigns the given player has created
     */
    public static int getPowerSignsAmount(Player p) {
        return (int) PowerSign.powerSigns.stream().filter(ps -> ps.getOwner().toString().equals(p.getUniqueId().toString())).count();
    }

    /**
     * @param group The group name get limit of
     * @return How many PowerSigns can be created by the given group
     */
    public static int getLimitForGroup(String group) {
        for(Map.Entry<String, Object> entry : PowerSignsPlugin.instance.getConfig().getValues(true).entrySet()) {
            if(entry.getKey().startsWith("limit.")) {
                String limitKey = entry.getKey().replaceAll("limit.(.*)", "$1");
                if(!Utils.isNumber(limitKey)) {
                    if(limitKey.equalsIgnoreCase(group)) {
                        int limit = (int) entry.getValue();
                        if(limit == -1)
                            return Integer.MAX_VALUE;
                        return limit;
                    }
                }
            }
        }
        return Integer.MAX_VALUE;
    }

    /**
     * @param p The player to check
     * @return Whether the given player can create more PowerSigns
     */
    public static boolean canCreateMorePowerSigns(Player p) {
        if(p.isOp())
            return true;

        boolean groupLimit = false;
        boolean permissionLimit = false;
        for(PermissionAttachmentInfo pai : p.getEffectivePermissions()) {
            if(pai.getValue()) { // permission is active
                if(pai.getPermission().startsWith("powersigns.limit.")) {
                    String limitStr = pai.getPermission().replace("powersigns.limit.", "");
                    if(Utils.isNumber(limitStr)) {
                        // limit is a number
                        int limit = Integer.valueOf(limitStr);
                        if(getPowerSignsAmount(p) < limit)
                            permissionLimit = true;
                    } else {
                        // limit is a string
                        if(getPowerSignsAmount(p) < getLimitForGroup(limitStr))
                            groupLimit = true;
                    }
                }
            }
        }
        return groupLimit || permissionLimit;
    }
}