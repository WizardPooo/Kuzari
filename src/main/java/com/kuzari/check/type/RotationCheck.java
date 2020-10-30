package com.kuzari.check.type;

import com.kuzari.Kuzari;
import com.kuzari.check.Check;
import com.kuzari.data.PlayerData;
import com.kuzari.update.impl.RotationUpdate;
import org.bukkit.Rotation;
import org.bukkit.entity.Player;

public class RotationCheck extends Check<RotationUpdate> {


    public RotationCheck(Kuzari plugin, PlayerData playerData,  String name, int maxvl, String type) {
        super(plugin, playerData, RotationUpdate.class, name, maxvl, type);
    }

    @Override
    public void run(Player player, RotationUpdate update) {

    }
}
