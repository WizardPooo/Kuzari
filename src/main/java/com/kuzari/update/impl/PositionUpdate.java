package com.kuzari.update.impl;

import com.kuzari.update.MovementUpdate;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PositionUpdate extends MovementUpdate {
    public PositionUpdate(Player player, Location to, Location from) {
        super(player, to, from);
    }
}
