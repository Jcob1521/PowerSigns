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

    /**
     * Constructs a TextComponent with the given message as default value
     * @param message The message to be sent later
     */
	public ezTextComponent(String message) {
		tc = new TextComponent(message);
	}

    /**
     * Adds a message visible by hovering over the text component in chat
     * @param hMessage The message to be displayed when hovering
     * @return Instance of current ezTextComponent
     */
	public ezTextComponent withHoverMessage(String hMessage) {
		tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hMessage).create()));
		return this;
	}

    /**
     * Adds a command ran when clicking on the text component in chat. Adds '/' if needed
     * @param command The command to be run
     * @return Instance of current ezTextComponent
     */
	public ezTextComponent withCommandExecution(String command) {
		if(!command.startsWith("/"))
			command = "/" + command;
		tc.setClickEvent(new ClickEvent(Action.RUN_COMMAND, command));
		return this;
	}

    /**
     * Adds a url opened when clicking on the text component in chat
     * @param url The url to be opened
     * @return Instance of current ezTextComponent
     */
	public ezTextComponent withURL(String url) {
		tc.setClickEvent(new ClickEvent(Action.OPEN_URL, url));
		return this;
	}

    /**
     * Adds a command suggested when clicking on the text component in chat. Adds '/' if needed
     * @param command The command to be suggested
     * @return Instance of current ezTextComponent
     */
	public ezTextComponent withCommandSuggestion(String command) {
		if(!command.startsWith("/"))
			command = "/" + command;
		tc.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, command));
		return this;
	}

    /**
     * @return The built text component
     */
	public TextComponent get() {
		return tc;
	}

    /**
     * Sends the built text component to the player
     * @param p The player to send the text component to
     */
	public void send(Player p) {
		p.spigot().sendMessage(tc);
	}
}