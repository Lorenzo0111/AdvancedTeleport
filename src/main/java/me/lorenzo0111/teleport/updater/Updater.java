package me.lorenzo0111.teleport.updater;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class Updater {
    private boolean fetched = false;
    private boolean updateAvailable;
    private final JavaPlugin plugin;
    private final int resourceId;
    private final String url;
    private final String prefix;
    private final String api;

    /**
     * @param resourceId Spigot resource id
     * @param url Download url of the plugin. <b>Not the api url</b>
     */
    public Updater(JavaPlugin plugin, int resourceId, String url) {
        this.plugin = plugin;
        this.resourceId = resourceId;
        this.url = url;

        this.prefix = "&8[&eRocketUpdater&8]";
        this.api = "https://api.spigotmc.org/legacy/update.php?resource=";

        this.fetch();
    }

    /**
     * Fetch updates from spigot api
     */
    private CompletableFuture<Boolean> fetch() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (InputStream inputStream = new URL(api + this.resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    String version = scanner.next();

                    fetched = true;
                    this.updateAvailable = !plugin.getDescription().getVersion().equalsIgnoreCase(version);
                    future.complete(updateAvailable);
                    return;
                }
            } catch (IOException exception) {
                exception.printStackTrace();
                fetched = true;
            }

            future.complete(false);
        });

        return future;
    }

    /**
     * @param entity Entity to send the update message
     */
    public void sendUpdateCheck(CommandSender entity) {
        if (!fetched) {
            this.fetch()
                .thenAccept((available) -> this.sendUpdateCheck(entity));
            return;
        }

        if (updateAvailable) {
            String string = ChatColor.translateAlternateColorCodes('&', String.format("%s &7An update of %s is available. Download it from %s",this.prefix,plugin.getName(),url));
            entity.sendMessage(string);
        }
    }
}
