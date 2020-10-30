package com.kuzari.check.impl.velocity;

import com.kuzari.Kuzari;
import com.kuzari.check.type.PositionCheck;
import com.kuzari.data.PlayerData;
import com.kuzari.update.impl.PositionUpdate;
import com.kuzari.util.NmsUtil;
import lombok.val;
import org.bukkit.entity.Player;

public class VelocityC extends PositionCheck {
    public VelocityC(Kuzari plugin, PlayerData playerData, String name, int maxvl, String type) {
        super(plugin, playerData, "Velocity",5, "C");
    }
    private double vl;

    @Override
    public void run(Player player, PositionUpdate update) {
        val dy = update.getTo().getY() - update.getFrom().getY();
        val dxz = Math.hypot(update.getTo().getX() - update.getFrom().getX(),
                update.getTo().getZ() - update.getFrom().getZ());

        val kbxz = Math.hypot(this.playerData.getVelocityX(), this.playerData.getVelocityZ());

        val entityPlayer = NmsUtil.getNmsPlayer(player);


        if (this.playerData.getVelocityY() > 0.0 && this.playerData.isWasOnGround() &&
                !this.playerData.isBelowBlock() && !this.playerData.isWasBelowBlock() && !this.playerData.isInLiquid() &&
                !this.playerData.isWasInLiquid() && !this.playerData.isInWeb() && !this.playerData.isWasInWeb() &&
                update.getFrom().getY() % 1.0 == 0.0 && dy > 0.0 && dy < 0.41999998688697815 &&
                kbxz > 0.45 && !entityPlayer.world.c(entityPlayer.getBoundingBox().grow(1.0, 0.0, 1.0))) {

            val quotient = dxz / kbxz;

            if (quotient < 0.6) {
                if ((vl += 1.1) >= 8.0  && vl >= 15) {
                    onViolation(player, "" + quotient);
                }
            } else {
                vl = Math.max(0, vl - 0.4);
            }
        }
    }
}
