package com.kuzari.check.impl.velocity;

import com.kuzari.Kuzari;
import com.kuzari.check.type.PositionCheck;
import com.kuzari.data.PlayerData;
import com.kuzari.update.impl.PositionUpdate;
import lombok.val;
import org.bukkit.entity.Player;

public class VelocityB extends PositionCheck {
    public VelocityB(Kuzari plugin, PlayerData playerData, String name, int maxvl, String type) {
        super(plugin, playerData, "Velocity", 12, "B");
    }
    private int threshold;
    @Override
    public void run(Player player, PositionUpdate update) {
        val dy = update.getTo().getY() - update.getFrom().getY();

        if (this.playerData.getVelocityY() > 0.0 && this.playerData.isWasOnGround() &&
                !this.playerData.isBelowBlock() && !this.playerData.isWasBelowBlock() &&
                !this.playerData.isInLiquid() && !this.playerData.isWasInLiquid() && !this.playerData.isInWeb() &&
                !this.playerData.isWasInWeb() && dy > 0.0 && dy < 0.41999998688697815 && update.getFrom().getY() % 1.0 == 0.0) {

            val quotient = dy / this.playerData.getVelocityY();

            if (quotient < 0.99 && (threshold += 30) > 35) {
                onViolation(player, "" + quotient);
            }
        }

        if (threshold > 0) {
            --threshold;
        }
    }
}
