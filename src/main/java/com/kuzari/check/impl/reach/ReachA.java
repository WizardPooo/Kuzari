package com.kuzari.check.impl.reach;

import com.kuzari.Kuzari;
import com.kuzari.check.type.PacketCheck;
import com.kuzari.data.PlayerData;
import com.kuzari.update.PlayerLocation;
import com.kuzari.util.MathUtil;
import com.kuzari.util.NmsUtil;
import lombok.val;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ReachA extends PacketCheck {
    public ReachA(Kuzari plugin, PlayerData playerData, String name, int maxvl, String type) {
        super(plugin, playerData, "Reach", 30, "A");
    }
    private Kuzari plugin;

    private int vl;
    @Override
    public void run(Player player, Packet packet) {
        if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity) packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
            EntityPlayer nmsPlayer = NmsUtil.getNmsPlayer(player);
            Entity entity = ((PacketPlayInUseEntity) packet).a(nmsPlayer.world);
            org.bukkit.entity.Entity bukkitEntity = entity.getBukkitEntity();
            if(bukkitEntity == null)
                return;
            if (bukkitEntity instanceof Player) {
                Player target = (Player) bukkitEntity;

                if (System.currentTimeMillis() - this.playerData.getLastDelayedPacket() <= 160L || System.currentTimeMillis() - this.plugin.getData(target).getLastDelayedPacket() <= 160L
                        || this.playerData.getPlayerLocations().size() <= 10 || this.plugin.getData(target).getPlayerLocations().size() <= 10) {
                    return;
                }

                PlayerLocation targetLocation = this.plugin.getData(target).getPlayerLocations().get(this.plugin.getData(target).getPlayerLocations().size() - MathUtil.timeToTicks(target)),
                        playerLocation = this.playerData.getPlayerLocations().get(this.playerData.getPlayerLocations().size() - MathUtil.timeToTicks(player));

                if (targetLocation == null || playerLocation == null) {
                    return;
                }

                Location attackerLocation = new Location(player.getWorld(), playerLocation.getX(), playerLocation.getY(), playerLocation.getZ());
                Location attackedLocation = new Location(target.getWorld(), targetLocation.getX(), targetLocation.getY(), targetLocation.getZ());


                double reach = attackedLocation.toVector().setY(0).distance(attackerLocation.toVector().setY(0));
                val kbxz = Math.hypot(this.playerData.getVelocityX(), this.playerData.getVelocityZ());

                reach -= kbxz;
                reach -= MathUtil.getHitboxSize(attackerLocation.getYaw());
                reach -= this.playerData.getPlayerMoveSpeed();

                if (reach > 3.15) {
                    if (vl++ > 10) {
                        onViolation(player, "failed " + name);
                    }
                } else {
                    if (vl > 0) {
                        vl = Math.max(vl - 1, 0);
                    }
                }
            }
        }
    }
}
