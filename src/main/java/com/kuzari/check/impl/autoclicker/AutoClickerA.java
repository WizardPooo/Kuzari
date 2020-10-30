package com.kuzari.check.impl.autoclicker;

import com.kuzari.Kuzari;
import com.kuzari.check.type.PacketCheck;
import com.kuzari.data.PlayerData;
import lombok.val;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AutoClickerA extends PacketCheck {
    private List<Long> recentDelays = new ArrayList<>();
    public AutoClickerA(Kuzari plugin, PlayerData playerData, String name, int maxvl, String type) {
        super(plugin, playerData, "AutoClicker", 100, "A");
    }

    //TODO: Fix falses but very nice check for detecting autoclicker
    @Override
    public void run(Player player, Packet packet) {
        if (packet instanceof PacketPlayInArmAnimation) {
            val lastLocation = playerData.getLastLocation();

            if (lastLocation == null || this.playerData.getActionManager().isDigging()) {
                return;
            }


            val delay = System.currentTimeMillis() - lastLocation.getTimestamp();

            if (this.recentDelays.add(delay) && this.recentDelays.size() == 40) {
                val averageDelay = this.recentDelays.stream()
                        .mapToDouble(Long::doubleValue)
                        .average()
                        .orElse(0.0);

                if (averageDelay <= this.recentDelays.size()) {
                    onViolation(player, "");
                }

                this.recentDelays.clear();
            }

        }
    }
}
