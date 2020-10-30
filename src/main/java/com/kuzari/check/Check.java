package com.kuzari.check;

import com.kuzari.Kuzari;
import com.kuzari.data.PlayerData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;


@RequiredArgsConstructor
public abstract class Check<T> {

    private final Kuzari plugin;
    public final PlayerData playerData;

    @Getter
    private final Class<T> clazz;

    @Getter
    public final String name;
    @Getter
    public final int maxvl;
    @Getter
    public final String type;
    private final List<Long> alerts = new ArrayList<>();

    public int getVl() {
        try {
            return Math.toIntExact(this.alerts.stream()
                    .filter(timestamp -> timestamp + 60000L > System.currentTimeMillis())
                    .count());
        } catch (ConcurrentModificationException e) {
        }

        return 0;
    }

    public void onViolation(Player player, String debug) {
            for(Player players : org.bukkit.Bukkit.getOnlinePlayers()) {
                if(playerData.isAlerts()) {
                    players.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', plugin.getFlagmessage().replace("%player%", player.getName()).replace("%check%", name).replace("%type%", type).replace("%vl%", getVl() + "")));
                }
            }
            PlayerData data = plugin.getData(player);
            if(!data.isBanned()) {
                if(getVl() >= maxvl) {
                    data.setBanned(true);
                    org.bukkit.Bukkit.getServer().dispatchCommand(org.bukkit.Bukkit.getConsoleSender(), plugin.getBanCmd().replace("%player%", player.getName()));

                }
            }


    }
    //This is where the check is ran
    public abstract void run(Player player, T p);

}



