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
package net.PlayFriik.Annihilation.listeners;

import java.util.HashMap;

import net.PlayFriik.Annihilation.object.GameTeam;
import net.PlayFriik.Annihilation.object.PlayerMeta;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class EnderChestListener implements Listener {
	private HashMap<GameTeam, Location> enderchests = new HashMap<GameTeam, Location>();
	private HashMap<String, Inventory> inventories = new HashMap<String, Inventory>();

	@EventHandler
	public void onChestOpen(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}

		Block b = e.getClickedBlock();
		if (b.getType() != Material.ENDER_CHEST) {
			return;
		}

		Location loc = b.getLocation();
		Player player = e.getPlayer();
		GameTeam team = PlayerMeta.getMeta(player).getTeam();
		if (team == null || !enderchests.containsKey(team)) {
			return;
		}

		if (enderchests.get(team).equals(loc)) {
			e.setCancelled(true);
			openEnderChest(player);
		} else if (enderchests.containsValue(loc)) {
			e.setCancelled(true);
		}
	}

	public void setEnderChestLocation(GameTeam team, Location loc) {
		enderchests.put(team, loc);
	}

	private void openEnderChest(Player player) {
		String name = player.getName();
		if (!inventories.containsKey(name)) {
			Inventory inv = Bukkit.createInventory(null, 9, "Ender Chest");
			inventories.put(name, inv);
		}
		player.openInventory(inventories.get(name));
	}

	@EventHandler
	public void onEnderChestBreak(BlockBreakEvent e) {
		if (enderchests.values().contains(e.getBlock().getLocation())) {
			e.setCancelled(true);
		}
	}
}