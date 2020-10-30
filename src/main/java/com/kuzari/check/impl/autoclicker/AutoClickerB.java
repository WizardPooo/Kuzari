package com.kuzari.check.impl.autoclicker;

import com.kuzari.Kuzari;
import com.kuzari.check.type.PacketCheck;
import com.kuzari.data.PlayerData;
import lombok.val;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import org.bukkit.entity.Player;

public class AutoClickerB extends PacketCheck {
    public AutoClickerB(Kuzari plugin, PlayerData playerData, String name, int maxvl, String type) {
        super(plugin, playerData, "AutoClicker", 10, "B");
    }
    private boolean dug;
    private int vl;
    @Override
    public void run(Player player, Packet packet) {
        if (packet instanceof PacketPlayInBlockDig) {
            val digType = ((PacketPlayInBlockDig) packet).c();

            if (digType == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK) {
                this.dug = true;
            } else if (digType == PacketPlayInBlockDig.EnumPlayerDigType.ABORT_DESTROY_BLOCK) {
                if (this.dug) {
                    if (++vl > 9) {
                      onViolation(player, "");
                    }
                } else if (this.vl > 0) {
                    this.vl -= 3;
                }
            }
        } else if (packet instanceof PacketPlayInArmAnimation) {
            this.dug = false;
        }
    }
}
