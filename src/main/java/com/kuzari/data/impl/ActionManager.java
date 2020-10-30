package com.kuzari.data.impl;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ActionManager {

    @Setter
    private boolean digging;

    private boolean placing;
    private boolean attacking;
    private boolean swinging;
    private long lastAttack;

    private List<Long> cps = new ArrayList<>(), swings = new ArrayList<>();

    private List<Integer> lastCps = new ArrayList<>();
    private double averageCps;
    private long lastDamageAttack;
    private Entity lastEntity;
    public void onArmAnimation() {
        this.swinging = true;
    }

    public void onAttack(Entity entity) {
        if(!cps.isEmpty()) {
            cps.removeIf(t -> System.currentTimeMillis() -t > 1000);

        }
        cps.add(System.currentTimeMillis());
        averageCps += cps.size() > 0 ? cps.size() : 0;
        if (averageCps > 0) {
            averageCps /= 2;
        }
        this.attacking = true;

        this.lastEntity = entity;




        this.lastAttack = System.currentTimeMillis();

    }

    public void onPlace() {
        this.placing = true;
    }

    public void onFlying() {
        this.placing = false;
        this.attacking = false;
        this.swinging = false;
    }


}
