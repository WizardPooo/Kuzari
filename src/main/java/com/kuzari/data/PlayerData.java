package com.kuzari.data;

import com.kuzari.Kuzari;
import com.kuzari.check.Check;
import com.kuzari.check.impl.aimassist.AimAssistA;
import com.kuzari.check.impl.aimassist.AimAssistB;
import com.kuzari.check.impl.aimassist.AimAssistC;
import com.kuzari.check.impl.badpackets.BadPacketsA;
import com.kuzari.check.impl.badpackets.BadPacketsB;
import com.kuzari.check.impl.fly.FlyA;
import com.kuzari.check.impl.killaura.KillAuraA;
import com.kuzari.check.impl.killaura.KillAuraB;
import com.kuzari.check.impl.killaura.KillAuraC;
import com.kuzari.check.impl.killaura.KillAuraD;
import com.kuzari.util.location.Direction;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Setter
@Getter
public class PlayerData {
    //Checks here
    public final Class[] checks = new Class[] {

        AimAssistA.class, AimAssistB.class, AimAssistC.class,
        FlyA.class,
        BadPacketsA.class, BadPacketsB.class,
        KillAuraA.class, KillAuraB.class, KillAuraC.class, KillAuraD.class,
    };
    /*
    TODO: Remove every @Getter on vars
     */
    @Setter
    public int blockAboveTicks;
    @Setter
    private int aimAssistvalues;
    @Getter @Setter
    private final List<Check> checkList = new ArrayList<>();
    @Getter @Setter
    private com.kuzari.update.PlayerLocation lastLocation;
    @Setter
    private  Set<Direction> boxSidesTouchingBlocks;
    @Getter @Setter
    private boolean isBanned;
    @Getter @Setter
    private long lastJoinTime, lastDelayedPacket, lastFlying;
    @Getter @Setter
    private final List<com.kuzari.update.PlayerLocation> playerLocations = new ArrayList<>();
    @Getter
    public com.kuzari.data.impl.ActionManager actionManager = new com.kuzari.data.impl.ActionManager();
    private final com.kuzari.listener.MovementParser movementParser = new com.kuzari.listener.MovementParser();

    @Getter @Setter
    public boolean sprinting;

    public boolean lastOnGround;
    public double lastDistY;
    public boolean onGround;
    public boolean lastLastOnGround;

    @Setter
    public boolean alerts;

    private double velocityX;
    private double velocityY;
    private double velocityZ;

    private com.kuzari.data.impl.TeleportManager teleportManager = new com.kuzari.data.impl.TeleportManager();
    private com.kuzari.data.impl.VelocityManager velocityManager = new com.kuzari.data.impl.VelocityManager();
    public PlayerData(Kuzari plugin) {

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            Arrays.stream(checks).forEach(clazz -> {
                Check<?> check;

                try {
                    check = (Check<?>) clazz
                            .getConstructor(Kuzari.class, PlayerData.class)
                            .newInstance(plugin, this);
                    /*
                    TODO: Add config system here!
                     */
                } catch (InstantiationException | NoSuchMethodException |
                        InvocationTargetException | IllegalAccessException e) {

                    e.printStackTrace();

                    throw new RuntimeException("Unknown check error!");
                }
                this.checkList.add(check);
            });
        });
        boxSidesTouchingBlocks = new HashSet<>();

    }

    public boolean isOnGround() {
        return this.movementParser.onGround;
    }

    public boolean isInLiquid() {
        return this.movementParser.inLiquid;
    }

    public boolean isOnLadder() {
        return this.movementParser.onLadder;
    }

    public boolean isBelowBlock() {
        return this.movementParser.belowBlock;
    }

    public boolean isWasOnGround() {
        return this.movementParser.wasOnGround;
    }

    public boolean isWasInLiquid() {
        return this.movementParser.wasInLiquid;
    }

    public boolean isWasOnLadder() {
        return this.movementParser.wasOnLadder;
    }
    public boolean isWasOnWall() { return this.movementParser.wasOnWall;}
    public boolean isOnWall() { return this.movementParser.onWall;}
    public boolean isWasBelowBlock() {
        return this.movementParser.wasBelowBlock;
    }

    public boolean isInWeb() {
        return this.movementParser.inWeb;
    }

    public boolean isWasInWeb() {
        return this.movementParser.wasWeb;
    }




}
