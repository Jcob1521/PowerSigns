package xyz.redslime.powersigns.listeners;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.redslime.powersigns.PowerSignsPlugin;
import xyz.redslime.powersigns.locale.PSLocale;
import xyz.redslime.powersigns.objects.Confirmation;
import xyz.redslime.powersigns.objects.PowerSign;
import xyz.redslime.powersigns.util.Utils;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by redslime on 16.10.2017
 */
public class InteractListener implements Listener {

    private static HashMap<Block, Long> cooldowns = new HashMap<>();
    private PowerSignsPlugin pl;

    public InteractListener(PowerSignsPlugin pl) {
        this.pl = pl;
    }

    /**
     * Sets the sign on the cooldown specified in config.yml
     *
     * @param b The player to be cooldown'd
     */
    public static void cooldown(Block b) {
        cooldowns.put(b, System.currentTimeMillis());
    }

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(PowerSign.isPowerSign(e.getClickedBlock())) { // block clicked on is a PowerSign that is setup correctly and working
				Player p = e.getPlayer();
				if(p.hasPermission("powersigns.sign.use")) {
					PowerSign ps = PowerSign.getPowerSign(e.getClickedBlock());
                    if(ps != null) {
                        if(Utils.isAllowedHere(p)) {
                            if(PowerSignsPlugin.getEconomy().has(p, ps.getPrice())) {
                                if(!isOnCooldown(ps.getSignBlock())) {
                                    if(Confirmation.isRequired(ps)) {
                                        new Confirmation(pl, p, ps).fire();
                                    } else {
                                        ps.use(p);
                                        cooldown(ps.getSignBlock());
                                    }
                                } else
                                    p.sendMessage(PSLocale.SIGN_USE_ERROR_COOLDOWN.get());
                            } else
                                p.sendMessage(PSLocale.SIGN_USE_ERROR_NOMONEY.get());
                        } else
                            p.sendMessage(PSLocale.SIGN_USE_ERROR_WORLDDISABLED.get());
                    } else {
                        p.sendMessage(PSLocale.SIGN_ERROR_LONG.get());
                        Sign sign = (Sign) e.getClickedBlock().getState();
                        sign.setLine(1, "");
                        sign.setLine(2, PSLocale.SIGN_ERROR_SHORT.get());
                        sign.setLine(3, "");
                        sign.update();
                    }
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
            return System.currentTimeMillis() - cooldowns.get(b) < TimeUnit.SECONDS.toMillis(PowerSignsPlugin.instance.getConfig().getInt("cooldown"));
		}
		return false;
	}
}