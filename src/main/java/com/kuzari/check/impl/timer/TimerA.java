package com.kuzari.check.impl.timer;

import com.kuzari.Kuzari;
import com.kuzari.check.type.PacketCheck;
import com.kuzari.data.PlayerData;
import com.kuzari.util.MovingStats;
import com.kuzari.util.Verbose;
import lombok.val;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.entity.Player;

public class TimerA extends PacketCheck  {
    private final MovingStats movingStats = new MovingStats(20);
    public Verbose verbose = new Verbose();
    private long lastFlyingTime;

    public TimerA(Kuzari plugin, PlayerData playerData, String name, int maxvl, String type) {
        super(plugin, playerData, "Timer", 50, "A");
    }
    @Override
    public void run(Player player, Packet packet) {
        if (packet instanceof PacketPlayInFlying) {
            val now = System.currentTimeMillis();
            val lastLocation = playerData.getLastLocation();
            if (lastLocation == null) {
                return;
            }


            //Can false on-join, so this is a quick patch.
            //This is also a quick fix for people who lag and send 0-1ms delays causing timer to false
            if (this.playerData.getTeleportManager().hasTeleported(lastLocation, 3.0) ||
                    System.currentTimeMillis() - this.playerData.getLastJoinTime() <= 9000) {
                return;
            }

            //add the packet change to our moving stats to compute it
            movingStats.add(now - this.lastFlyingTime);

            val max = 7.07;
            val stdDev = movingStats.getStdDev(max);

            //make sure the standard deviation of the packet change is less than max
            if (stdDev != 0.0E00 / 0.0E00 && stdDev < max && verbose.flag(200, 7000)) {


                this.onViolation(player, "failed " + name);

            }


            this.lastFlyingTime = now;
        }
    }
}
