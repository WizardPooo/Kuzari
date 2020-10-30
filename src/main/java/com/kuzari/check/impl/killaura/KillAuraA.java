package com.kuzari.check.impl.killaura;

import com.kuzari.Kuzari;
import com.kuzari.check.type.PacketCheck;
import com.kuzari.data.PlayerData;
import com.kuzari.update.PlayerLocation;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.entity.Player;

public class KillAuraA extends PacketCheck {
    public KillAuraA(Kuzari plugin, PlayerData playerData, String name, int maxvl, String type) {
        super(plugin, playerData, "KillAura", 50, "A");
    }
    private long lastAttack;
    private double vl, vl2, vl3;
    /*
    Good check for detecting bad auras and tpauras
    TODO: Can false on extreme lag conditions
     */

    @Override
    public void run(Player player, Packet packet) {
        if(packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity) packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
            PlayerLocation lastLocation = playerData.getLastLocation();
            if(lastLocation == null) {
                return;
            }
            long now = System.currentTimeMillis();
            boolean isLagging = now - this.playerData.getLastDelayedPacket() <= 160L;
            if(!isLagging && now - lastLocation.getTimestamp() < 20) {
                if(vl2++ > 3) {
                    onViolation(player, "");
                }

            } else if(vl2 > 0) {
                vl2 = Math.max(vl2 - 1, 0);
            }
            if(!isLagging && lastLocation.getTimestamp() + 20L > now) {
                this.lastAttack = now;

            } else if(vl > 0) {
                vl -= 0.5;
            }




        } else if(packet instanceof PacketPlayInFlying) {
            long now = System.currentTimeMillis();
            boolean isLagging = now - playerData.getLastDelayedPacket() <= 160L;
            if(!isLagging && this.lastAttack + 100L > now) {
                if(++vl > 4) {
                    this.onViolation(player, "");
                }
            }
        }
        super.run(player, packet);
    }
}
