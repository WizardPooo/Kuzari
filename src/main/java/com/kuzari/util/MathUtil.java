package com.kuzari.util;

import com.kuzari.Kuzari;
import lombok.val;
import lombok.var;
import net.minecraft.server.v1_8_R3.MathHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class MathUtil {
    private static ArrayList<String> weirdBlocks = new ArrayList<String>(), passable = new ArrayList<String>();

    static {
        weirdBlocks.add("WEB");
        weirdBlocks.add("SLAB");
        weirdBlocks.add("SNOW");
        weirdBlocks.add("STAIR");
        weirdBlocks.add("DOOR");
        weirdBlocks.add("TRAPDOOR");
        weirdBlocks.add("FENCE");
        weirdBlocks.add("SENSOR");
        weirdBlocks.add("REPEATER");
        weirdBlocks.add("COMPARATOR");
        weirdBlocks.add("GATE");
        weirdBlocks.add("CARPET");
        weirdBlocks.add("BED"); // ffs bedrock?
        weirdBlocks.add("WALL");
        weirdBlocks.add("SKULL");
        weirdBlocks.add("LILY");
        weirdBlocks.add("HONEY");

        passable.add("FLOWER");
        passable.add("ROSE");
        passable.add("DOOR");
        passable.add("GATE");
        passable.add("SAPLING");
        passable.add("GRASS");
        passable.add("TORCH");
        passable.add("MUSHROOM");
        passable.add("WART");
        passable.add("VINE");
        passable.add("LILAC");
        passable.add("PLANT");
        passable.add("BANNER");
        passable.add("PORTAL");
        passable.add("STAND");
        passable.add("SIGN");
        passable.add("LADDER");
        passable.add("WHEAT");
        passable.add("PUMPKIN");
        passable.add("MELON");
        passable.add("SEED");
        passable.add("RAIL");
        passable.add("REPEATER");
        passable.add("PLATE");
        passable.add("HOOK");
        passable.add("BUTTON");
        passable.add("LEVER");
        passable.add("AIR"); // what???????
        passable.add("FIRE");
        passable.add("LAVA");
        passable.add("WATER");
        passable.add("WIRE");
        passable.add("THIN_");
    }
    public static Map<EntityType, Vector> entityDimensions;
    public MathUtil() {
        entityDimensions = new HashMap<>();
        entityDimensions.put(EntityType.WOLF, new Vector(0.31, 0.8, 0.31));
        entityDimensions.put(EntityType.SHEEP, new Vector(0.45, 1.3, 0.45));
        entityDimensions.put(EntityType.COW, new Vector(0.45, 1.3, 0.45));
        entityDimensions.put(EntityType.PIG, new Vector(0.45, 0.9, 0.45));
        entityDimensions.put(EntityType.MUSHROOM_COW, new Vector(0.45, 1.3, 0.45));
        entityDimensions.put(EntityType.WITCH, new Vector(0.31, 1.95, 0.31));
        entityDimensions.put(EntityType.BLAZE, new Vector(0.31, 1.8, 0.31));
        entityDimensions.put(EntityType.PLAYER, new Vector(0.3, 1.8, 0.3));
        entityDimensions.put(EntityType.VILLAGER, new Vector(0.31, 1.8, 0.31));
        entityDimensions.put(EntityType.CREEPER, new Vector(0.31, 1.8, 0.31));
        entityDimensions.put(EntityType.GIANT, new Vector(1.8, 10.8, 1.8));
        entityDimensions.put(EntityType.SKELETON, new Vector(0.31, 1.8, 0.31));
        entityDimensions.put(EntityType.ZOMBIE, new Vector(0.31, 1.8, 0.31));
        entityDimensions.put(EntityType.SNOWMAN, new Vector(0.35, 1.9, 0.35));
        entityDimensions.put(EntityType.HORSE, new Vector(0.7, 1.6, 0.7));
        entityDimensions.put(EntityType.ENDER_DRAGON, new Vector(1.5, 1.5, 1.5));

        entityDimensions.put(EntityType.ENDERMAN, new Vector(0.31, 2.9, 0.31));
        entityDimensions.put(EntityType.CHICKEN, new Vector(0.2, 0.7, 0.2));
        entityDimensions.put(EntityType.OCELOT, new Vector(0.31, 0.7, 0.31));
        entityDimensions.put(EntityType.SPIDER, new Vector(0.7, 0.9, 0.7));
        entityDimensions.put(EntityType.WITHER, new Vector(0.45, 3.5, 0.45));
        entityDimensions.put(EntityType.IRON_GOLEM, new Vector(0.7, 2.9, 0.7));
        entityDimensions.put(EntityType.GHAST, new Vector(2, 4, 2));
    }
    public static double getDelta(double one, double two) {
        return Math.abs(one - two);
    }
    public static final double HITBOX_NORMAL = 0.4;
    public static final double EXPANDER = Math.pow(2, 24);
    public static final double MIN_EXPANSION_VALUE = 0b100000000000000000;
    public static final double MIN_EUCILDEAN_VALUE = 0b100000000000000;
    public static final double HITBOX_DIAGONAL = Math.sqrt(Math.pow(HITBOX_NORMAL, 2) + Math.pow(HITBOX_NORMAL, 2));

    public static int timeToTicks(Player player) {
        return (int) Math.round(NmsUtil.getNmsPlayer(player).ping / 2L / 50.0) + 2;
    }
    public static double[] getOffsetFromEntity(Player player, Entity entity) {
        double yawOffset = Math.abs(yawTo180F(player.getEyeLocation().getYaw()) - yawTo180F(getRotations(player.getLocation(), entity.getLocation())[0]));
        double pitchOffset = Math.abs(Math.abs(player.getEyeLocation().getPitch()) - Math.abs(getRotations(player.getLocation(), entity.getLocation())[1]));
        return new double[]{yawOffset, pitchOffset};
    }

    public static boolean looked(Location from, Location to) {
        return (from.getYaw() != 0 && to.getYaw() != 0) || (from.getPitch() != 0 && from.getPitch() != 0);
    }
    public static float[] getAngles(final Player player, final Entity e, final Location from, final Location to) {
        return new float[]{(float) (getYawChangeToEntity(player, e, from, to) + getYawDifference(from, to)), (float) (getPitchChangeToEntity(player, e, from, to) + getYawDifference(from, to))};
    }
    public static float getYawChangeToEntity(final Player player, final Entity entity, final Location from, final Location to) {
        final double deltaX = entity.getLocation().getX() - player.getLocation().getX();
        final double deltaZ = entity.getLocation().getZ() - player.getLocation().getZ();
        double yawToEntity;
        if (deltaZ < 0.0 && deltaX < 0.0) {
            yawToEntity = 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
        } else if (deltaZ < 0.0 && deltaX > 0.0) {
            yawToEntity = -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
        } else {
            yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }
        return Float.valueOf((float) (-getYawDifference(from, to) - yawToEntity));
    }

    public static float getPitchChangeToEntity(final Player player, final Entity entity, final Location from, final Location to) {
        final double deltaX = entity.getLocation().getX() - player.getLocation().getX();
        final double deltaZ = entity.getLocation().getZ() - player.getLocation().getZ();
        final double deltaY = player.getLocation().getY() - 1.6 + 2.0 - 0.4 - entity.getLocation().getY();
        final double distanceXZ = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        final double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
        return yawTo180F((float) (-(getYawDifference(from, to) - (float) pitchToEntity)));
    }
    public static float getAimDetection(final Player player, final Entity entity) {
        final float[] neededRotations = getRotationsNeeded(player, entity);
        val pd = Kuzari.getInstance().getData(player);
        if (neededRotations != null) {
            final float answer = Math.abs(pd.getLastLocation().getYaw() - neededRotations[0]);
            return answer;
        }
        return -1.0f;
    }

    public static float[] getRotationsNeeded(final Player player, final Entity entity) {
        if (entity == null) {
            return null;
        }
        val pd = Kuzari.getInstance().getData(player);
        final double diffX = entity.getLocation().getX() - pd.getLastLocation().getX();
        final double diffZ = entity.getLocation().getZ() - pd.getLastLocation().getZ();
        if (entity instanceof LivingEntity) {
            final LivingEntity entityLivingBase = (LivingEntity)entity;
            final double diffY = entityLivingBase.getLocation().getY() + entityLivingBase.getEyeHeight() - (pd.getLastLocation().getY() + player.getEyeHeight());
            final double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
            final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
            final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
            return new float[] { pd.getLastLocation().getYaw() + wrap(yaw - pd.getLastLocation().getYaw()), pd.getLastLocation().getPitch() + wrap(pitch - pd.getLastLocation().getPitch()) };
        }
        return new float[] { 0.0f, 0.0f };
    }
    public static float wrap(float value) {
        value %= 360.0f;
        if (value >= 180.0f) {
            value -= 360.0f;
        }
        if (value < -180.0f) {
            value += 360.0f;
        }
        return value;
    }
    public static double getAngle(Player p, Entity entity) {
        Location loc1 = p.getLocation();
        Location loc2 = entity.getLocation();
        loc1.setY(1.0);
        loc2.setY(1.0);
        Vector localVector1 = loc2.toVector().subtract(loc1.toVector());
        Location localLocation = loc1.clone();
        localLocation.setPitch(0.0f);
        Vector localVector2 = localLocation.getDirection();
        return localVector1.normalize().dot(localVector2);
    }
    public static int floor(double var0) {
        int var2 = (int) var0;
        return var0 < var2 ? var2 - 1 : var2;
    }
    public static float yawTo180F(float flub) {
        if ((flub %= 360.0f) >= 180.0f) {
            flub -= 360.0f;
        }
        if (flub < -180.0f) {
            flub += 360.0f;
        }
        return flub;
    }

    public static double yawTo180D(double dub) {
        dub %= 360.0D;
        if (dub >= 180.0D) {
            dub -= 360.0D;
        }
        if (dub < -180.0D) {
            dub += 360.0D;
        }
        return dub;
    }
    public boolean isBlockAbove(Location loc) {
        if (loc != null) {
            double y = 1.8;

            if (!isAir(loc.clone().add(0, y, 0).getBlock().getType())
                    && !isLiquid(loc.clone().add(0, y, 0).getBlock().getType())
                    && !isPassable(loc.clone().add(0, y, 0).getBlock().getType())) {
                return true;
            }
            double x = loc.getX() % 1.0 >= 0 ? loc.getX() % 1 : 1 - Math.abs(loc.getX() % 1),
                    z = loc.getZ() % 1.0 >= 0 ? loc.getZ() % 1 : 1 - Math.abs(loc.getZ() % 1);

            if (!isAir(loc.clone().add(x, y, z).getBlock().getType())
                    && !isLiquid(loc.clone().add(x, y, z).getBlock().getType())
                    && !isPassable(loc.clone().add(x, y, z).getBlock().getType())) {
                return true;
            }

            if (x < 0.3 && z < 0.3) {
                if (!isAir(loc.clone().add(-1, y, -1).getBlock().getType())
                        && !isLiquid(loc.clone().add(-1, y, -1).getBlock().getType())
                        && !isPassable(loc.clone().add(-1, y, -1).getBlock().getType())) {
                    return true;
                }
            }

            if (x > 0.7 && z > 0.7) {
                if (!isAir(loc.clone().add(1, y, 1).getBlock().getType())
                        && !isLiquid(loc.clone().add(1, y, 1).getBlock().getType())
                        && !isPassable(loc.clone().add(1, y, 1).getBlock().getType())) {
                    return true;
                }
            }

            if (x > 0.7 && z < 0.3) {
                if (!isAir(loc.clone().add(1, y, -1).getBlock().getType())
                        && !isLiquid(loc.clone().add(1, y, -1).getBlock().getType())
                        && !isPassable(loc.clone().add(1, y, -1).getBlock().getType())) {
                    return true;
                }
            }

            if (x < 0.3 && z > 0.7) {
                if (!isAir(loc.clone().add(-1, y, 1).getBlock().getType())
                        && !isLiquid(loc.clone().add(-1, y, 1).getBlock().getType())
                        && !isPassable(loc.clone().add(-1, y, 1).getBlock().getType())) {
                    return true;
                }
            }

            if (x < 0.3) {
                if (!isAir(loc.clone().add(-1, y, 0).getBlock().getType())
                        && !isLiquid(loc.clone().add(-1, y, 0).getBlock().getType())
                        && !isPassable(loc.clone().add(-1, y, 0).getBlock().getType())) {
                    return true;
                }
            }

            if (z < 0.3) {
                if (!isAir(loc.clone().add(0, y, -1).getBlock().getType())
                        && !isLiquid(loc.clone().add(0, y, -1).getBlock().getType())
                        && !isPassable(loc.clone().add(0, y, -1).getBlock().getType())) {
                    return true;
                }
            }

            if (x > 0.7) {
                if (!isAir(loc.clone().add(1, y, 0).getBlock().getType())
                        && !isLiquid(loc.clone().add(1, y, 0).getBlock().getType())
                        && !isPassable(loc.clone().add(1, y, 0).getBlock().getType())) {
                    return true;
                }
            }

            if (z > 0.7) {
                if (!isAir(loc.clone().add(0, y, 1).getBlock().getType())
                        && !isLiquid(loc.clone().add(0, y, 1).getBlock().getType())
                        && !isPassable(loc.clone().add(0, y, 1).getBlock().getType())) {
                    return true;
                }
            }
        }
        return false;
    }


    public static float[] getRotations(Player one, LivingEntity two) {
        double diffX = two.getLocation().getX() - one.getLocation().getX();
        double diffZ = two.getLocation().getZ() - one.getLocation().getZ();

        BoundingBox box = getEntityBoundingBox(two);
        double diffY = box.maxY - (MathUtil.getEntityBoundingBox(one).maxY);
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float) (-Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }
    public boolean isAir(Material type) {
        if (type.name().contains("AIR")) {
            return true;
        }
        return false;
    }
    private boolean isPassable(Material m) {
        for (String s : passable) {
            if (m.toString().equals(s) || m.toString().contains(s)) {
                return true;
            }
        }
        return false;
    }

    public Boolean isLiquid(Material mat) {
        return (mat.name().contains("WATER") || mat.name().contains("LAVA"));
    }

    public static BoundingBox getEntityBoundingBox(LivingEntity entity) {
        if (entityDimensions.containsKey(entity.getType())) {
            Vector entityVector = entityDimensions.get(entity.getType());

            float minX = (float) Math.min(-entityVector.getX() + entity.getLocation().getX(), entityVector.getX() + entity.getLocation().getX());
            float minY = (float) Math.min(entity.getLocation().getY(), entityVector.getY() + entity.getLocation().getY());
            float minZ = (float) Math.min(-entityVector.getZ() + entity.getLocation().getZ(), entityVector.getZ() + entity.getLocation().getZ());
            float maxX = (float) Math.max(-entityVector.getX() + entity.getLocation().getX(), entityVector.getX() + entity.getLocation().getX());
            float maxY = (float) Math.max(entity.getLocation().getY(), entityVector.getY() + entity.getLocation().getY());
            float maxZ = (float) Math.max(-entityVector.getZ() + entity.getLocation().getZ(), entityVector.getZ() + entity.getLocation().getZ());
            return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
        }
        return ReflectionUtil.toBoundingBox(ReflectionUtil.getBoundingBox(entity));
    }
    public static float[] getRotations(Location one, Location two) {
        double diffX = two.getX() - one.getX();
        double diffZ = two.getZ() - one.getZ();
        double diffY = two.getY() + 2.0 - 0.4 - (one.getY() + 2.0);
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float) (-Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }

    public static List<Entity> getEntitiesInCone(List<Entity> entities, Location startpoint, int radius, int degrees, int direction)
    {
        List<Entity> newEntities = new ArrayList<Entity>();

        int[] startPos = new int[] { (int)startpoint.getX(), (int)startpoint.getZ() };

        int[] endA = new int[] { (int)(radius * Math.cos(direction - (degrees / 2))), (int)(radius * Math.sin(direction - (degrees / 2))) };

        for(Entity e : entities)
        {
            Location l = e.getLocation();
            int[] entityVector = getVectorForPoints(startPos[0], startPos[1], l.getBlockX(), l.getBlockY());

            double angle = getAngleBetweenVectors(endA, entityVector);
            if(Math.toDegrees(angle) < degrees && Math.toDegrees(angle) > 0)
                newEntities.add(e);
        }
        return newEntities;
    }
    /**
     * Created an integer vector in 2d between two points
     *
     * @param x1 - {@code int}, X pos 1
     * @param y1 - {@code int}, Y pos 1
     * @param x2 - {@code int}, X pos 2
     * @param y2 - {@code int}, Y pos 2
     * @return {@code int[]} - vector
     */
    public static int[] getVectorForPoints(int x1, int y1, int x2, int y2)
    {
        return new int[] { x2 - x1, y2 - y1 };
    }
    /**
     * Get the angle between two vectors.
     *
     * @param vector1 - {@code int[]}, vector 1
     * @param vector2 - {@code int[]}, vector 2
     * @return {@code double} - angle
     */
    public static double getAngleBetweenVectors(int[] vector1, int[] vector2)
    {
        return Math.atan2(vector2[1], vector2[0]) - Math.atan2(vector1[1], vector1[0]);
    }
    public static double getOffsetOffCursor(Player player, LivingEntity entity) {
        double offset = 0.0D;
        double[] offsets = getOffsetsOffCursor(player, entity);

        offset += offsets[0];
        offset += offsets[1];

        return offset;
    }
    public static Vector getRotation(Location one, Location two) {
        double dx = two.getX() - one.getX();
        double dy = two.getY() - one.getY();
        double dz = two.getZ() - one.getZ();
        double distanceXZ = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float) (Math.atan2(dz, dx) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float) -(Math.atan2(dy, distanceXZ) * 180.0D / 3.141592653589793D);
        return new Vector(yaw, pitch, 0.0F);
    }
    public static double getHorizontalDistance(Location to, Location from) {
        double x = Math.abs(Math.abs(to.getX()) - Math.abs(from.getX()));
        double z = Math.abs(Math.abs(to.getZ()) - Math.abs(from.getZ()));

        return Math.sqrt(x * x + z * z);
    }
    public static double[] getOffsetsOffCursor(Player player, LivingEntity entity) {
        Location entityLoc = entity.getLocation().add(0.0D, entity.getEyeHeight(), 0.0D);
        Location playerLoc = player.getLocation().add(0.0D, player.getEyeHeight(), 0.0D);

        Vector playerRotation = new Vector(playerLoc.getYaw(), playerLoc.getPitch(), 0.0F);
        Vector expectedRotation = getRotation(playerLoc, entityLoc);

        double deltaYaw = clamp180(playerRotation.getX() - expectedRotation.getX());
        double deltaPitch = clamp180(playerRotation.getY() - expectedRotation.getY());

        double horizontalDistance = getHorizontalDistance(playerLoc, entityLoc);
        double distance = getDistance3D(playerLoc, entityLoc);

        double offsetX = deltaYaw * horizontalDistance * distance;
        double offsetY = deltaPitch * Math.abs(Math.sqrt(entityLoc.getY() - playerLoc.getY())) * distance;

        return new double[]{Math.abs(offsetX), Math.abs(offsetY)};
    }
    public static double getDistance3D(Location one, Location two) {
        double toReturn = 0.0D;
        double xSqr = (two.getX() - one.getX()) * (two.getX() - one.getX());
        double ySqr = (two.getY() - one.getY()) * (two.getY() - one.getY());
        double zSqr = (two.getZ() - one.getZ()) * (two.getZ() - one.getZ());
        double sqrt = Math.sqrt(xSqr + ySqr + zSqr);
        toReturn = Math.abs(sqrt);
        return toReturn;
    }

    public static double getHitboxSize(float yaw) {
        var clamped = (int) Math.abs(MathUtil.clamp180(yaw)) % 90;

        if (clamped > 45) {
            clamped = 90 - clamped;
        }

        clamped /= 0.45;

        val opposite = 100 - clamped;

        val diagonal = HITBOX_DIAGONAL * (clamped / 100.0);
        val normal = HITBOX_NORMAL * (opposite / 100.0);

        return diagonal + normal;
    }
    public static double getYawDifference(Location one, Location two) {
        return Math.abs(one.getYaw() - two.getYaw());
    }
    public static float getDistanceBetweenAngles(float from, float to) {
        float distance = Math.abs(from - to) % 360.0f;
        return distance > 180.f ? 360.f - distance : distance;
    }

    public static long getGcd(long current, long previous) {
        return previous <= MathUtil.MIN_EUCILDEAN_VALUE ? current :
                MathUtil.getGcd(previous, MathUtil.getDelta(current, previous));
    }

    private static long getDelta(long alpha, long beta) {
        return alpha % beta;
    }

    public static double clamp180(double theta) {
        theta %= 360.0;

        if (theta >= 180.0) {
            theta -= 360.0;
        }

        if (theta < -180.0) {
            theta += 360.0;
        }
        return theta;
    }

    public static float[] getRotationFromPosition(Location location, Location secondLocation) {
        double deltaX = location.getX() - secondLocation.getX(),
                deltaY = location.getY() - (secondLocation.getY() + 0.4),
                deltaZ = location.getZ() - secondLocation.getZ();

        double distance = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        float yaw = (float)(Math.atan2(deltaZ, deltaX) * 180.0 / 3.141592653589793D) - 90.0F,
                pitch = (float)-(Math.atan2(deltaY, distance) * 180.0 / 3.141592653589793D);
        return new float[] { yaw, pitch };
    }

    public static double invSqrt(double x) {
        double xhalf = 0.5d * x;
        long i = Double.doubleToLongBits(x);
        i = 0x5fe6ec85e7de30daL - (i >> 1);
        x = Double.longBitsToDouble(i);
        x *= (1.5d - xhalf * x * x);
        return x;
    }

    public static float wrapAngleTo180_float(float value)
    {
        value = value % 360.0F;

        if (value >= 180.0F)
        {
            value -= 360.0F;
        }

        if (value < -180.0F)
        {
            value += 360.0F;
        }

        return value;
    }

    public static double getAngleDistance(float angle1, float angle2) {
        float distance = Math.abs(angle1 - angle2) % 360.0F;
        if (distance > 180.0F) {
            distance = 360.0F - distance;
        }
        return distance;
    }


    public static boolean isUsingOptifine(Location current, Location previous) {
        val yawChange = Math.abs(current.getYaw() - previous.getYaw());
        val pitchChange = Math.abs(current.getPitch() - previous.getPitch());

        val yawWrapped = MathUtil.wrapAngleTo180_float(yawChange);
        val pitchWrapped = MathUtil.wrapAngleTo180_float(pitchChange);

        return pitchWrapped < 2.5;
    }

    public static final Random RANDOM = new Random();

    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
