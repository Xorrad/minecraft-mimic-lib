package me.mimic.lib.configs;

import me.mimic.lib.LibMain;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public abstract class Config extends YamlConfiguration {
    protected File file;

    public Config(String fileName) {
        this.file = new File(LibMain.getInstance().getDataFolder(), fileName);
    }

    public void load() {
        try {
            if (!this.file.exists()) {
                this.file.getParentFile().mkdirs();
                this.file.createNewFile();
                this.initDefault();
                this.save();
            }

            this.load(this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            this.save(this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return this.file.getName();
    }

    public abstract void initDefault();
}
