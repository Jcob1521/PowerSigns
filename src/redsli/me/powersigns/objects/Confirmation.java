package redsli.me.powersigns.objects;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import redsli.me.powersigns.PowerSignsPlugin;
import redsli.me.powersigns.listeners.InteractListener;
import redsli.me.powersigns.locale.PSLocale;
import redsli.me.powersigns.util.ezItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by redslime on 26.01.2018
 * Prompts a player to confirm the payment before activating the PowerSign
 */
public class Confirmation implements Listener {

    public static final double VALUE = PowerSignsPlugin.instance.getConfig().getDouble("confirmation.value"); // min. value to trigger a confirmation
    public static final boolean ENABLED = PowerSignsPlugin.instance.getConfig().getBoolean("confirmation.enabled"); // whether we want confirmations at all

    private static List<Confirmation> confirmations = new ArrayList<>();

    private Player p;
    private PowerSign ps;
    private String title;

    /**
     * Used in main class to register listener
     */
    public Confirmation() {
    }

    /**
     * Create a new confirmation stage
     *
     * @param pl The plugin instance
     * @param p  The player to prompt the confirmation to
     * @param ps The PowerSign that requires a confirmation
     */
    public Confirmation(PowerSignsPlugin pl, Player p, PowerSign ps) {
        this.p = p;
        this.ps = ps;
        title = PSLocale.SIGN_USE_CONFIRM_TITLE.get();

        confirmations.add(this);
    }

    /**
     * @param ps The PowerSign to check
     * @return whether a confirmation is needed for the given PowerSign
     */
    public static boolean isRequired(PowerSign ps) {
        return ENABLED && ps.getPrice() >= VALUE;
    }

    /**
     * Creates the confirmation GUI and shows it to the player
     */
    public void fire() {
        if(ENABLED) {
            Inventory v = Bukkit.createInventory(null, 9 * 3, title);
            v.setItem(13, ezItem.getSkull("MHF_Question", PSLocale.SIGN_USE_CONFIRM_HELP.get().replace("{value}", ps.getPrice() + "").replace("{description}", ps.getDescription())));
            for(int i = 0; i < 9 * 3; i++) {
                if(i < 3 || (i >= 9 && i <= 11) || (i >= 18 && i <= 20))
                    v.setItem(i, new ezItem(Material.REDSTONE_BLOCK).withDisplayName(PSLocale.SIGN_USE_CONFIRM_NO.get()).get());
                if((i >= 6 && i <= 8) || (i >= 15 && i <= 17) || i > 23)
                    v.setItem(i, new ezItem(Material.EMERALD_BLOCK).withDisplayName(PSLocale.SIGN_USE_CONFIRM_YES.get()).get());
            }

            if(p != null) {
                p.openInventory(v);
            }
        }
    }

    /**
     * Handles click events in all confirmation GUIs
     *
     * @param e The triggered event
     */
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if(e.getSlotType() == InventoryType.SlotType.CONTAINER && e.getCurrentItem() != null) {
            for(Confirmation confirmation : confirmations) {
                if(e.getInventory().getViewers().contains(confirmation.p)) {
                    e.setCancelled(true);
                    if(e.getCurrentItem().getType() != Material.PLAYER_HEAD)
                        p.getOpenInventory().close();
                    if(e.getCurrentItem().getType() == Material.EMERALD_BLOCK) {
                        confirmation.ps.use(p);
                        InteractListener.cooldown(confirmation.ps.getSignBlock());
                    }
                }
            }
        }
    }

    /**
     * @return The player as specified in the constructor
     */
    public Player getPlayer() {
        return p;
    }

    /**
     * @return The PowerSign as specified in the constructor
     */
    public PowerSign getPowerSign() {
        return ps;
    }
}
