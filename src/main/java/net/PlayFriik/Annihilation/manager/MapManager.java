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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.PlayFriik.Annihilation.Annihilation;
import net.PlayFriik.Annihilation.maps.GameMap;
import net.PlayFriik.Annihilation.maps.MapRollback;
import net.PlayFriik.Annihilation.maps.VoidGenerator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.Configuration;

public class MapManager {
	private final ArrayList<String> maps = new ArrayList<String>();
	private GameMap currentMap = null;
	private Location lobbySpawn;
	private MapRollback mapRollback;

	public MapManager(Annihilation plugin, MapRollback mapRollback, Configuration config) {
		this.mapRollback = mapRollback;
		for (String s : config.getKeys(false)) {
			if (!s.equalsIgnoreCase("lobby")) {
				maps.add(s);
			}
		}

		WorldCreator wc = new WorldCreator("lobby");
		wc.generator(new VoidGenerator());
		Bukkit.createWorld(wc);

		lobbySpawn = parseLocation(config.getString("lobby.spawn"));
	}

	private Location parseLocation(String in) {
		String[] params = in.split(",");
		if (params.length == 3 || params.length == 5) {
			double x = Double.parseDouble(params[0]);
			double y = Double.parseDouble(params[1]);
			double z = Double.parseDouble(params[2]);
			Location loc = new Location(Bukkit.getWorld("lobby"), x, y, z);
			if (params.length == 5) {
				loc.setYaw(Float.parseFloat(params[3]));
				loc.setPitch(Float.parseFloat(params[4]));
			}
			return loc;
		}
		return null;
	}

	public boolean selectMap(String mapName) {
		currentMap = new GameMap(mapRollback);
		return currentMap.loadIntoGame(mapName);
	}

	public boolean mapSelected() {
		return currentMap != null;
	}

	public GameMap getCurrentMap() {
		return currentMap;
	}

	public Location getLobbySpawnPoint() {
		return lobbySpawn;
	}

	public List<String> getRandomMaps() {
		LinkedList<String> shuffledMaps = new LinkedList<String>(maps);
		Collections.shuffle(shuffledMaps);
		return shuffledMaps.subList(0, Math.min(3, shuffledMaps.size()));
	}

	public void reset() {
		currentMap = null;
	}
}
