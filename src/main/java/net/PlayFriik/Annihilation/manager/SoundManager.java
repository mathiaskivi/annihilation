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
package net.PlayFriik.Annihilation.manager;

import java.util.Random;

import net.PlayFriik.Annihilation.object.GameTeam;
import net.PlayFriik.Annihilation.object.PlayerMeta;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundManager {
	private static Random rand = new Random();

	public static void playSound(Location loc, Sound sound, float volume, float minPitch, float maxPitch) {
		loc.getWorld().playSound(loc, sound, volume, randomPitch(minPitch, maxPitch));
	}

	public static void playSoundForPlayer(Player p, Sound sound, float volume, float minPitch, float maxPitch) {
		p.playSound(p.getLocation(), sound, volume, randomPitch(minPitch, maxPitch));
	}

	public static void playSoundForTeam(GameTeam team, Sound sound, float volume, float minPitch, float maxPitch) {
		for (Player p : Bukkit.getOnlinePlayers())
			if (PlayerMeta.getMeta(p).getTeam() == team)
				playSoundForPlayer(p, sound, volume, minPitch, maxPitch);
	}

	private static float randomPitch(float min, float max) {
		return min + rand.nextFloat() * (max - min);
	}
}