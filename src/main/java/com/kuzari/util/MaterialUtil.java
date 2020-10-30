package com.kuzari.util;

import org.bukkit.Material;

public class MaterialUtil {

    private static final int[] MATERIAL_FLAGS = new int[256];

    public static final int SOLID = 1;
    public static final int LIQUID = 2;
    public static final int LADDER = 4;
    public static final int WALL = 8;
    public static final int STAIRS = 16;

    public static final int COBWEB = 32;

    static {
        for (int i = 0; i < MATERIAL_FLAGS.length; i++) {
            Material material = Material.values()[i];

            if (material.isSolid()) {
                MATERIAL_FLAGS[i] |= SOLID;
            }

            if (material.name().endsWith("_STAIRS")) {
                MATERIAL_FLAGS[i] |= STAIRS;
            }

            if (material.name().toUpperCase().contains("WEB")) {
                MATERIAL_FLAGS[i] |= COBWEB;
            }
        }

        // fix some types where isSolid() returns the wrong value
        MATERIAL_FLAGS[Material.SIGN_POST.getId()] = 0;
        MATERIAL_FLAGS[Material.WALL_SIGN.getId()] = 0;
        MATERIAL_FLAGS[Material.DIODE_BLOCK_OFF.getId()] = SOLID;
        MATERIAL_FLAGS[Material.DIODE_BLOCK_ON.getId()] = SOLID;
        MATERIAL_FLAGS[Material.CARPET.getId()] = SOLID;
        MATERIAL_FLAGS[Material.SNOW.getId()] = SOLID;
        MATERIAL_FLAGS[Material.ANVIL.getId()] = SOLID;
        MATERIAL_FLAGS[Material.SLIME_BLOCK.getId()] = SOLID;

        // liquids
        MATERIAL_FLAGS[Material.WATER.getId()] |= LIQUID;
        MATERIAL_FLAGS[Material.STATIONARY_WATER.getId()] |= LIQUID;
        MATERIAL_FLAGS[Material.LAVA.getId()] |= LIQUID;
        MATERIAL_FLAGS[Material.STATIONARY_LAVA.getId()] |= LIQUID;

        // ladders
        MATERIAL_FLAGS[Material.LADDER.getId()] |= LADDER | SOLID;
        MATERIAL_FLAGS[Material.VINE.getId()] |= LADDER | SOLID;

        // walls
        MATERIAL_FLAGS[Material.FENCE.getId()] |= WALL;
        MATERIAL_FLAGS[Material.FENCE_GATE.getId()] |= WALL;
        MATERIAL_FLAGS[Material.COBBLE_WALL.getId()] |= WALL;
        MATERIAL_FLAGS[Material.NETHER_FENCE.getId()] |= WALL;

        // trapdoors
        MATERIAL_FLAGS[Material.TRAP_DOOR.getId()] |= SOLID;
        MATERIAL_FLAGS[Material.IRON_TRAPDOOR.getId()] |= SOLID;
    }

    private MaterialUtil() {
    }

    public static boolean checkFlag(Material material, int flag) {
        return (MATERIAL_FLAGS[material.getId()] & flag) == flag;
    }
}
