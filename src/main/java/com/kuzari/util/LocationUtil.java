package com.kuzari.util;

import org.bukkit.util.Vector;

public class LocationUtil {

    public static boolean isUneven(double alpha) {
        return alpha % 1.0 == 0.0;
    }

    public static double offset(Vector from, Vector to) {
        from.setY(0);
        to.setY(0);

        return to.subtract(from).length();
    }
}
