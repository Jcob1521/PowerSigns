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
		
		if(ChatColor.stripColor(lines[0]).equalsIgnoreCase("[SIGNAL]")) { // first line equals [SIGNAL] without color codes
			if(!ChatColor.stripColor(lines[1]).equalsIgnoreCase("")) { // second line is not empty
				if(ChatColor.stripColor(lines[1]).equalsIgnoreCase(p.getName())) { // second line equals the player name creating the sign
					if(!p.hasPermission("powersigns.sign.create.self")) { // player does not have permissions to create sign for themselves
						p.sendMessage(PSLocale.SIGN_CREATE_DENIED_SELF.get());
						e.setCancelled(true);
						return;
					}
				} else if(Bukkit.getOfflinePlayer(ChatColor.stripColor(lines[1])).hasPlayedBefore()) { // second line does not equal the player name, name is known on server
					if(!p.hasPermission("powersigns.sign.create.other")) { // player does not have permissions to create sign for the other player
						e.setCancelled(true);
						if(p.hasPermission("powersigns.sign.create.self")) { // player can only create signs for themselves instead
							p.sendMessage(PSLocale.SIGN_CREATE_DENIED_OTHER.get());
						} else {
							p.sendMessage(PSLocale.SIGN_CREATE_DENIED_SELF.get());
						}
						return;
					}
				} else { // player name given in line 2 never played on server before
					e.setCancelled(true);
					p.sendMessage(PSLocale.SIGN_CREATE_ERROR_PLAYERNOTFOUND.get().replace("{player}", lines[1]));
					return;
				}
				if(Utils.isNumber(ChatColor.stripColor(lines[3]))) { // line 4 is a number -> price
					// success! updating first line with color codes and setting player name with correct capitalisation
					e.setLine(0, "§4[SIGNAL]");
					e.setLine(1, Bukkit.getOfflinePlayer(lines[1]).getName());
					p.sendMessage(PSLocale.SIGN_CREATE_SUCCESS.get());
				} else { // line 4 is not a number
					e.setCancelled(true);
					if(!ChatColor.stripColor(lines[3]).equalsIgnoreCase("")) { // line 4 is not empty
						p.sendMessage(PSLocale.SIGN_CREATE_ERROR_INVALIDNUMBER.get().replace("{number}", lines[3]));
					} else { // line 4 is empty
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