package redsli.me.powersigns.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import net.md_5.bungee.api.ChatColor;
import redsli.me.powersigns.locale.PSLocale;
import redsli.me.powersigns.util.Utils;

/**
 * Created by redslime on 15.10.2017
 */
public class SignChangeListener implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onSignChangeEvent(SignChangeEvent e) {
		Player p = e.getPlayer();
		String[] lines = e.getLines();
		
		if(ChatColor.stripColor(lines[0]).equalsIgnoreCase("[SIGNAL]")) {
			if(!ChatColor.stripColor(lines[1]).equalsIgnoreCase("")) {
				if(ChatColor.stripColor(lines[1]).equalsIgnoreCase(p.getName())) {
					if(!p.hasPermission("powersigns.sign.create.self")) {
						p.sendMessage(PSLocale.SIGN_CREATE_DENIED_SELF.get());
						e.setCancelled(true);
						return;
					}
				} else if(Bukkit.getOfflinePlayer(ChatColor.stripColor(lines[1])).hasPlayedBefore()) {
					if(!p.hasPermission("powersigns.sign.create.other")) {
						e.setCancelled(true);
						if(p.hasPermission("powersigns.sign.create.self")) {
							p.sendMessage(PSLocale.SIGN_CREATE_DENIED_OTHER.get());
						} else {
							p.sendMessage(PSLocale.SIGN_CREATE_DENIED_SELF.get());
						}
						return;
					}
				} else {
					e.setCancelled(true);
					p.sendMessage(PSLocale.SIGN_CREATE_ERROR_PLAYERNOTFOUND.get().replace("{player}", lines[1]));
					return;
				}
				if(Utils.isNumber(ChatColor.stripColor(lines[3]))) {
					e.setLine(0, "§4[SIGNAL]");
					e.setLine(1, Bukkit.getOfflinePlayer(lines[1]).getName());
					p.sendMessage(PSLocale.SIGN_CREATE_SUCCESS.get());
				} else {
					e.setCancelled(true);
					if(!ChatColor.stripColor(lines[3]).equalsIgnoreCase("")) {
						p.sendMessage(PSLocale.SIGN_CREATE_ERROR_INVALIDNUMBER.get().replace("{number}", lines[3]));
					} else {
						p.sendMessage(PSLocale.SIGN_CREATE_HELP.get());
						p.sendMessage("   §e[SIGNAL]");
						p.sendMessage("   §e" + p.getName());
						p.sendMessage("   §e§o" + PSLocale.SIGN_DESCRIPTION.get());
						p.sendMessage("   §e§o" + PSLocale.SIGN_PRICE.get());
					}
				}
			} else {
				e.setLine(0, "");
				p.sendMessage(PSLocale.SIGN_CREATE_HELP.get());
				p.sendMessage("   §e[SIGNAL]");
				p.sendMessage("   §e" + p.getName());
				p.sendMessage("   §e§o" + PSLocale.SIGN_DESCRIPTION.get());
				p.sendMessage("   §e§o" + PSLocale.SIGN_PRICE.get());
			}
		}
	}
}