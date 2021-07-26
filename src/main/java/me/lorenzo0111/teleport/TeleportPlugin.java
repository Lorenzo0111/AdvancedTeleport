package me.lorenzo0111.teleport;

import io.papermc.lib.PaperLib;
import me.lorenzo0111.teleport.cache.ExpiringCache;
import me.lorenzo0111.teleport.commands.RemoveWarpCommand;
import me.lorenzo0111.teleport.commands.SetWarpCommand;
import me.lorenzo0111.teleport.commands.TeleportCommand;
import me.lorenzo0111.teleport.commands.WarpCommand;
import me.lorenzo0111.teleport.configuration.PlayerConfiguration;
import me.lorenzo0111.teleport.configuration.WarpConfiguration;
import me.lorenzo0111.teleport.listener.LeaveListener;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public final class TeleportPlugin extends JavaPlugin {
    private File playersFolder;
    private File warpsFolder;

    private ExpiringCache<UUID,PlayerConfiguration> playerCache;
    private Map<String,WarpConfiguration> warps;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        PaperLib.suggestPaper(this);

        this.getLogger().info("Loading files..");
        this.playersFolder = new File(this.getDataFolder(), "players");
        if (!playersFolder.exists()) {
            playersFolder.mkdirs();
        }

        playerCache = new ExpiringCache<>(TimeUnit.MINUTES, 5);

        this.warpsFolder = new File(this.getDataFolder(), "warps");
        if (!warpsFolder.exists()) {
            warpsFolder.mkdirs();
        }

        warps = new HashMap<>();

        File[] files = warpsFolder.listFiles(File::isFile);
        if (files != null) {
            for (File file : files) {
                String name = FilenameUtils.removeExtension(file.getName());
                try {
                    this.warps.put(name.toLowerCase(), new WarpConfiguration(name, this));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        this.getCommand("removewarp").setExecutor(new RemoveWarpCommand(this));
        this.getCommand("setwarp").setExecutor(new SetWarpCommand(this));
        this.getCommand("warp").setExecutor(new WarpCommand(this));
        this.getCommand("teleport").setExecutor(new TeleportCommand(this));
        Bukkit.getPluginManager().registerEvents(new LeaveListener(this), this);
    }

    @Override
    public void onDisable() {
        warps.clear();
        playerCache.getCache().clear();
    }

    public PlayerConfiguration getPlayer(OfflinePlayer player) {
        try {
            return playerCache.getCache().getOrDefault(player.getUniqueId(), new PlayerConfiguration(player,this));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public WarpConfiguration getWarp(String name) {
        return warps.get(name.toLowerCase());
    }

    public @Nullable WarpConfiguration createWarp(String name, Location location) {
        try {
            if (warps.containsKey(name.toLowerCase())) {
                return null;
            }

            WarpConfiguration configuration = new WarpConfiguration(name,this);
            configuration.setLocation(location);
            warps.put(name.toLowerCase(), configuration);
            return configuration;
        } catch (IOException e) {
            this.getLogger().log(Level.SEVERE, "An error has occurred while creating warp", e);
        }

        return null;
    }

    public boolean removeWarp(String name) {
        if (!warps.containsKey(name.toLowerCase())) return false;

        WarpConfiguration configuration = warps.get(name.toLowerCase());
        warps.remove(name.toLowerCase());
        return configuration.getFile().delete();
    }

    public Map<String, WarpConfiguration> getWarps() {
        return warps;
    }

    public File getPlayersFolder() {
        return playersFolder;
    }

    public File getWarpsFolder() {
        return warpsFolder;
    }
}
