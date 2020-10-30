package com.kuzari.listener;

import com.kuzari.Kuzari;
import com.kuzari.data.PlayerData;
import com.kuzari.update.MovementHandler;
import com.kuzari.util.NmsUtil;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import sun.rmi.rmic.Main;

@RequiredArgsConstructor
public class PlayerListener implements Listener {
    private final Kuzari kuzari;
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        val player = event.getPlayer();
        kuzari.getData(player).setLastJoinTime(System.currentTimeMillis());
        val nmsPlayer = NmsUtil.getNmsPlayer(player);
        kuzari.getData(player).setBanned(false);
        this.kuzari.getPacketThread().execute(() -> nmsPlayer.playerConnection.networkManager.channel.pipeline()
                .addBefore("packet_handler",
                        "kuzari_packet_handler",
                        new PacketParser(kuzari, player, kuzari.getData(player))));

    }
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        MovementHandler movementHandler = new MovementHandler(kuzari);
        movementHandler.handleLocationUpdate(event.getPlayer(), event.getTo(), event.getFrom());

    }


}
