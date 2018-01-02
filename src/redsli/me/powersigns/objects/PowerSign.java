package redsli.me.powersigns.objects;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.material.Sign;

import redsli.me.powersigns.PowerSignsPlugin;
import redsli.me.powersigns.events.PowerSignUseEvent;
import redsli.me.powersigns.locale.PSLocale;
import redsli.me.powersigns.util.Utils;

/**
 * Created by redslime on 15.10.2017
 */
public class PowerSign {
	
	private UUID owner;
	private String description;
	private double price;
	private Location loc;

    /**
     * PowerSign constructor
     * @param owner The UUID of the player name given in line 2 of the sign
     * @param description The description, line 3
     * @param price The price to pay in order to use the sign, line 4
     * @param loc The location of the sign
     */
	public PowerSign(UUID owner, String description, double price, Location loc) {
		this.owner = owner;
		this.description = description;
		this.price = price;
		this.loc = loc;
	}

    /**
     * Checks if the given block is a PowerSign
     * @param block The block to check
     * @return Whether the given block is a PowerSign
     */
	public static boolean isPowerSign(Block block) {
		if(block.getType() != null) {
			if(block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) {
				org.bukkit.block.Sign sign = (org.bukkit.block.Sign) block.getState();
				if(sign.getLines() != null) {
					if(ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[SIGNAL]")) {
						if(Bukkit.getOfflinePlayer(sign.getLine(1)).hasPlayedBefore()) {
							if(Utils.isNumber(sign.getLine(3))) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

    /**
     * Gets the PowerSign for the given location
     * @param loc The location to find the PowerSign at
     * @return The PowerSign
     */
	public static PowerSign getPowerSign(Location loc) {
		org.bukkit.block.Sign sign = (org.bukkit.block.Sign) loc.getBlock().getState();
		if(sign.getLines() != null) {
			if(ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[SIGNAL]")) {
				OfflinePlayer player = Bukkit.getOfflinePlayer(sign.getLine(1));
				if(Bukkit.getOfflinePlayer(sign.getLine(1)).hasPlayedBefore()) {
					if(Utils.isNumber(sign.getLine(3))) {
						return new PowerSign(player.getUniqueId(), sign.getLine(2), Integer.valueOf(sign.getLine(3)), loc);
					}
				}
			}
		}
		return null;
	}

    /**
     * @see #getPowerSign(Block)
     * @param block The block to get the PowerSign from
     */
	public static PowerSign getPowerSign(Block block) {
		return getPowerSign(block.getLocation());
	}

    /**
     * Handles Economy actions. Calls PowerSignUseEvent. Replaces block sign is standing on/attached to with a redstone block for a split second.
     * @param player The player using the PowerSign
     */
	public void use(Player player) {
		Bukkit.getPluginManager().callEvent(new PowerSignUseEvent(player, this));
		PowerSignsPlugin.getEconomy().withdrawPlayer(player, price);
		PowerSignsPlugin.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(owner), price);
		player.sendMessage(PSLocale.SIGN_USE_SUCCESS_SELF.get().replace("{price}", price + ""));
		Player signOwner = Bukkit.getPlayer(owner);
		if(signOwner != null) { // Player owning the sign is online
			signOwner.sendMessage(PSLocale.SIGN_USE_SUCCESS_OWNER.get().replace("{player}", player.getName()).replace("{price}", price + "").replace("{desc}", description));
		}
		Material type = getSignBlock().getType();
		byte data = getSignBlock().getData();
		getSignBlock().setType(Material.REDSTONE_BLOCK);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(PowerSignsPlugin.instance, () -> getSignBlock().setType(type), 5L);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(PowerSignsPlugin.instance, () -> getSignBlock().setData(data), 5L);
	}

    /**
     * Gets the block the sign is standing on or attached to
     * @return The block the sign is standing on or attached to
     */
	public Block getSignBlock() {
		if(loc.getWorld() != null) {
			Block block = loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
			Sign sign = (Sign) block.getState().getData();
			block = block.getRelative(sign.getAttachedFace());
			return block;
		}
		return null;
	}
	
	/**
	 * @return the owner
	 */
	public UUID getOwner() {
		return owner;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

    /**
     * @return the location
     */
	public Location getLoc() {
		return loc;
	}
}