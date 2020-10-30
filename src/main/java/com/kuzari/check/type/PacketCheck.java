package com.kuzari.check.type;

import com.kuzari.Kuzari;
import com.kuzari.check.Check;
import com.kuzari.data.PlayerData;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PacketCheck extends Check<Packet> {


    public PacketCheck(Kuzari plugin, PlayerData playerData,  String name, int maxvl, String type) {
        super(plugin, playerData, Packet.class, name, maxvl, type);
    }

    @Override
    public void run(Player player, Packet packet) {

    }

    public double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_EVEN);
        return bd.doubleValue();
    }
}
