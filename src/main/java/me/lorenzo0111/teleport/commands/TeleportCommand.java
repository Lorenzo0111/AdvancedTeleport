package me.lorenzo0111.teleport.commands;

import io.papermc.lib.PaperLib;
import me.lorenzo0111.teleport.TeleportPlugin;
import me.lorenzo0111.teleport.configuration.PlayerConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeleportCommand implements CommandExecutor {
    private final TeleportPlugin plugin;

    public TeleportCommand(TeleportPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("advancedteleport.teleport")) return true;

        if (args.length <= 0) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("usage", "").replace("%usage%", "/teleport (player) [target]")));
            return true;
        }

        Player player;
        OfflinePlayer target;

        if (args.length == 2) {
            player = Bukkit.getPlayer(args[0]);
            target = Bukkit.getOfflinePlayer(args[1]);
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("player", "")));
                return true;
            }

            player = (Player) sender;
            target = Bukkit.getOfflinePlayer(args[0]);
        }

        if (player == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(plugin.getConfig().getString("not-found", "&e%s not found."), "Player")));
            return true;
        }

        Location targetLocation = null;

        if (target.isOnline()) {
            targetLocation = ((Player) target).getLocation();
        } else {
            PlayerConfiguration config = plugin.getPlayer(target);

            if (config != null) {
                targetLocation = config.getLastLocation();
            }
        }


        if (targetLocation == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("teleport.no-last", "&eNo last location has been recorded for that player.")));
            return true;
        }

        PaperLib.teleportAsync(player,targetLocation);

        if (player != sender) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("teleport.tp-other", "")
                    .replace("%player%", player.getName())
                    .replace("%target%", target.getName() == null ? "Unknown" : target.getName())
                    .replace("%status%", target.isOnline() ? plugin.getConfig().getString("status.online", "&aOnline") : plugin.getConfig().getString("status.offline","&cOffline"))
            ));
            return true;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("teleport.tp", "")
                .replace("%target%", target.getName() == null ? "Unknown" : target.getName())
                .replace("%status%", target.isOnline() ? plugin.getConfig().getString("status.online", "&aOnline") : plugin.getConfig().getString("status.offline","&cOffline"))
        ));
        return true;
    }

}
