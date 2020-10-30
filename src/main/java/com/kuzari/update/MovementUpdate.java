package com.kuzari.update;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
@RequiredArgsConstructor
public class MovementUpdate {
    private final Player player;
    private final Location to, from;
}
