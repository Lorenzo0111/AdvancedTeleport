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
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("player", "")));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("usage", "")
                .replace("%usage%", "/setwarp (name) [DisplayName]")));
            return true;
        }

        String name = args[0];
        String displayName = null;

        if (args.length == 2) {
            displayName = ChatColor.translateAlternateColorCodes('&', args[1]);
        }

        WarpConfiguration config = plugin.createWarp(name, ((Player) sender).getLocation());

        if (config == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("create-error", "")));
            return true;
        }

        if (displayName != null) {
            config.setDisplayName(displayName);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("warp.display-create", "&eWarp %name% (%display%&e) created successfully.")
                .replace("%name%", name)
                .replace("%display%", displayName)));
            return true;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("warp.create", "&eWarp %name% created successfully.")
                .replace("%name%", name)));
        return true;
    }
}
