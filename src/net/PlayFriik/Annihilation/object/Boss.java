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
package net.PlayFriik.Annihilation.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class Boss {
	private String configName;
	private int health;
	private String bossName;
	private Location spawn;
	private Location chest;
	private boolean alive;

	private HashMap<ItemStack, Float> legendaries = new HashMap<>();
	private HashMap<ItemStack, Float> loot = new HashMap<>();
	private int lootItems;
	private int ingots;

	public Boss(String configName, int health, String bossName, Location spawn, Location chest) {
		this.configName = configName;
		this.health = health;
		this.bossName = bossName;
		this.spawn = spawn;
		this.chest = chest;
		this.loot.put(new ItemStack(Material.IRON_INGOT,1),(float) 1);
		this.loot.put(new ItemStack(Material.IRON_BOOTS,1),(float) 1);
		this.loot.put(new ItemStack(Material.IRON_CHESTPLATE,1),(float) 1);
		this.loot.put(new ItemStack(Material.COOKED_CHICKEN,1),(float) 1);
		this.loot.put(new ItemStack(Material.COOKED_BEEF,1),(float) 1);
		this.loot.put(new ItemStack(Material.IRON_SWORD,1),(float) 1);
		this.loot.put(new ItemStack(Material.GOLD_NUGGET,1),(float) 1);
		this.loot.put(new ItemStack(Material.IRON_HELMET,1),(float) 1);
		this.loot.put(new ItemStack(Material.TNT,1),(float) 1);
		this.loot.put(new ItemStack(Material.TORCH,1),(float) 1);
		this.loot.put(new ItemStack(Material.IRON_LEGGINGS,1),(float) 1);
		this.loot.put(new ItemStack(Material.DIAMOND,1),(float) 1);
		this.loot.put(new ItemStack(Material.GOLD_INGOT,1),(float) 1);
		this.loot.put(new ItemStack(Material.IRON_PICKAXE,1),(float) 1);
		this.loot.put(new ItemStack(Material.COAL,5),(float) 1);
		this.lootItems = loot.size();
		this.setAlive(false);
	}

	public void LootChest() {
		chest.getBlock().setType(Material.CHEST);
		Chest c = (Chest) chest.getBlock().getState();
		Inventory inv = c.getBlockInventory();
		Random r = new Random();
		inv.setItem(r.nextInt(inv.getSize()), SpawnRandomItem(legendaries));
		if (lootItems > inv.getSize() - 2) {
			lootItems = inv.getSize() - 2;
		}
		for (int i = 0; i < lootItems; i++) {
			int slot = r.nextInt(inv.getSize());
			if (isEmpty(inv, slot)) {
				inv.setItem(slot, SpawnRandomItem(loot));
			} else {
				i--;
			}
		}
		for (int i = 0; i < ingots; i++) {
			int slot = r.nextInt(inv.getSize());
			ItemStack stack = inv.getItem(slot);
			if (isEmpty(inv, slot)) {
				inv.setItem(slot, new ItemStack(Material.IRON_INGOT));
			} else if (stack.getType() == Material.IRON_INGOT) {
				inv.getItem(slot).setAmount(stack.getAmount() + 1);
			} else {
				i--;
			}
		}
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public String getBossName() {
		return bossName;
	}

	public void setBossName(String bossName) {
		this.bossName = bossName;
	}

	public Location getSpawn() {
		return spawn;
	}

	public void setSpawn(Location spawn) {
		this.spawn = spawn;
	}

	public Location getChest() {
		return chest;
	}

	public void setChest(Location chest) {
		this.chest = chest;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	private static ItemStack SpawnRandomItem(HashMap<ItemStack, Float> weighting) {
		List<ItemStack> items = new ArrayList<>(weighting.keySet());

		float totalWeight = 0F;
		for (Float f : weighting.values()) {
			totalWeight += f;
		}

		float rand = new Random().nextFloat() * totalWeight;
		for (int i = 0; i < weighting.size(); i++) {
			ItemStack item = items.get(i);
			rand -= weighting.get(item);
			if (rand <= 0F) {
				return item;
			}
		}
		return null;
	}

	private static boolean isEmpty(Inventory inv, int slot) {
		ItemStack stack = inv.getItem(slot);
		if (stack == null) {
			return true;
		}
		if (stack.getType() == Material.AIR) {
			return true;
		}
		return false;
	}
}