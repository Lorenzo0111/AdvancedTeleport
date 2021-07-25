package me.lorenzo0111.teleport.commands;

import me.lorenzo0111.teleport.TeleportPlugin;
import me.lorenzo0111.teleport.configuration.WarpConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetWarpCommand implements CommandExecutor {
    private final TeleportPlugin plugin;

    public SetWarpCommand(TeleportPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou must be a player to execute this command"));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eInvalid usage: &c/setwarp (name) [DisplayName]"));
            return true;
        }

        String name = args[0];
        String displayName = null;

        if (args.length == 2) {
            displayName = ChatColor.translateAlternateColorCodes('&', args[1]);
        }

        WarpConfiguration config = plugin.createWarp(name, ((Player) sender).getLocation());

        if (config == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eAn error has occurred while creating a warp. Maybe it already exists?"));
            return true;
        }

        if (displayName != null) {
            config.setDisplayName(displayName);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&eWarp %s (%s&e) created successfully.", name, "&7" + displayName)));
            return true;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&eWarp %s created successfully.", name)));
        return true;
    }
}
