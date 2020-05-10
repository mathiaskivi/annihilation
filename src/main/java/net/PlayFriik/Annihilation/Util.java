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
package net.PlayFriik.Annihilation;

import java.util.List;
import java.util.Random;

import net.PlayFriik.Annihilation.object.GameTeam;
import net.PlayFriik.Annihilation.object.Kit;
import net.PlayFriik.Annihilation.object.PlayerMeta;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class Util {
	public static Location parseLocation(World w, String in) {
		String[] params = in.split(",");
		for (String s : params) {
			s.replace("-0", "0");
		}
		if (params.length == 3 || params.length == 5) {
			double x = Double.parseDouble(params[0]);
			double y = Double.parseDouble(params[1]);
			double z = Double.parseDouble(params[2]);
			Location loc = new Location(w, x, y, z);
			if (params.length == 5) {
				loc.setYaw(Float.parseFloat(params[4]));
				loc.setPitch(Float.parseFloat(params[5]));
			}
			return loc;
		}
		return null;
	}

	public static void sendPlayerToGame(final Player player, Annihilation plugin) {
		final PlayerMeta meta = PlayerMeta.getMeta(player);
		if (meta.getTeam() != null) {
			meta.setAlive(true);
			player.teleport(meta.getTeam().getRandomSpawn());

			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				public void run() {
					meta.getKit().give(player, meta.getTeam());
					player.setCompassTarget(meta.getTeam().getNexus().getLocation());
					player.setGameMode(GameMode.SURVIVAL);
					player.setHealth(player.getMaxHealth());
					player.setFoodLevel(20);
					player.setSaturation(20F);
				}
			}, 10L);
		}
	}

	public static boolean isEmptyColumn(Location loc) {
		boolean hasBlock = false;
		Location test = loc.clone();
		for (int y = 0; y < loc.getWorld().getMaxHeight(); y++) {
			test.setY(y);
			if (test.getBlock().getType() != Material.AIR)
				hasBlock = true;
		}
		return !hasBlock;
	}

	public static void showClassSelector(Player p, String title) {
		int size = ((Kit.values().length + 8) / 9) * 9;
		Inventory inv = Bukkit.createInventory(p, size, title);
		for (Kit kit : Kit.values()) {
			ItemStack i = kit.getIcon().clone();
			ItemMeta im = i.getItemMeta();
			List<String> lore = im.getLore();
			lore.add(ChatColor.GRAY + "---------------");
			if (kit.isOwnedBy(p)) {
				lore.add(ChatColor.GREEN + "Unlocked");
			} else {
				lore.add(ChatColor.RED + "Locked");
			}
			im.setLore(lore);
			i.setItemMeta(im);
			inv.addItem(i);
		}
		p.openInventory(inv);
	}

	public static void spawnFirework(Location loc) {
		Random colour = new Random();

		Firework fw = loc.getWorld().spawn(loc, Firework.class);
		FireworkMeta fwMeta = fw.getFireworkMeta();

		Type fwType = Type.BALL_LARGE;

		int c1i = colour.nextInt(17) + 1;
		int c2i = colour.nextInt(17) + 1;

		Color c1 = getFWColor(c1i);
		Color c2 = getFWColor(c2i);

		FireworkEffect effect = FireworkEffect.builder().withFade(c2).withColor(c1).with(fwType).build();

		fwMeta.addEffect(effect);
		fwMeta.setPower(1);
		fw.setFireworkMeta(fwMeta);
	}

	public static void spawnFirework(Location loc, Color c1, Color c2) {
		Firework fw = loc.getWorld().spawn(loc, Firework.class);
		FireworkMeta fwMeta = fw.getFireworkMeta();

		Type fwType = Type.BALL_LARGE;

		FireworkEffect effect = FireworkEffect.builder().withFade(c2).withColor(c1).with(fwType).build();

		fwMeta.addEffect(effect);
		fwMeta.setPower(1);
		fw.setFireworkMeta(fwMeta);
	}

	public static Color getFWColor(int c) {
		switch (c) {
		case 1:
			return Color.TEAL;
		default:
		case 2:
			return Color.WHITE;
		case 3:
			return Color.YELLOW;
		case 4:
			return Color.AQUA;
		case 5:
			return Color.BLACK;
		case 6:
			return Color.BLUE;
		case 7:
			return Color.FUCHSIA;
		case 8:
			return Color.GRAY;
		case 9:
			return Color.GREEN;
		case 10:
			return Color.LIME;
		case 11:
			return Color.MAROON;
		case 12:
			return Color.NAVY;
		case 13:
			return Color.OLIVE;
		case 14:
			return Color.ORANGE;
		case 15:
			return Color.PURPLE;
		case 16:
			return Color.RED;
		case 17:
			return Color.SILVER;
		}
	}

	public static String getPhaseColor(int phase) {
		switch (phase) {
		case 1:
			return ChatColor.BLUE.toString();
		case 2:
			return ChatColor.GREEN.toString();
		case 3:
			return ChatColor.YELLOW.toString();
		case 4:
			return ChatColor.GOLD.toString();
		case 5:
			return ChatColor.RED.toString();
		default:
			return ChatColor.WHITE.toString();
		}
	}

	public static boolean isTeamTooBig(GameTeam team) {
		int players = team.getPlayers().size();
		for (GameTeam gt : GameTeam.teams()) {
			if (players >= gt.getPlayers().size() + 3 && gt.getNexus().isAlive()) {
				return true;
			}
		}
		return false;
	}
}
