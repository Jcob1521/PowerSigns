package redsli.me.powersigns.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import redsli.me.powersigns.objects.PowerSign;

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
        }
    }
}
