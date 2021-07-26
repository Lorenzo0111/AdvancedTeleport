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
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("usage", "").replace("%usage%", "/warp [player] (warp)")));
            return true;
        }

        Player player;
        String warpName;

        if (args.length == 2) {
            player = Bukkit.getPlayer(args[0]);
            warpName = args[1];
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("player", "")));
                return true;
            }

            player = (Player) sender;
            warpName = args[0];
        }

        if (player == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(plugin.getConfig().getString("not-found", "&c%s not found."), "Player")));
            return true;
        }

        WarpConfiguration warp = plugin.getWarp(warpName);
        if (warp == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(plugin.getConfig().getString("not-found", "&c%s not found."), "Warp")));
            return true;
        }

        PaperLib.teleportAsync(player,warp.getLocation());

        if (sender == player) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(plugin.getConfig().getString("teleport.warp", "&eYou have been teleported to warp %s."), warp.getDisplayName())));
            return true;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("teleport.warp-other","").replace("%player%", player.getName()).replace("%warp%", warp.getDisplayName())));
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>(plugin.getWarps().keySet());
    }

}
