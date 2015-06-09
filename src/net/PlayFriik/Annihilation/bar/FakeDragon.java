/*******************************************************************************
 * Copyright 2014 stuntguy3000 (Luke Anderson) and coasterman10.
 *  
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 ******************************************************************************/
package net.PlayFriik.Annihilation.bar;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Location;

public class FakeDragon {
    public static final int MAX_HEALTH = 200;
    private int x;
    private int y;
    private int z;

    private int pitch = 0;
    private int yaw = 0;
    private byte xvel = 0;
    private byte yvel = 0;
    private byte zvel = 0;
    public float health = 0;
    private boolean visible = false;
    public String name;
    private Object world;

    private Object dragon;
    private int id;

    public FakeDragon(String name, Location loc, int percent) {
        this.name = name;
        this.x = loc.getBlockX();
        this.y = loc.getBlockY();
        this.z = loc.getBlockZ();
        this.health = percent / 100F * MAX_HEALTH;
        this.world = Util.getHandle(loc.getWorld());
    }

    public FakeDragon(String name, Location loc) {
        this.name = name;
        this.x = loc.getBlockX();
        this.y = loc.getBlockY();
        this.z = loc.getBlockZ();
        this.world = Util.getHandle(loc.getWorld());
    }

    public int getMaxHealth() {
        return MAX_HEALTH;
    }

    public void setHealth(int percent) {
        this.health = percent / 100F * MAX_HEALTH;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getPitch() {
        return pitch;
    }

    public void setPitch(int pitch) {
        this.pitch = pitch;
    }

    public int getYaw() {
        return yaw;
    }

    public void setYaw(int yaw) {
        this.yaw = yaw;
    }

    public byte getXvel() {
        return xvel;
    }

    public void setXvel(byte xvel) {
        this.xvel = xvel;
    }

    public byte getYvel() {
        return yvel;
    }

    public void setYvel(byte yvel) {
        this.yvel = yvel;
    }

    public byte getZvel() {
        return zvel;
    }

    public void setZvel(byte zvel) {
        this.zvel = zvel;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Object getWorld() {
        return world;
    }

    public void setWorld(Object world) {
        this.world = world;
    }

    public Object getSpawnPacket() {
        Class<?> Entity = Util.getCraftClass("Entity");
        Class<?> EntityLiving = Util.getCraftClass("EntityLiving");
        Class<?> EntityEnderDragon = Util.getCraftClass("EntityEnderDragon");
        Object packet = null;
        try {
            dragon = EntityEnderDragon.getConstructor(
                    Util.getCraftClass("World")).newInstance(getWorld());

            Method setLocation = Util.getMethod(EntityEnderDragon,
                    "setLocation", new Class<?>[] { double.class, double.class,
                            double.class, float.class, float.class });
            setLocation.invoke(dragon, getX(), getY(), getZ(), getPitch(),
                    getYaw());

            Method setInvisible = Util.getMethod(EntityEnderDragon,
                    "setInvisible", new Class<?>[] { boolean.class });
            setInvisible.invoke(dragon, isVisible());

            Method setCustomName = Util.getMethod(EntityEnderDragon,
                    "setCustomName", new Class<?>[] { String.class });
            setCustomName.invoke(dragon, name);

            Method setHealth = Util.getMethod(EntityEnderDragon, "setHealth",
                    new Class<?>[] { float.class });
            setHealth.invoke(dragon, health);

            Field motX = Util.getField(Entity, "motX");
            motX.set(dragon, getXvel());

            Field motY = Util.getField(Entity, "motX");
            motY.set(dragon, getYvel());

            Field motZ = Util.getField(Entity, "motX");
            motZ.set(dragon, getZvel());

            Method getId = Util.getMethod(EntityEnderDragon, "getId",
                    new Class<?>[] {});
            this.id = (Integer) getId.invoke(dragon);

            Class<?> PacketPlayOutSpawnEntityLiving = Util
                    .getCraftClass("PacketPlayOutSpawnEntityLiving");

            packet = PacketPlayOutSpawnEntityLiving.getConstructor(
                    new Class<?>[] { EntityLiving }).newInstance(dragon);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return packet;
    }

    public Object getDestroyPacket() {
        Class<?> PacketPlayOutEntityDestroy = Util
                .getCraftClass("PacketPlayOutEntityDestroy");

        Object packet = null;
        try {
            packet = PacketPlayOutEntityDestroy.newInstance();
            Field a = PacketPlayOutEntityDestroy.getDeclaredField("a");
            a.setAccessible(true);
            a.set(packet, new int[] { id });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return packet;
    }

    public Object getMetaPacket(Object watcher) {
        Class<?> DataWatcher = Util.getCraftClass("DataWatcher");

        Class<?> PacketPlayOutEntityMetadata = Util
                .getCraftClass("PacketPlayOutEntityMetadata");

        Object packet = null;
        try {
            packet = PacketPlayOutEntityMetadata.getConstructor(
                    new Class<?>[] { int.class, DataWatcher, boolean.class })
                    .newInstance(id, watcher, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return packet;
    }

    public Object getTeleportPacket(Location loc) {
        Class<?> PacketPlayOutEntityTeleport = Util
                .getCraftClass("PacketPlayOutEntityTeleport");

        Object packet = null;

        try {
            packet = PacketPlayOutEntityTeleport.getConstructor(
                    new Class<?>[] { int.class, int.class, int.class,
                            int.class, byte.class, byte.class }).newInstance(
                    this.id, loc.getBlockX() * 32, loc.getBlockY() * 32,
                    loc.getBlockZ() * 32,
                    (byte) ((int) loc.getYaw() * 256 / 360),
                    (byte) ((int) loc.getPitch() * 256 / 360));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return packet;
    }

    public Object getWatcher() {
        Class<?> Entity = Util.getCraftClass("Entity");
        Class<?> DataWatcher = Util.getCraftClass("DataWatcher");

        Object watcher = null;
        try {
            watcher = DataWatcher.getConstructor(new Class<?>[] { Entity })
                    .newInstance(dragon);
            Method a = Util.getMethod(DataWatcher, "a", new Class<?>[] {
                    int.class, Object.class });

            a.invoke(watcher, 0, isVisible() ? (byte) 0 : (byte) 0x20);
            a.invoke(watcher, 6, (Float) health);
            a.invoke(watcher, 7, (Integer) 0);
            a.invoke(watcher, 8, (Byte) (byte) 0);
            a.invoke(watcher, 10, name);
            a.invoke(watcher, 11, (Byte) (byte) 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return watcher;
    }
}
