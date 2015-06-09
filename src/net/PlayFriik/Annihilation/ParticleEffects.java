package net.PlayFriik.Annihilation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public enum ParticleEffects {

	HUGE_EXPLODE(EnumParticle.EXPLOSION_HUGE, 0), LARGE_EXPLODE(EnumParticle.EXPLOSION_LARGE, 1), FIREWORK_SPARK(
			EnumParticle.FIREWORKS_SPARK, 2), AIR_BUBBLE(EnumParticle.WATER_BUBBLE, 3), SUSPEND(EnumParticle.SUSPENDED, 4), DEPTH_SUSPEND(
					EnumParticle.SUSPENDED_DEPTH, 5), TOWN_AURA(EnumParticle.TOWN_AURA, 6), CRITICAL_HIT(
							EnumParticle.CRIT, 7),MAGIC_CRITICAL_HIT(EnumParticle.CRIT_MAGIC, 8), MOB_SPELL(EnumParticle.SPELL_MOB, 9), MOB_SPELL_AMBIENT(
									EnumParticle.SPELL_MOB_AMBIENT, 10), SPELL(EnumParticle.SPELL, 11), INSTANT_SPELL(
											EnumParticle.SPELL_INSTANT, 12), BLUE_SPARKLE(EnumParticle.SPELL_WITCH, 13), NOTE_BLOCK(
													EnumParticle.NOTE, 14), ENDER(EnumParticle.PORTAL, 15), ENCHANTMENT_TABLE(
															EnumParticle.ENCHANTMENT_TABLE, 16), EXPLODE(EnumParticle.EXPLOSION_NORMAL, 17), FIRE(EnumParticle.FLAME, 18), LAVA_SPARK(
																	EnumParticle.LAVA, 19), FOOTSTEP(EnumParticle.FOOTSTEP, 20), SPLASH(EnumParticle.WATER_SPLASH, 21), LARGE_SMOKE(
																			EnumParticle.SMOKE_LARGE, 22), CLOUD(EnumParticle.CLOUD, 23), REDSTONE_DUST(EnumParticle.REDSTONE, 24), SNOWBALL_HIT(
																					EnumParticle.SNOWBALL, 25), DRIP_WATER(EnumParticle.DRIP_WATER, 26), DRIP_LAVA(
																							EnumParticle.DRIP_LAVA, 27), SNOW_DIG(EnumParticle.SNOW_SHOVEL, 28), SLIME(EnumParticle.SLIME, 29), HEART(
																									EnumParticle.HEART, 30), ANGRY_VILLAGER(EnumParticle.VILLAGER_ANGRY, 31), GREEN_SPARKLE(
																											EnumParticle.VILLAGER_HAPPY, 32), ICONCRACK(EnumParticle.BLOCK_CRACK, 33), TILECRACK(
																													EnumParticle.BLOCK_CRACK, 34);

	private static Object createPacket(ParticleEffects effect, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count) throws Exception {
		if (count <= 0) {
			count = 1;
		}
		PacketPlayOutWorldParticles pp = new PacketPlayOutWorldParticles(effect.get(), true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), offsetX, offsetY, offsetZ, speed, count);
		return pp;
	}

	private static Object createPacket(EnumParticle effect, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count) throws Exception {
		if (count <= 0) {
			count = 1;
		}

		Constructor<?>[] cc = PacketPlayOutWorldParticles.class.getDeclaredConstructors();
		for(Constructor<?> c:cc){
			Class<?>[] classes = c.getParameterTypes();
			StringBuilder sb = new StringBuilder();
			for(Class<?> cl:classes){
				sb.append("[").append(cl.getName()).append("]");
			}
			System.out.println(sb.toString());
		}

		PacketPlayOutWorldParticles pp = new PacketPlayOutWorldParticles(effect, true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), offsetX, offsetY, offsetZ, speed, count);
		return pp;
	}

	private static Object getHandle(Entity entity) {
		try {
			Method entity_getHandle = entity.getClass().getMethod("getHandle");
			Object nms_entity = entity_getHandle.invoke(entity);
			return nms_entity;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private static void sendPacket(Player p, Object packet) throws Exception {
		Object eplayer = getHandle(p);
		Field playerConnectionField = eplayer.getClass().getField("playerConnection");
		Object playerConnection = playerConnectionField.get(eplayer);
		for (Method m : playerConnection.getClass().getMethods()) {
			if (m.getName().equalsIgnoreCase("sendPacket")) {
				m.invoke(playerConnection, packet);
				return;
			}
		}
	}

	public static void sendToLocation(ParticleEffects effect, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count) {
		try {
			Object packet = createPacket(effect, location, offsetX, offsetY, offsetZ, speed, count);
			for (Player player : Bukkit.getOnlinePlayers()) {
				sendPacket(player, packet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendToLocation(EnumParticle effect, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count) {
		try {
			Object packet = createPacket(effect, location, offsetX, offsetY,
					offsetZ, speed, count);
			for (Player player : Bukkit.getOnlinePlayers()) {
				sendPacket(player, packet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendToPlayer(ParticleEffects effect, Player player, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count) {
		try {
			Object packet = createPacket(effect, location, offsetX, offsetY,
					offsetZ, speed, count);
			sendPacket(player, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private EnumParticle name;

	private int id;

	ParticleEffects(EnumParticle name, int id) {
		this.name = name;
		this.id = id;
	}

	int getId() {
		return id;
	}

	EnumParticle get() {
		return name;
	}
}