package redsli.me.powersigns.objects;

import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import redsli.me.powersigns.PowerSignsPlugin;
import redsli.me.powersigns.events.PowerSignUseEvent;
import redsli.me.powersigns.locale.PSLocale;
import redsli.me.powersigns.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by redslime on 15.10.2017
 */
@Getter
public class PowerSign {

	public static List<PowerSign> powerSigns = new ArrayList<>();

	private UUID owner;
	private String description;
	private double price;
	private SerializableLocation loc;

	private transient boolean active; // whether the redstone block is placed

	/**
	 * PowerSign constructor
	 *
	 * @param owner       The UUID of the player name given in line 2 of the sign
	 * @param description The description, line 3
	 * @param price       The price to pay in order to use the sign, line 4
	 * @param loc         The location of the sign
	 */
	public PowerSign(UUID owner, String description, double price, Location loc) {
		this.owner = owner;
		this.description = description;
		this.price = price;
		this.loc = new SerializableLocation(loc);

		powerSigns.add(this);
	}

    /**
     * Checks if the given block is a PowerSign
     * @param block The block to check
     * @return Whether the given block is a PowerSign
     */
	public static boolean isPowerSign(Block block) {
		if(block.getType() != null) {
			if(block.getBlockData() instanceof org.bukkit.block.data.type.Sign || block.getBlockData() instanceof WallSign) {
				org.bukkit.block.Sign sign = (org.bukkit.block.Sign) block.getState();
				if(sign.getLines() != null && !sign.getLine(1).trim().isEmpty() && !sign.getLine(3).trim().isEmpty()) {
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
						for(PowerSign ps : powerSigns) {
							if(ps.owner.toString().equals(player.getUniqueId().toString()) && ps.description.equalsIgnoreCase(sign.getLine(2)) && ps.price == Double.valueOf(sign.getLine(3)) && ps.loc.equals(loc)) {
								return ps;
							}
						}
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
		active = true;
		Bukkit.getPluginManager().callEvent(new PowerSignUseEvent(player, this));
		PowerSignsPlugin.getEconomy().withdrawPlayer(player, price);
		PowerSignsPlugin.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(owner), price);
		player.sendMessage(PSLocale.SIGN_USE_SUCCESS_SELF.get().replace("{price}", price + ""));
		Player signOwner = Bukkit.getPlayer(owner);
		if(signOwner != null) { // Player owning the sign is online
			signOwner.sendMessage(PSLocale.SIGN_USE_SUCCESS_OWNER.get().replace("{player}", player.getName()).replace("{price}", price + "").replace("{desc}", description));
		}
		Material type = getSignBlock().getType();
		BlockData blockData = getSignBlock().getBlockData();
		long delay = PowerSignsPlugin.instance.getConfig().getLong("power-duration") * 20;
		getSignBlock().setType(Material.REDSTONE_BLOCK);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(PowerSignsPlugin.instance, () -> {
			getSignBlock().setType(type);
			getSignBlock().setBlockData(blockData);
			active = false;
		}, delay);
	}

    /**
     * Gets the block the sign is standing on or attached to
     * @return The block the sign is standing on or attached to
     */
	public Block getSignBlock() {
		Location loc = this.loc.toLocation();
		if(loc.getWorld() != null) {
			Block block = loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
			if(block.getBlockData() instanceof WallSign) {
				WallSign sign = (WallSign) block.getBlockData();
				return block.getRelative(sign.getFacing().getOppositeFace());
			} else {
				return block.getRelative(BlockFace.DOWN);
			}
		}
		return null;
	}
}