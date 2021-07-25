package me.lorenzo0111.teleport.configuration;

import me.lorenzo0111.teleport.TeleportPlugin;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;

public class WarpConfiguration extends AbstractConfiguration {

    public WarpConfiguration(String name, TeleportPlugin plugin) throws IOException {
        super(AbstractConfiguration.load(new File(plugin.getWarpsFolder(), name + ".yml")), new File(plugin.getWarpsFolder(), name + ".yml"));
    }

    public Location getLocation() {
        return (Location) this.getConfig().get("location");
    }

    public void setLocation(Location location) {
        this.getConfig().set("location" , location);
        this.save();
    }

    public String getDisplayName() {
        return this.getConfig().getString("displayName", FilenameUtils.removeExtension(getFile().getName()));
    }

    public void setDisplayName(String name) {
        this.getConfig().set("displayName" , name);
        this.save();
    }
}
