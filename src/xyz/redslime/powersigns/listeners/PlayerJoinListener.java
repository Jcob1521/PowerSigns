package xyz.redslime.powersigns.listeners;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by redslime on 27.01.2018
 * Merely sending plugin update messages to ops
 */
public class PlayerJoinListener implements Listener {

    public static List<TextComponent> opMessages = new ArrayList<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if(p.isOp() && !opMessages.isEmpty()) {
            for(TextComponent opMessage : opMessages) {
                p.spigot().sendMessage(opMessage);
            }
            opMessages.clear();
        }
    }
}
