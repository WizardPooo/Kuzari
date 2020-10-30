package com.kuzari.update;

import com.kuzari.Kuzari;
import com.kuzari.check.type.PositionCheck;
import com.kuzari.update.impl.PositionUpdate;
import com.kuzari.util.MathUtil;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
public class MovementHandler {
    private final Kuzari plugin;


    public void handleLocationUpdate(Player player, Location to, Location from) {
        val playerData = this.plugin.getData(player);
        if (!player.getWorld().isChunkLoaded(to.getBlockX() >> 4, to.getBlockZ() >> 4)) {
            return;
        }
        if(playerData == null)
            return;
        double lastDeltaXZ = playerData.getMovementParser().deltaXZ;
        double deltaXZ = to.clone().toVector().setY(0.0).distance(from.clone().toVector().setY(0.0));
        playerData.getMovementParser().lastDeltaXZ = lastDeltaXZ;
        playerData.getMovementParser().deltaXZ = deltaXZ;

        float yawDelta = Math.abs(from.getYaw() - to.getYaw()), pitchDelta = Math.abs(from.getYaw() - to.getYaw());
        float yaw = opt(yawDelta), pitch = opt(pitchDelta);
        float smoothing = ((float) Math.cbrt((yawDelta / 0.15f) / 8f) - 0.2f) / .6f;
        float smooth = smooth(yaw, pitch * 0.05f);
        boolean smoothing2 = (Math.abs(smooth - smoothing) > 0.2 && smoothing > 1.2);
        float yawDiff = (float) round(Math.abs(clamp180(from.getYaw() - to.getYaw())), 3);

        if(!smoothing2) {
            playerData.setAimAssistvalues(playerData.getAimAssistvalues() + 1);
        } else {
            playerData.setAimAssistvalues(0);
        }
        if(playerData.getVelocityManager().getLastVelocityTicks() > 0) {
            playerData.getVelocityManager().setLastVelocityTicks(playerData.getVelocityManager().getLastVelocityTicks() - 1);
        }
        if(Kuzari.getInstance().getMathUtil().isBlockAbove(to) || Kuzari.getInstance().getMathUtil().isBlockAbove(from)) {
            playerData.blockAboveTicks = 20;


        } else {
            if(playerData.blockAboveTicks != 0) {
                playerData.blockAboveTicks--;
            }

        }

        playerData.getMovementParser().updatePositionFlags(player, to);
        playerData.getCheckList().stream().filter(PositionCheck.class::isInstance).forEach(check -> check.run(player, new PositionUpdate(player, to, from)));


    }
    private float x = 0, y = 0, z = 0;

    public float smooth(float toSmooth, float increment) {
        x += toSmooth;
        toSmooth = (x - y) * increment;
        z += (toSmooth - z) * 0.5f;

        if (toSmooth > 0f && toSmooth > z || toSmooth < 0f && toSmooth < z) {
            toSmooth = z;
        }

        y += toSmooth;
        return toSmooth;
    }

    public void reset() {
        x = 0;
        y = 0;
        z = 0;
    }

    public float clamp180(float d) {
        if ((d %= 360.0) >= 180.0) {
            d -= 360.0;
        }
        if (d < -180.0) {
            d += 360.0;
        }
        return d;
    }

    public double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static float opt(float value) {
        return ((float) Math.cbrt((value / 0.15f) / 8f) - 0.2f) / .6f;
    }


}
