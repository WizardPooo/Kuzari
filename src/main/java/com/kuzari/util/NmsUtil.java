package com.kuzari.util;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NmsUtil {

    public static EntityPlayer getNmsPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }
}
