package com.kuzari.check.impl.autoclicker;

import com.kuzari.Kuzari;
import com.kuzari.check.type.PacketCheck;
import com.kuzari.data.PlayerData;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.entity.Player;

public class AutoClickerC extends PacketCheck {
    public AutoClickerC(Kuzari plugin, PlayerData playerData, String name, int maxvl, String type) {
        super(plugin, playerData, "AutoClicker", 100, "C");
    }
    private int threshold;
    private int clickDelay;
    @Override
    public void run(Player player, Packet packet) {
        if (packet instanceof PacketPlayInFlying) {
            if (this.playerData.getActionManager().isDigging()) {
                return;
            }

            if (clickDelay++ < 2) {
                if (++threshold > 50) {
                    this.onViolation(player, "failed " + name);

                }
            } else {
                threshold /= 4;
            }
        } else if (packet instanceof PacketPlayInArmAnimation) {
            clickDelay = 0;
            threshold--;
        }
    }
}
