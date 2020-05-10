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

import java.util.Iterator;

import net.PlayFriik.Annihilation.ActionAPI;
import net.PlayFriik.Annihilation.Annihilation;
import net.PlayFriik.Annihilation.Util;
import net.PlayFriik.Annihilation.object.GameTeam;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class RestartHandler {
	private final Annihilation plugin;
	private long time;
	private long delay;
	private int taskID;
	private int fwID;

	public RestartHandler(Annihilation plugin, final long delay) {
		this.plugin = plugin;
		this.delay = delay;
	}

	public void start(final long gameTime, final Color c) {
		for (Iterator<Entity> iterator = plugin.getMapManager().getCurrentMap().getWorld().getEntities().iterator(); iterator.hasNext();) {
			Entity entity = iterator.next();
			if (entity.getType() == EntityType.IRON_GOLEM) {
				entity.remove();
			}
		}

		time = delay;
		final String totalTime = PhaseManager.timeString(gameTime);
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin,
				new Runnable() {
			public void run() {
				if (time <= 0) {
					stop();
					return;
				}

				String message = ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "Total time: " + totalTime + " - " + "Restarting in: " + time;
				for (Player p : Bukkit.getOnlinePlayers()) {
					ActionAPI.sendAnnouncement(p, message);
				}
				time--;
			}
		}, 0L, 20L);

		fwID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				for (GameTeam gt : GameTeam.values()) {
					if (gt != GameTeam.NONE) {
						for (Location l : gt.getSpawns()) {
							Util.spawnFirework(l, c, c);
						}
					}
				}
			}
		}, 0L, 40L);
	}

	private void stop() {
		Bukkit.getScheduler().cancelTask(taskID);
		Bukkit.getScheduler().cancelTask(fwID);

		if (plugin.runCommand) {
			for (String c : plugin.commands) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), c);
			}
		} else {
			plugin.reset();
		}
	}
}
