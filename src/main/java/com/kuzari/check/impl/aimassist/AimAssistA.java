package com.kuzari.check.impl.aimassist;

import com.kuzari.Kuzari;
import com.kuzari.check.type.RotationCheck;
import com.kuzari.data.PlayerData;
import com.kuzari.update.impl.RotationUpdate;
import com.kuzari.util.MathUtil;
import org.bukkit.entity.Player;

public class AimAssistA extends RotationCheck {
    public AimAssistA(Kuzari plugin, PlayerData playerData, String name, int maxvl, String type) {
        super(plugin, playerData, "AimAssist", 20, "A");
    }
    private float sus;
    @Override
    public void run(Player player, RotationUpdate update) {
        if(System.currentTimeMillis() - playerData.getActionManager().getLastAttack() > 10000L) {
            return;
        }
        float yawChange = MathUtil.getDistanceBetweenAngles(update.getTo().getYaw(), update.getFrom().getYaw());

        if(yawChange > 1.0 && Math.round(yawChange) == yawChange && yawChange % 1.5 != 0.0f) {
            if(yawChange == sus) {
                onViolation(player, sus + "");
            }
            sus = Math.round(yawChange);
        } else {
          sus = 0;
        }


        super.run(player, update);
    }
}
