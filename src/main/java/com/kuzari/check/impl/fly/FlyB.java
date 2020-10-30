package com.kuzari.check.impl.fly;

import com.kuzari.Kuzari;
import com.kuzari.check.type.PositionCheck;
import com.kuzari.data.PlayerData;
import com.kuzari.update.impl.PositionUpdate;
import com.kuzari.util.BlockUtils;
import com.kuzari.util.Verbose;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FlyB extends PositionCheck {
    public FlyB(Kuzari plugin, PlayerData playerData, String name, int maxvl, String type) {
        super(plugin, playerData, "Fly", 30, "B");
    }
    private double jumpLimit;
    private double jumpMultiplier;
    private double fallSpeedLimit;
    private int fallViolations, ascendTicks;
    private Verbose verbose = new Verbose();
    private Verbose verbose1 = new Verbose();
    @Override
    public void run(Player player, PositionUpdate update) {
        val from = update.getFrom();
        val to = update.getTo();
        if(playerData.getVelocityManager().getLastVelocityTicks() > 0) {
            return;
        }
        int maxascendticks = 10;
        if (from.getX() == to.getX() && from.getY() == to.getY() && from.getZ() == to.getZ()) {
            return;
        }
        if(BlockUtils.isNearFence(player))
            return;

        val dy = to.getY() - from.getY();
        if (this.playerData.isBelowBlock() || this.playerData.isWasBelowBlock() || this.playerData.isWasOnWall() || this.playerData.isOnWall()) {
            resetLimits(player);
            return;
        }
        if (player.isFlying() || player.isInsideVehicle() || this.playerData.isOnGround() || this.playerData.isInLiquid() || this.playerData.isOnLadder()) {
            this.resetLimits(player);
            return;
        }


        if (this.playerData.isWasOnGround()) {
            this.resetLimits(player);
        }

        if(getJumpBoostAmplifier(player) > 0) {
            this.jumpLimit += 2;
            maxascendticks += 5;

        }
        if (this.playerData.isWasInLiquid()) {
            this.jumpLimit += 0.18;
        }

        if (dy > 0.0) {
            if (dy > this.jumpLimit + this.playerData.getVelocityManager().getMaxVertical()) {
                this.onViolation(player, ascendTicks + "");
                this.jumpLimit *= this.jumpMultiplier;
                this.jumpMultiplier -= 0.025;
            }
            if (ascendTicks++ > maxascendticks) {
                this.onViolation(player, ascendTicks + "");
            }
        } else if (dy < 0.0) {
            if (this.fallSpeedLimit - dy < 0.2 && ++this.fallViolations == 6 && verbose.flag(3, 3000)) {

                this.onViolation(player, ascendTicks + "");


                this.resetLimits(player);
            }

            this.fallSpeedLimit -= 0.01;
        } else if (dy == 0.0 && ++this.fallViolations == 5 && verbose1.flag(3, 3000)) {
            this.onViolation(player, ascendTicks + "");

            this.resetLimits(player);
        }
        super.run(player, update);
    }
    private void resetLimits(Player player) {
        val jumpAmplifier = this.getJumpBoostAmplifier(player) ;
        this.jumpMultiplier = 0.8 + 0.03 * jumpAmplifier;
        this.jumpLimit = 0.42 + 0.11 * jumpAmplifier;
        this.fallSpeedLimit = 0.078;
        this.fallViolations = 0;
        this.ascendTicks = 0;
    }

    private int getJumpBoostAmplifier(Player player) {
        if (player.hasPotionEffect(PotionEffectType.JUMP)) {
            for (PotionEffect effect : player.getActivePotionEffects()) {
                if (effect.getType().equals(PotionEffectType.JUMP)) {
                    return effect.getAmplifier() + 1;
                }
            }
        }
        return 0;
    }
}
