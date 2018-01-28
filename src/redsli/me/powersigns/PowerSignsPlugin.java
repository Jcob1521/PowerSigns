package redsli.me.powersigns;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.update.spiget.SpigetUpdate;
import org.inventivetalent.update.spiget.UpdateCallback;
import org.inventivetalent.update.spiget.comparator.VersionComparator;
import redsli.me.powersigns.commands.PowerSignCommand;
import redsli.me.powersigns.listeners.BlockBreakListener;
import redsli.me.powersigns.listeners.InteractListener;
import redsli.me.powersigns.listeners.PlayerJoinListener;
import redsli.me.powersigns.listeners.SignChangeListener;
import redsli.me.powersigns.locale.PSLocale;
import redsli.me.powersigns.objects.Confirmation;
import redsli.me.powersigns.util.Metrics;
import redsli.me.powersigns.util.UTF8YamlConfiguration;
import redsli.me.powersigns.util.Utils;
import redsli.me.powersigns.util.ezTextComponent;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

/**
 * Created by redslime on 15.10.2017
 *
 */
public class PowerSignsPlugin extends JavaPlugin {

    public static final int RESOURCE_ID = 51501; // the spigot resource id

	private static Economy economy; // the vault economy
	private static Metrics metrics; // bStats metrics
	public static PowerSignsPlugin instance; // instance of this class
    public static File dataFile;

    /**
     * Called on startup of the plugin. Initializes everything
     */
	public void onEnable() {
		instance = this;
		metrics = new Metrics(this);
        dataFile = new File(getDataFolder(), "signs.json");
        Utils.loadPowerSigns();
		setupData();
		registerListeners();
		registerCommands();
		checkUpdate();

		// check if vault can find a economy system
		if(!setupEconomy()) {
            // economy system not found, shutting down
            Logger.getLogger("Minecraft").severe(String.format("[%s] Disabled due to no Economy system plugin found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
	}

    /**
     * Called on shutdown of the plugin. Saves PowerSigns to file
     */
    public void onDisable() {
        Utils.savePowerSigns();
    }

    /**
     * Prints an info message to the console
     * @param message The message to be printed
     */
	private void logInfo(String message) {
        Logger.getLogger("Minecraft").info(String.format("[%s] " + message, getDescription().getName()));
    }

    /**
     * Registers all listeners
     */
	private void registerListeners() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new SignChangeListener(), this);
        pm.registerEvents(new InteractListener(this), this);
        pm.registerEvents(new Confirmation(), this);
        pm.registerEvents(new PlayerJoinListener(), this);
        pm.registerEvents(new BlockBreakListener(), this);
	}

    /**
     * Registers all commands
     */
	private void registerCommands() {
		getCommand("powersigns").setExecutor(new PowerSignCommand(this));
	}

    /**
     * Checks if there's an economy system in place
     * @return Whether an economy system was found by vault
     */
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    /**
     * Loops through all locales inside the jar file and returns their names
     * @return A list of all locale names inside the jar
     */
    private List<String> getJarLocales() {
		// locales
		List<String> locales = new ArrayList<>();

		// load all available lang files
        // the following code is from
		// https://github.com/tastybento/bskyblock/blob/master/src/main/java/us/tastybento/bskyblock/util/FileLister.java
		File jarfile = null;

		try {
            /**
             * Get the jar file from the plugin.
             */
            try {
                Method method = JavaPlugin.class.getDeclaredMethod("getFile");
                method.setAccessible(true);

                jarfile = (File) method.invoke(this);
            } catch (Exception e) {
                throw new IOException(e);
            }

            JarFile jar = new JarFile(jarfile);

            /**
             * Loop through all the entries.
             */
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String path = entry.getName();

                /**
                 * Not in the folder.
                 */
                if(!path.startsWith("locale")) {
                    continue;
                }

                //plugin.getLogger().info("DEBUG: jar filename = " + entry.getName());
                if(entry.getName().endsWith(".yml")) {
                    String name = entry.getName().replace(".yml", "").replace("locale/", "");
                    if(name.length() == 5 && name.substring(2, 3).equals("-")) {
                        locales.add(name);
                    }
                }

            }
			jar.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return locales;
    }

    /**
     * Generates config.yml and locales if not existing
     */
    private void setupData() {
        // config
        System.setProperty("file.encoding", "UTF-8");
        File configFile = new File(getDataFolder().getPath(), "config.yml");

        // check config version and replace with newer version if needed
        if(configFile.exists()) {
            UTF8YamlConfiguration config = new UTF8YamlConfiguration(configFile);
            if(config.get("version") == null)
                performUpgrade(configFile);
            else {
                if(!config.getString("version").equalsIgnoreCase(getDescription().getVersion())) {
                    performUpgrade(configFile);
                }
            }
        } else
            saveDefaultConfig();

		if(getResource("locale/") != null) {
			if(!PSLocale.LOCALE_FOLDER.exists()) {
				PSLocale.LOCALE_FOLDER.mkdir();
                for(String locale : getJarLocales()) {
					saveResource("locale/" + locale + ".yml", true);
				}
			}
		}
	}

    /**
     * Deletes the old config file and replaces it with the included one; Updates locales
     * Config: Transfers old values to the new file
     *
     * @param configFile The configFile to replace
     */
    private void performUpgrade(File configFile) {
        // replace config
        UTF8YamlConfiguration config = new UTF8YamlConfiguration(configFile);
        logInfo("File config.yml is outdated. Updating...");
        Map<String, Object> oldValues = config.getValues(true);
        saveResource("config.yml", true);
        config = new UTF8YamlConfiguration(configFile);
        for(Map.Entry<String, Object> entry : oldValues.entrySet()) {
            if(!entry.getKey().equalsIgnoreCase("version"))
                config.set(entry.getKey(), entry.getValue());
        }
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logInfo("Updated config.yml");

        // replace locales
        for(File locale : Objects.requireNonNull(new File(getDataFolder() + "/locale/").listFiles())) {
            if(locale.isFile()) {
                if(locale.getName().endsWith(".yml")) {
                    String localeName = locale.getName().replaceAll("(.*).yml", "$1");
                    for(String jarLocale : getJarLocales()) {
                        if(localeName.equalsIgnoreCase(jarLocale)) {
                            locale.delete();
                            saveResource("locale/" + localeName + ".yml", true);
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks for updates
     * Following code is from https://github.com/InventivetalentDev/SpigetUpdater/blob/master/README.MD
     */
	private void checkUpdate() {
	    if(RESOURCE_ID > 0) {
            SpigetUpdate updater = new SpigetUpdate(this, RESOURCE_ID);

            // This compares versions just by checking if they are equal
            // This means that older versions will also be treated as updates
            updater.setVersionComparator(VersionComparator.EQUAL);

            logInfo("Checking for updates...");
            updater.checkForUpdate(new UpdateCallback() {
                @Override
                public void updateAvailable(String newVersion, String downloadUrl, boolean hasDirectDownload) {
                    //// A new version is available
                    // newVersion - the latest version
                    // downloadUrl - URL to the download
                    // hasDirectDownload - whether the update is available for a direct download on spiget.org
                    logInfo("New version (" + newVersion + ") is available! " + downloadUrl);
                    PlayerJoinListener.opMessages.add(new ezTextComponent(PSLocale.PLUGIN_UPDATE.get().replace("{version}", newVersion)).withURL(downloadUrl).get());
                }

                @Override
                public void upToDate() {
                    logInfo("No updates found.");
                }
            });
        }
    }
	
	/**
	 * @return the economy
	 */
	public static Economy getEconomy() {
		return economy;
	}
	
	/**
	 * @return the metrics
	 */
	public static Metrics getMetrics() {
		return metrics;
	}
}