package com.kuzari.check.impl.badpackets;

import com.kuzari.Kuzari;
import com.kuzari.check.type.PacketCheck;
import com.kuzari.data.PlayerData;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;

public class BadPacketsB extends PacketCheck {
    public BadPacketsB(Kuzari plugin, PlayerData playerData, String name, int maxvl, String type) {
        super(plugin, playerData, "BadPackets", 30, "B");
    }

    @Override
    public void run(Player player, Packet packet) {

        super.run(player, packet);
    }
}
