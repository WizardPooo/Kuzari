package com.kuzari.check.impl.killaura;

import com.kuzari.Kuzari;
import com.kuzari.check.type.PacketCheck;
import com.kuzari.data.PlayerData;
import com.kuzari.util.MathUtil;
import com.kuzari.util.NmsUtil;
import com.kuzari.util.Verbose;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

public class KillAuraE extends PacketCheck {
    public KillAuraE(Kuzari plugin, PlayerData playerData, String name, int maxvl, String type) {
        super(plugin, playerData, "KillAura", 30, "E");
    }

    private Verbose verbose = new Verbose();


    @Override
    public void run(Player player, Packet packet) {
        if(packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity) packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
            EntityPlayer nmsPlayer = NmsUtil.getNmsPlayer(player);
            Entity entity = ((PacketPlayInUseEntity) packet).a(nmsPlayer.world);
            org.bukkit.entity.Entity bukkitEntity = entity.getBukkitEntity();
            if(bukkitEntity == null)
                return;
            if(bukkitEntity instanceof Player) {
                if (System.currentTimeMillis() - this.playerData.getLastDelayedPacket() <= 160L || System.currentTimeMillis() - Kuzari.instance.getData((Player) bukkitEntity).getLastDelayedPacket() <= 160L
                        || this.playerData.getPlayerLocations().size() <= 10 || Kuzari.instance.getData((Player) bukkitEntity).getPlayerLocations().size() <= 10) {
                    return;
                }
                double[] r = MathUtil.getOffsetsOffCursor(player, (LivingEntity) bukkitEntity);
                if(r[0] >= 250 && verbose.flag(4, 989L)) {
                    onViolation(player, " " + r[0] + " : " + playerData.getLastDelayedPacket());
                }
            }

        }
        super.run(player, packet);
    }
}
