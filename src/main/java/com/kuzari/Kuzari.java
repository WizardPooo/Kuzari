package com.kuzari;

import com.kuzari.data.PlayerData;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Kuzari extends JavaPlugin {
    //Add fkin stuff here
    //ConcurrentHashMaps should prevent memory leaks.
    @Getter
    private com.kuzari.util.MathUtil mathUtil;
    @Getter
    public static Kuzari instance;
    private final Map<UUID, PlayerData> playerData = new ConcurrentHashMap<>();
    @Getter
    private final Executor packetThread = Executors.newSingleThreadExecutor();
    @Getter
    private String banCmd = "ban %name% hacking";
    @Getter
    private String flagmessage = "&7[&6Kuzari&7] &6%player% &7maybe using &6%check% &7(&c%type%&7) [VL: &c%vl%&7]";

    public static String bukkitVersion;

    public void onEnable() {
        instance = this;
        bukkitVersion = org.bukkit.Bukkit.getServer().getClass().getPackage().getName().substring(23); bukkitVersion = org.bukkit.Bukkit.getServer().getClass().getPackage().getName().substring(23);
        this.getServer().getPluginManager().registerEvents(new com.kuzari.listener.PlayerListener(this), this);

        sendLog("Loading Kuzari");
        mathUtil = new com.kuzari.util.MathUtil();
        super.onEnable();
    }
    public void loadConfig() {
        //There's no point of this but okay
        getConfig().addDefault("name", "Kuzari");
        getConfig().addDefault("flagmessage", flagmessage);
        getConfig().addDefault("ban_cmd", banCmd);
    }
    public void sendLog(String log) {
        System.out.println("[Kuzari] " +log);
    }



    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        File file = new File(getDataFolder(), "config.yml");
        File file1 = new File(getDataFolder(), "checks.yml");
        if(!file1.exists()) {
            try {
                file.createNewFile();
                loadConfig();
            } catch (Exception e) {

            }
        }
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public PlayerData getData(OfflinePlayer player) {
        return this.playerData.computeIfAbsent(player.getUniqueId(), uuid -> new PlayerData(this));
    }

}

