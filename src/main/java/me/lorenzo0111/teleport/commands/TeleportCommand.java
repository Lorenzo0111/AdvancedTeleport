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
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eInvalid usage: &c/teleport (player) [target]"));
            return true;
        }

        Player player;
        OfflinePlayer target;

        if (args.length == 2) {
            player = Bukkit.getPlayer(args[0]);
            target = Bukkit.getOfflinePlayer(args[1]);
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou must be a player to execute this command"));
                return true;
            }

            player = (Player) sender;
            target = Bukkit.getOfflinePlayer(args[0]);
        }

        if (player == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eThat player does not exists."));
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
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eNo last location has been recorded for that player."));
            return true;
        }

        PaperLib.teleportAsync(player,targetLocation);

        if (player != sender) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&eTeleported %s to %s (%s&e).", player.getName(), target.getName(), target.isOnline() ? "&aOnline" : "&cOffline")));
            return true;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&eTeleported to %s (%s&e).", target.getName(), target.isOnline() ? "&aOnline" : "&cOffline" )));
        return true;
    }

}
