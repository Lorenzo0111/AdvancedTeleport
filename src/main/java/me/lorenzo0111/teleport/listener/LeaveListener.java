package me.lorenzo0111.teleport.listener;

import me.lorenzo0111.teleport.TeleportPlugin;
import me.lorenzo0111.teleport.configuration.PlayerConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {
    private final TeleportPlugin plugin;

    public LeaveListener(TeleportPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        PlayerConfiguration data = plugin.getPlayer(event.getPlayer());
        if (data != null)
            data.setLastLocation(event.getPlayer().getLocation());
    }
}
