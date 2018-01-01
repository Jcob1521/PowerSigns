package redsli.me.powersigns.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import redsli.me.powersigns.objects.PowerSign;

/**
 * Created by redslime on 15.10.2017
 */
public class PowerSignUseEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private PowerSign sign;
	
	public PowerSignUseEvent(Player player, PowerSign sign) {
		this.player = player;
		this.sign = sign;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public PowerSign getPowerSign() {
		return sign;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}