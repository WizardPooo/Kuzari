package com.kuzari.check.impl.badpackets;

import com.kuzari.Kuzari;
import com.kuzari.check.type.PacketCheck;
import com.kuzari.check.type.RotationCheck;
import com.kuzari.data.PlayerData;
import com.kuzari.update.impl.RotationUpdate;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.entity.Player;

public class BadPacketsA extends PacketCheck {
    public BadPacketsA(Kuzari plugin, PlayerData playerData, String name, int maxvl, String type) {
        super(plugin, playerData, "BadPackets", 5, "A");
    }
    //Done
    @Override
    public void run(Player player, Packet packet) {
        double pitch;
        if(packet instanceof PacketPlayInFlying && Math.abs(pitch = Math.abs(pitch = ((PacketPlayInFlying) packet).e())) > 90.0){
            onViolation(player, "");
        }
    }
}
