package redsli.me.powersigns.util;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * Created by redslime on 06.07.2017
 */
public class ezTextComponent {
	
	private TextComponent tc;
	
	public ezTextComponent(String message) {
		tc = new TextComponent(message);
	}
	
	public ezTextComponent withHoverMessage(String hMessage) {
		tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hMessage).create()));
		return this;
	}
	
	public ezTextComponent withCommandExecution(String command) {
		if(!command.startsWith("/"))
			command = "/" + command;
		tc.setClickEvent(new ClickEvent(Action.RUN_COMMAND, command));
		return this;
	}
	
	public ezTextComponent withURL(String url) {
		tc.setClickEvent(new ClickEvent(Action.OPEN_URL, url));
		return this;
	}
	
	public ezTextComponent withCommandSuggestion(String command) {
		if(!command.startsWith("/"))
			command = "/" + command;
		tc.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, command));
		return this;
	}
	
	public TextComponent get() {
		return tc;
	}
	
	public void send(Player p) {
		p.spigot().sendMessage(tc);
	}
}