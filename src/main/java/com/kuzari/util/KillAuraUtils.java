package com.kuzari.util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class KillAuraUtils {


    public static boolean isValid(Entity entity) {
            if (entity != null && entity instanceof Player) {
               return ((Player) entity).isOnline();

            }
            return false;
    }

}
