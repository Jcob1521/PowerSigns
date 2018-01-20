package redsli.me.powersigns.listeners;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import redsli.me.powersigns.PowerSignsPlugin;
import redsli.me.powersigns.locale.PSLocale;
import redsli.me.powersigns.objects.PowerSign;

/**
 * Created by redslime on 16.10.2017
 */
public class InteractListener implements Listener {
	
	private HashMap<Block, Long> cooldowns = new HashMap<>();
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(PowerSign.isPowerSign(e.getClickedBlock())) { // block clicked on is a PowerSign that is setup correctly and working
				Player p = e.getPlayer();
				if(p.hasPermission("powersigns.sign.use")) {
					PowerSign ps = PowerSign.getPowerSign(e.getClickedBlock());
					if(PowerSignsPlugin.getEconomy().has(p, ps.getPrice()))
						if(!isOnCooldown(ps.getSignBlock())) {
							ps.use(p);
							cooldown(ps.getSignBlock());
						} else
							p.sendMessage(PSLocale.SIGN_USE_ERROR_COOLDOWN.get());
					else
						p.sendMessage(PSLocale.SIGN_USE_ERROR_NOMONEY.get());
				} else
					p.sendMessage(PSLocale.SIGN_USE_DENIED.get());
			}
		}
	}

	/**
	 * Checks if the sign is on cooldown
	 * @param b The block of the sign to check
	 * @return Whether the sign is on cooldown or not
	 */
	private boolean isOnCooldown(Block b) {
		if(cooldowns.containsKey(b)) {
			if(System.currentTimeMillis() - cooldowns.get(b) < TimeUnit.SECONDS.toMillis(PowerSignsPlugin.instance.getConfig().getInt("cooldown")))
				return true;
		}
		return false;
	}


    /**
     * Sets the sign on the cooldown specified in config.yml
     * @param b The player to be cooldown'd
     */
	private void cooldown(Block b) {
		cooldowns.put(b, System.currentTimeMillis());
	}
}