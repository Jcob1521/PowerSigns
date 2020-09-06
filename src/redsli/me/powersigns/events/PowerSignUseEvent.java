package redsli.me.powersigns.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import redsli.me.powersigns.objects.PowerSign;

/**
 * Created by redslime on 15.10.2017
 */
@Getter
public class PowerSignUseEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private PowerSign sign;

	/**
	 * Called when a player uses a PowerSign successfully
	 * @param player The player clicking the sign
	 * @param sign The PowerSign clicked on
	 */
	public PowerSignUseEvent(Player player, PowerSign sign) {
		this.player = player;
		this.sign = sign;
	}

    /**
     * @return The handler list
     */
	public static HandlerList getHandlerList() {
		return handlers;
	}

	/**
	 * @return The PowerSign clicked on
	 */
	@Deprecated
	public PowerSign getPowerSign() {
		return sign;
	}

    /**
     * @return The handler list
     */
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}