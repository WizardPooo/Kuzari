package com.kuzari.check.impl.killaura;

import com.kuzari.Kuzari;
import com.kuzari.check.type.PacketCheck;
import com.kuzari.data.PlayerData;
import com.kuzari.util.EvictingList;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.entity.Player;

public class KillAuraD extends PacketCheck {
    private int hitTicks;
    private final EvictingList<Long> clickDelays = new EvictingList<>(20);
    private long lastClickTime;
    private double cps;
    private int vl;
    public KillAuraD(Kuzari plugin, PlayerData playerData, String name, int maxvl, String type) {
        super(plugin, playerData, "KillAura", 20, "D");
    }

    @Override
    public void run(Player player, Packet packet) {
        if (packet instanceof PacketPlayInFlying) {
            PacketPlayInFlying flying = (PacketPlayInFlying) packet;

            //If the player is rotating
            if (++hitTicks < 2 && player.isSprinting()) {
                ;
                final double acceleration = Math.abs(playerData.getMovementParser().deltaXZ - playerData.getMovementParser().lastDeltaXZ);
                if (cps < 15 && cps > 4) {
                    if (acceleration < 0.0125) {
                        vl++;
                        if (vl > 7) {
                            onViolation(player, acceleration +"");

                        }
                    } else if(vl != 0){
                        vl--;
                    }
                }


            }

        } if (packet instanceof PacketPlayInUseEntity &&
                ((PacketPlayInUseEntity) packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
            this.hitTicks = 0;

        } else if (packet instanceof PacketPlayInArmAnimation) {
            final long clickTime = System.currentTimeMillis();
            final long clickDelay = clickTime - lastClickTime;

            clickDelays.add(clickDelay);

            if (clickDelays.size() >= 20) {
                final double average = clickDelays.parallelStream().mapToDouble(value -> value).average().orElse(0.0);
                cps = 1000L / average;
            }

            lastClickTime = clickTime;
        }


            super.run(player, packet);
    }
}
