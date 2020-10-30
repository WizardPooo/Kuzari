package com.kuzari.check.impl.velocity;

import com.kuzari.Kuzari;
import com.kuzari.check.type.PositionCheck;
import com.kuzari.data.PlayerData;
import com.kuzari.update.impl.PositionUpdate;
import com.kuzari.util.NmsUtil;
import org.bukkit.entity.Player;

public class VelocityA extends PositionCheck {
    private long lastMotion;

    public VelocityA(Kuzari plugin, PlayerData playerData, String name, int maxvl, String type) {
        super(plugin, playerData, "Velocity", 10, "A");
    }
    @Override
    public void run(Player player, PositionUpdate update) {
        if (this.playerData.getVelocityY() > 0.0 && !this.playerData.isBelowBlock() && !this.playerData.isWasBelowBlock() &&
                !this.playerData.isInLiquid() && !this.playerData.isWasInLiquid() &&
                !this.playerData.isInWeb() && !this.playerData.isWasInWeb()) {
            this.lastMotion += 50L;

            if (this.lastMotion > 500L + NmsUtil.getNmsPlayer(player).ping * 2L) {
                this.onViolation(player, lastMotion + "");


                this.playerData.setVelocityY(0.0);
                this.lastMotion = 0L;
            }
        } else {
            this.lastMotion = 0L;
        }
    }
}
