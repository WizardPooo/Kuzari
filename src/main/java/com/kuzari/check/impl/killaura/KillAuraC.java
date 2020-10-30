package com.kuzari.check.impl.killaura;

import com.kuzari.Kuzari;
import com.kuzari.check.type.PacketCheck;
import com.kuzari.data.PlayerData;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class KillAuraC extends PacketCheck {
    public KillAuraC(Kuzari plugin, PlayerData playerData, String name, int maxvl, String type) {
        super(plugin, playerData, "KillAura", 35, "C");
    }
    private int swings;
    private int attacks;

    @Override
    public void run(Player player, Packet packet) {
        if (packet instanceof PacketPlayInArmAnimation) {
            ++this.swings;
        } else if (packet instanceof PacketPlayInFlying) {
            //impossible attack actions, noswing, criticals, hit miss ratio, hit miss ratio faking
            if ((this.swings < this.attacks || (this.attacks != 0 && this.swings > this.attacks))) {
                if(player.getItemInHand().getType() != Material.FISHING_ROD) {
                    this.onViolation(player, "");

                }
            }

            this.swings = 0;
            this.attacks = 0;
        } else if (packet instanceof PacketPlayInUseEntity) {
            if (((PacketPlayInUseEntity) packet).a() != PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                return;
            }

            ++this.attacks;
        }
        super.run(player, packet);
    }
}
