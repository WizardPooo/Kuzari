package com.kuzari.check.impl.speed;

import com.kuzari.Kuzari;
import com.kuzari.check.type.PositionCheck;
import com.kuzari.data.PlayerData;
import com.kuzari.update.impl.PositionUpdate;
import com.kuzari.util.TimeUtils;
import com.kuzari.util.Verbose;
import com.sun.crypto.provider.PBEWithMD5AndDESCipher;
import lombok.val;
import lombok.var;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class SpeedA extends PositionCheck {

    private double lastSpeed;
    private int threshold;
    private Verbose verbose = new Verbose();

    public SpeedA(Kuzari plugin, PlayerData playerData, String name, int maxvl, String type) {
        super(plugin, playerData, "Speed", 30, "A");
    }
    /*
    Simple friction speed check for detecting most bhops and onground speeds
     */
    @Override
    public void run(Player player, PositionUpdate update) {
        if(player.getAllowFlight() || player.isInsideVehicle()) {
            return;
        }
        if(playerData.getVelocityManager().getLastVelocityTicks() > 0) {
            return;
        }
        val nmsPlayer = ((CraftPlayer) player).getHandle();
        val to = update.getTo();
        val from = update.getFrom();
        val dx = to.getX() - from.getX();
        val dy = to.getY() - to.getY();
        val dz = to.getZ() - from.getZ();
        var f5 = 0.91f;

        val onGround = nmsPlayer.onGround;

        var moveSpeed = 0.0f;
        //If someone finds a disabler using this u can remove it
        if(TimeUtils.elapsed(playerData.getTeleportManager().lastTeleportTime) < 500) {
            return;
        }
        if(onGround) {
            f5 = nmsPlayer.world.getType(new BlockPosition(MathHelper.floor(to.getX()),
                    MathHelper.floor(nmsPlayer.getBoundingBox().b) - 1,
                    MathHelper.floor(to.getZ())))
                    .getBlock()
                    .frictionFactor * 0.91F;
        }
        val f6 = 0.16277136F / (f5 * f5 * f5);
        if(onGround) {
            moveSpeed = nmsPlayer.bI() * f6;
            if(playerData.isSprinting() && moveSpeed < 0.129) {
                moveSpeed *= 1.3;
            }
            if(dy > 0.0001) {
                moveSpeed += 0.2;

            }
        } else {
            moveSpeed = nmsPlayer.aM + 0.00001f;
            if(this.playerData.isSprinting() && moveSpeed < 0.026) {
                moveSpeed += 0.006;
            }
            if(dy < -0.08 && player.getFallDistance() == 0.0) {
                moveSpeed *= Math.abs(dy) * 1.3;

            }
        }
        val previousSpeed = this.lastSpeed;
        val speed = Math.sqrt(dx * dx * dz * dz);
        val speedChange = speed - previousSpeed;
        moveSpeed += Math.sqrt(this.playerData.getVelocityManager().getMaxHorizontal());

        if(speed > 0.24 && speedChange - moveSpeed > 0.001) {
            if((threshold += 10) > 15) {
                if(verbose.flag(3, 1000)) {
                    onViolation(player, moveSpeed + "");
                }
            }
        }  else {
            threshold = Math.max(threshold - 1, 0);

        }




        super.run(player, update);
    }
}
