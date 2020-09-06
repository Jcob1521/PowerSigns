package xyz.redslime.powersigns.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import xyz.redslime.powersigns.locale.PSLocale;
import xyz.redslime.powersigns.util.CenteredMessage;
import xyz.redslime.powersigns.util.ezTextComponent;

/**
 * Created by redslime on 16.10.2017
 */
public class PowerSignCommand implements CommandExecutor {
	
	private Plugin plugin;
	
	public PowerSignCommand(Plugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * Gets executed on /ps /powersign /powersigns
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length == 1 && args[0].equalsIgnoreCase("help")) {
				p.sendMessage(CenteredMessage.getCenteredMessage("§a§m----------------------------------"));
				p.sendMessage(CenteredMessage.getCenteredMessage("§f§lPowerSigns Help"));
				p.sendMessage("");
				p.spigot().sendMessage(new ezTextComponent(CenteredMessage.getCenteredMessage("§f> §7What is a PowerSign?")).withHoverMessage("§f§lWhat is a PowerSign?\n\n§7PowerSigns allow players to activate a redstone mechanism by paying the owner of the sign.\n§7Useful for Casinos, Mob farms, VIP areas, firework shows and much more!").get());
				p.spigot().sendMessage(new ezTextComponent(CenteredMessage.getCenteredMessage("§f> §7How do I make a PowerSign?")).withHoverMessage("§f§lHow do I make a PowerSign?\n\n§7Place a sign and put this in the 4 lines:\n §e[SIGNAL]\n §e" + p.getName() + "\n §e§o" + PSLocale.SIGN_DESCRIPTION.get() + "\n §e§o" + PSLocale.SIGN_PRICE.get()).get());
				p.spigot().sendMessage(new ezTextComponent(CenteredMessage.getCenteredMessage("§f> §7How is the redstone signal sent?")).withHoverMessage("§f§lHow is the redstone signal sent?\n\n§7The moment a player activates a PowerSign, the block the sign is attached to\n§7or the block the sign is standing on is replaced\n§7by a redstone block for a split second").get());
				p.spigot().sendMessage(new ezTextComponent(CenteredMessage.getCenteredMessage("§f> §7How do I change the language?")).withHoverMessage("§f§lHow do I change the language?\n\n§7The language can be changed in the config.yml\n§7All available languages can be found in §bplugins/PowerSignsPlugin/locale\n§7If you'd like to help translating the plugin, please head to the Github page").get());
				p.spigot().sendMessage(new ezTextComponent(CenteredMessage.getCenteredMessage("§f> §7Need more help?")).withHoverMessage("§f§lNeed more help?\n\n§7Feel free to create an issue on the Github page or contact redslime!").get());
				p.sendMessage("");
				p.sendMessage(CenteredMessage.getCenteredMessage("§a§m----------------------------------"));
				
			} else {
				p.sendMessage(CenteredMessage.getCenteredMessage("§a§m----------------------------------"));
				p.sendMessage("");
				p.sendMessage(CenteredMessage.getCenteredMessage("§6§lPowerSigns §r§ev" + plugin.getDescription().getVersion() + " §6by §credslime"));
				p.sendMessage(CenteredMessage.getCenteredMessage("§fLanguage: §7" + PSLocale.LOCALE_NAME.get() + " by " + PSLocale.LOCALE_TRANSLATOR.get()));
				p.sendMessage(CenteredMessage.getCenteredMessage("§fGithub: §7https://github.com/redslime/PowerSigns"));
				p.spigot().sendMessage(new ezTextComponent(CenteredMessage.getCenteredMessage("§b[HELP]")).withHoverMessage("§fClick to see help!").withCommandExecution("powersigns help").get());
				p.sendMessage("");
				p.sendMessage(CenteredMessage.getCenteredMessage("§a§m----------------------------------"));
			}
		} else
			sender.sendMessage("§cMust be a player to use this!");
		return false;
	}
}