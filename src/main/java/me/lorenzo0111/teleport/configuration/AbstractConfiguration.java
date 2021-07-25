package me.lorenzo0111.teleport.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class AbstractConfiguration {
    private final FileConfiguration config;
    private final File file;

    public AbstractConfiguration(FileConfiguration config, File file) {
        this.config = config;
        this.file = file;
    }

    public static FileConfiguration load(File file) throws IOException {
        if (file.exists() || file.createNewFile())
            return YamlConfiguration.loadConfiguration(file);

        return null;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public File getFile() {
        return file;
    }

    public void save() {
        try {
            this.getConfig().save(this.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
