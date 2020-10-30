package com.kuzari.util;

import com.kuzari.Kuzari;
import net.minecraft.server.v1_8_R3.MinecraftServer;

public class ServerUtil {

    public static int getTps() {
        return (int) MinecraftServer.getServer().recentTps[0];
    }
    public static boolean isBukkitVerbose(String s) {
        return Kuzari.bukkitVersion.contains(s);
    }
}
