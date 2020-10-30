package com.kuzari.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;


@Setter
@Getter
@AllArgsConstructor
public class PlayerLocation {
    //this class is used when we store every players location for 20 ticks (1 second)

    private double x, y, z;
    private float yaw, pitch;
    private final  long timestamp = System.currentTimeMillis();
    public Vector toVector() {
        return new Vector(x, y, z);
    }
    public Location toLocation(Player p) {

        return new Location(p.getWorld(), x, y, z, yaw, pitch);

    }

}
