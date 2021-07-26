package me.lorenzo0111.teleport.listener;

import me.lorenzo0111.teleport.TeleportPlugin;
import me.lorenzo0111.teleport.updater.Updater;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    private final Updater updater;

    public JoinListener(TeleportPlugin plugin) {
        this.updater = plugin.getUpdater();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPermission("advancedteleport.update")) {
            return;
        }

        updater.sendUpdateCheck(event.getPlayer());
    }

}
