package me.lorenzo0111.teleport.commands;

import io.papermc.lib.PaperLib;
import me.lorenzo0111.teleport.TeleportPlugin;
import me.lorenzo0111.teleport.configuration.WarpConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WarpCommand implements CommandExecutor, TabExecutor {
    private final TeleportPlugin plugin;

    public WarpCommand(TeleportPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("advancedteleport.teleport")) return true;

        if (args.length < 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eInvalid usage: &c/warp [player] (warp)"));
            return true;
        }

        Player player;
        String warpName;

        if (args.length == 2) {
            player = Bukkit.getPlayer(args[0]);
            warpName = args[1];
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou must be a player to execute this command"));
                return true;
            }

            player = (Player) sender;
            warpName = args[0];
        }

        if (player == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&ePlayer not found."));
            return true;
        }

        WarpConfiguration warp = plugin.getWarp(warpName);
        if (warp == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eWarp not found."));
            return true;
        }

        PaperLib.teleportAsync(player,warp.getLocation());

        if (sender == player) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&eYou have been teleported to warp %s.", warp.getDisplayName())));
            return true;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&e%s has been teleported to warp %s.", player.getName(), warp.getDisplayName())));
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>(plugin.getWarps().keySet());
    }

}
