package com.kuzari.data.impl;

import com.kuzari.data.Velocity;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.function.Predicate;
public class VelocityManager {

    @Getter
    private final List<Velocity> velocities = new ArrayList<>();
    @Getter @Setter
    private int lastVelocityTicks;

    private final Predicate<Velocity> shouldRemoveVelocity = velocity -> {
        val now = System.currentTimeMillis();

        return velocity.getCreationTime() + 2000L < now;
    };


    public void addVelocityEntry(double x, double y, double z) {
        lastVelocityTicks = 25;
        this.velocities.add(new Velocity(x * x + z * z, Math.abs(y)));
    }


    public double getMaxHorizontal() {
        this.velocities.removeIf(shouldRemoveVelocity);

        try {
            return Math.sqrt(this.velocities.stream()
                    .mapToDouble(Velocity::getHorizontal)
                    .max()
                    .orElse(0.f));
        } catch (ConcurrentModificationException e) {
            return 1.0;
        }
    }


    public double getMaxVertical() {
        this.velocities.removeIf(shouldRemoveVelocity);

        return this.velocities.stream()
                .mapToDouble(Velocity::getVertical)
                .max()
                .orElse(0.f);
    }


}
