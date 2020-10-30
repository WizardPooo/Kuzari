package com.kuzari.check.impl.aimassist;

import com.kuzari.Kuzari;
import com.kuzari.check.type.RotationCheck;
import com.kuzari.data.PlayerData;
import com.kuzari.update.impl.RotationUpdate;
import com.kuzari.util.KillAuraUtils;
import com.kuzari.util.MathUtil;
import com.kuzari.util.TimeUtils;
import com.mysql.jdbc.TimeUtil;
import org.bukkit.entity.Player;

public class AimAssistC extends RotationCheck {
    public AimAssistC(Kuzari plugin, PlayerData playerData, String name, int maxvl, String type) {
        super(plugin, playerData, "AimAssist", 20, "C");
    }

    @Override
    public void run(Player player, RotationUpdate update) {
       if(TimeUtils.elapsed(playerData.getActionManager().getLastAttack()) <= 850L && KillAuraUtils.isValid(playerData.getActionManager().getLastEntity()))
        {
            double yawChange = Math.abs(update.getTo().getYaw() - update.getFrom().getYaw());
            double pitchChange = Math.abs(update.getTo().getPitch() - update.getFrom().getPitch());

            if (yawChange > 3.0D && pitchChange == 0.0 ||
                    pitchChange > 2.0D && yawChange == 0.0D) {
                onViolation(player, "");
            }
        }

        super.run(player, update);
    }
}
