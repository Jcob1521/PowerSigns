package redsli.me.powersigns.listeners;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import redsli.me.powersigns.objects.PowerSign;
import redsli.me.powersigns.objects.SerializableLocation;

/**
 * Created by redslime on 27.01.2018
 * Merely unregisters the destroyed PowerSign
 */
public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if(PowerSign.isPowerSign(e.getBlock())) {
            PowerSign ps = PowerSign.getPowerSign(e.getBlock());
            PowerSign.powerSigns.remove(ps);
        } else {
            for(PowerSign sign : PowerSign.powerSigns) {
                if(sign.isActive()) {
                    if(new SerializableLocation(e.getBlock().getLocation()).equals(sign.getSignBlock().getLocation())) {
                        // trying to break the redstone block
                        if(e.getPlayer().getGameMode() != GameMode.CREATIVE)
                            e.setCancelled(true);
                    }
                }
            }
        }
    }
}
