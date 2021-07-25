package me.lorenzo0111.teleport.configuration;

import me.lorenzo0111.teleport.TeleportPlugin;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class PlayerConfiguration extends AbstractConfiguration {
    private final TeleportPlugin plugin;
    private final OfflinePlayer player;

    public PlayerConfiguration(@NotNull OfflinePlayer player, TeleportPlugin plugin) throws IOException {
        super(AbstractConfiguration.load(new File(plugin.getPlayersFolder(), player.getUniqueId() + ".yml")), new File(plugin.getPlayersFolder(), player.getUniqueId() + ".yml"));

        this.plugin = plugin;
        this.player = player;
    }

    public void setLastLocation(Location location) {
        this.getConfig().set("logout", location);
        this.save();
    }

    public @Nullable Location getLastLocation() {
        return (Location) this.getConfig().get("logout");
    }

}
