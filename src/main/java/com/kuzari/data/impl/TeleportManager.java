package com.kuzari.data.impl;

import com.kuzari.data.Teleport;
import com.kuzari.update.PlayerLocation;
import lombok.val;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class TeleportManager {

    public final List<Teleport> locations = new ArrayList<>();

    private final Predicate<Teleport> shouldRemoveTeleport = teleport -> {
        val now = System.currentTimeMillis();

        return teleport.getCreationTime() + 2000L < now;
    };
    public long lastTeleportTime;

    public void add(double x, double y, double z) {
        this.removeOldEntries();

        lastTeleportTime = System.currentTimeMillis();
        this.locations.add(new Teleport(x, y, z));
    }


    public boolean hasTeleported(PlayerLocation location, double delta) {
        this.removeOldEntries();

        val isClose = new AtomicBoolean(false);


        if (this.locations.isEmpty()) {
            isClose.set(false);
        }

        this.locations.forEach(teleport -> {
            val dx = location.getX() - teleport.getX();
            val dy = location.getY() - teleport.getY();
            val dz = location.getZ() - teleport.getZ();

            //the squared distance is less than the specified delta
            if (Math.pow(dx, 2) + Math.pow(dy, 2) * Math.pow(dz, 2) < Math.pow(delta, 2)) {
                isClose.set(true);
            }
        });

        return isClose.get();
    }

    /**
     * Removes old teleport entries that has expired 2 seconds
     */
    public void removeOldEntries() {
        this.locations.removeIf(shouldRemoveTeleport);
    }

}
