package me.mimic.lib;

import org.bukkit.plugin.java.JavaPlugin;

public class LibMain {
    public static JavaPlugin instance = null;
    public static JavaPlugin getInstance() {
        return instance;
    }
}
