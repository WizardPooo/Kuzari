package com.kuzari.check.impl.fly;

import com.kuzari.Kuzari;
import com.kuzari.check.type.PositionCheck;
import com.kuzari.data.PlayerData;
import com.kuzari.update.impl.PositionUpdate;
import com.kuzari.util.BlockUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FlyA extends PositionCheck {
    public FlyA(Kuzari plugin, PlayerData playerData, String name, int maxvl, String type) {
        super(plugin, playerData, "Fly", 10, "A");
    }
    private int verbose;
    @Override
    public void run(Player player, PositionUpdate update) {
        if (player.isFlying() || player.isInsideVehicle() || this.playerData.isOnGround() || this.playerData.isInLiquid() || this.playerData.isOnLadder()) {
            return;
        }
        if (playerData.blockAboveTicks > 0) {
            return;
        }
        if(BlockUtils.isNearWeb(player))
            return;
        if(isNearAbove(update.getTo()))
            return;
        if(isNearAbove1(update.getTo()))
            return;
        if(isNearAbove3(update.getTo())) // also anything you need help with?
            return;
        //A proper way to get the ground location of a play lying on packets (cough cough durpy)
        double distanceToGround = getDistanceToGround(player);
        if(distanceToGround < 2)
            return;
        double distY = update.getTo().getY() - update.getFrom().getY();
        double lastDistY = playerData.lastDistY;
        playerData.lastDistY = distY;
        final double predictedDist = (lastDistY - 0.08) * 0.9800000190734863;
        final boolean onGround = this.isNearGround(update.getTo());
        final boolean lastOnGround = playerData.lastOnGround;
        playerData.lastOnGround = onGround;
        final boolean lastLastOnGround = playerData.lastLastOnGround;
        playerData.lastLastOnGround = lastOnGround;
        if (!onGround && !lastOnGround && !lastLastOnGround && Math.abs(predictedDist) >= 0.005) {
            if (!this.isRoughlyEqual(distY, predictedDist)) {

                if (verbose++ > 5) {

                    onViolation(player, "failed " + name);

                    verbose = 0;
                }

            } else {
                verbose -= ((verbose > 0) ? 1 : 0);
            }
        }
            super.run(player, update);
    }
    public boolean isNearGround(Location location) {
        double expand = 0.3;
        for(double x = -expand; x <= expand; x+= expand) {
            for(double z = -expand; z <= expand; z += expand) {
                if(location.clone().add(x, -0.5001, z).getBlock().getType() != org.bukkit.Material.AIR) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isRoughlyEqual(double dl, double d2) {
        return Math.abs(dl - d2) < 0.001;
    }
    public boolean isNearAbove(Location location) {
        double expand = 0.3;
        for(double x = -expand; x <= expand; x+= expand) {
            for(double z = -expand; z <= expand; z += expand) {
                if(location.clone().add(x, +0.5001, z).getBlock().getType() != org.bukkit.Material.AIR) {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean isNearAbove1(Location location) {
        double expand = 0.3;
        for(double x = -expand; x <= expand; x+= expand) {
            for(double z = -expand; z <= expand; z += expand) {
                if(location.clone().add(x, +1.5001, z).getBlock().getType() != org.bukkit.Material.AIR) {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean isNearAbove2(Location location) {
        double expand = 0.3;
        for(double x = -expand; x <= expand; x+= expand) {
            for(double z = -expand; z <= expand; z += expand) {
                if(location.clone().add(x, +2.0001, z).getBlock().getType() != org.bukkit.Material.AIR) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isNearAbove3(Location location) {
        double expand = 0.3;
        for(double x = -expand; x <= expand; x+= expand) {
            for(double z = -expand; z <= expand; z += expand) {
                if(location.clone().add(x, +2.5001, z).getBlock().getType() != org.bukkit.Material.AIR) {
                    return true;
                }
            }
        }
        return false;
    }
    private int getDistanceToGround(Player p){
        Location loc = p.getLocation().clone();
        double y = loc.getBlockY();
        int distance = 0;
        for (double i = y; i >= 0; i--){
            loc.setY(i);
            if(loc.getBlock().getType().isSolid() || loc.getBlock().isLiquid())break;
            distance++;
        }
        return distance;
    }
}
