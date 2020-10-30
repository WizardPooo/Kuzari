package com.kuzari.check.impl.killaura;

import com.kuzari.Kuzari;
import com.kuzari.check.type.PacketCheck;
import com.kuzari.data.PlayerData;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.entity.Player;

public class KillAuraB extends PacketCheck {
    public KillAuraB(Kuzari plugin, PlayerData playerData, String name, int maxvl, String type) {
        super(plugin, playerData, "KillAura", 5, "B");
    }

    @Override
    public void run(Player player, Packet packet) {
        if(packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity) packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
            if(!this.playerData.getActionManager().isSwinging()) {
                onViolation(player, "");

            }
        }
        super.run(player, packet);
    }
}
