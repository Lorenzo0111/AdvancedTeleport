package me.lorenzo0111.teleport.commands;

import me.lorenzo0111.teleport.TeleportPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RemoveWarpCommand implements CommandExecutor, TabExecutor {
    private final TeleportPlugin plugin;

    public RemoveWarpCommand(TeleportPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eInvalid usage: &c/removewarp (name)"));
            return true;
        }

        if (plugin.removeWarp(args[0])) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eWarp removed successfully."));
            return true;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eUnable to delete that warp. Maybe it does not exist?"));
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>(plugin.getWarps().keySet());
    }

}
