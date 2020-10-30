package com.kuzari.check.type;

import com.kuzari.Kuzari;
import com.kuzari.check.Check;
import com.kuzari.data.PlayerData;
import com.kuzari.update.impl.PositionUpdate;
import org.bukkit.entity.Player;

public class PositionCheck extends Check<PositionUpdate> {

    public PositionCheck(Kuzari plugin, PlayerData playerData, String name, int maxvl, String type) {
        super(plugin, playerData, PositionUpdate.class, name, maxvl, type);
    }


    @Override
    public void run(Player player, PositionUpdate update) {
    }
}
