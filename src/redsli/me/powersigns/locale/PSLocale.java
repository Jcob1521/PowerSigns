package redsli.me.powersigns.locale;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import redsli.me.powersigns.PowerSignsPlugin;
import redsli.me.powersigns.util.UTF8YamlConfiguration;

/**
 * Created by redslime on 16.10.2017
 * Contains all messages from locales
 */
public enum PSLocale {

	LOCALE_TRANSLATOR("locale.translator", "redslime"),
	LOCALE_NAME("locale.name", "en-US"),
	FORMAT_PREFIX("format.prefix", "&9[&b&lPower&r&3Signs&9] "),
	FORMAT_SUCCESS("format.success", "&a> &7"),
	FORMAT_ERROR("format.error", "&c> &7"),
	FORMAT_INFO("format.info", "  §b* §7"),
	SIGN_DESCRIPTION("sign.description", "Description"),
	SIGN_PRICE("sign.price", "Price"),
	SIGN_DESTROY("sign.destroy", "{$format.success} PowerSign has been removed!"),
	SIGN_USE_DENIED("sign.use.denied", "{$format.error} You don't have permissions to use this PowerSign!"),
	SIGN_USE_SUCCESS_SELF("sign.use.success.self", "{$format.success} You activated the PowerSign for &e${price}"),
	SIGN_USE_SUCCESS_OWNER("sign.use.success.owner", "{$format.prefix} &e{player} &7used your PowerSign for &e${price}"),
	SIGN_USE_ERROR_NOMONEY("sign.use.error.nomoney", "{$format.error} You don't have enough money!"),
	SIGN_USE_ERROR_COOLDOWN("sign.use.error.cooldown", "{$format.error} Please wait a few seconds before using this PowerSign again!"),
	SIGN_CREATE_DENIED_SELF("sign.create.denied.self", "{$format.error} You don't have permissions to create a PowerSign"),
	SIGN_CREATE_DENIED_OTHER("sign.create.denied.other", "{$format.error} You can only create a PowerSign with your name"),
	SIGN_CREATE_ERROR_PLAYERNOTFOUND("sign.create.error.playernotfound", "{$format.error} Couldn't find a player named &b{player}"),
	SIGN_CREATE_ERROR_INVALIDNUMBER("sign.create.error.invalidnumber", "{$format.error} &b{number} &7is not a number!"),
	SIGN_CREATE_HELP("sign.create.help", "{$format.info} &fA valid PowerSign looks like this:"),
	SIGN_CREATE_SUCCESS("sign.create.success", "{$format.success} Successfully created a new PowerSign");
	
	public static final File LOCALE_FOLDER = new File(PowerSignsPlugin.instance.getDataFolder() + "/locale/");
	private String path; // path of message in locale file
	private String default_; // default value as fallback if nothing was found

    /**
     * PSLocale constructor
     * @param path The path of the message in the locale file
     * @param default_ The fallback message if nothing was found
     */
	PSLocale(String path, String default_) {
		this.path = path;
		this.default_ = default_;
	}

    /**
     * @see #get(String)
     */
	public String get() {
		return get(path);
	}

    /**
     * Returns the (translated) message from current locale file specified in config.yml
     * @param path The path where the message can be found in the locale file
     * @return The message as defined in locale file
     */
	public String get(String path) {
		String lang = PowerSignsPlugin.instance.getConfig().getString("lang"); // get enabled language in config.yml
		if(!new File(LOCALE_FOLDER, lang + ".yml").exists()) { // language specified does not exist
			Logger.getLogger("Minecraft").severe(String.format("[%s] You're using a nonexistent language file! Change in config.yml", PowerSignsPlugin.instance.getDescription().getName()));
			Logger.getLogger("Minecraft").severe(String.format("[%s] Using en-US as backup", PowerSignsPlugin.instance.getDescription().getName()));
			return process(default_, false); // process fallback value instead
		}
		YamlConfiguration langFile = new UTF8YamlConfiguration(new File(LOCALE_FOLDER, lang + ".yml")); // get yamlconfiguration (using UTF8YC here so characters such as äöü are displayed correctly)
		return process(langFile.getString(path), true); // return processed value
	}

    /**
     * Replaces all variables in given input
     * @param input The input to be processed
     * @param allowLoop Whether variables should be processed the same way, if not the fallback value is used
     * @return The processed input string
     */
	private String process(String input, boolean allowLoop) {
		input = ChatColor.translateAlternateColorCodes('&', input);
		for(String word : input.split(" ")) {
			if(word.matches("\\{\\$(.*)\\}")) { // regex, input starts with '{$' and ends with '}'
				String otherPath = word.replaceAll("\\{\\$(.*)\\}", "$1"); // get value between '{$' and '}'
				if(allowLoop)
					input = input.replace(word, get(otherPath));
				else
					input = input.replace(word, backup(otherPath));
			}
		}
		return input;
	}

    /**
     * Returns the fallback value without being processed
     * @param path The path of the message the backup is needed for
     * @return The fallback value of the message path
     */
	private String backup(String path) {
		for(PSLocale p : PSLocale.values()) {
			if(p.path.equalsIgnoreCase(path)) {
				return ChatColor.translateAlternateColorCodes('&', p.default_);
			}
		}
		return "";
	}
}