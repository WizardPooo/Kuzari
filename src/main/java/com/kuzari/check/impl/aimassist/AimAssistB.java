package com.kuzari.check.impl.aimassist;

import com.kuzari.Kuzari;
import com.kuzari.check.type.PacketCheck;
import com.kuzari.check.type.RotationCheck;
import com.kuzari.data.PlayerData;
import com.kuzari.update.impl.RotationUpdate;
import com.kuzari.util.MathUtil;
import lombok.val;
import org.bukkit.entity.Player;

public class AimAssistB extends RotationCheck {
    public AimAssistB(Kuzari plugin, PlayerData playerData, String name, int maxvl, String type) {
        super(plugin, playerData, "AimAssist", 20, "B");
    }
    private float suspiciousYaw;

    @Override
    public void run(Player player, RotationUpdate update) {
        if (System.currentTimeMillis() - this.playerData.getActionManager().getLastAttack() > 10000L) {
            return;
        }
        val yawChange = MathUtil.getDistanceBetweenAngles(update.getTo().getYaw(), update.getFrom().getYaw());
        if (yawChange > 1.0f && Math.round(yawChange * 10.0f) * 0.1f == yawChange && Math.round(yawChange) != yawChange && yawChange % 1.5f != 0.0f) {
            if (yawChange == this.suspiciousYaw) {
                onViolation(player, "" + suspiciousYaw);

            }
            this.suspiciousYaw = Math.round(yawChange * 10.0f) * 0.1f;
        } else {
            this.suspiciousYaw = 0.0f;
        }
        super.run(player, update);
    }
}
