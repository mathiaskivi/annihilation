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

import java.util.HashMap;

import net.PlayFriik.Annihilation.Annihilation;
import net.PlayFriik.Annihilation.Util;
import net.PlayFriik.Annihilation.object.Boss;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;

public class BossManager {
    public HashMap<String, Boss> bosses = new HashMap<String, Boss>();
    public HashMap<String, Boss> bossNames = new HashMap<String, Boss>();

    private Annihilation plugin;

    public BossManager(Annihilation instance) {
        this.plugin = instance;
    }

    public void loadBosses(HashMap<String, Boss> b) {
        bosses = b;
    }

    public void spawnBosses() {
        for (Boss b : bosses.values())
            spawn(b);
    }

    @SuppressWarnings("deprecation")
    public void spawn(Boss b) {
        Location spawn = b.getSpawn();

        if (spawn != null && spawn.getWorld() != null) {
            IronGolem boss = (IronGolem) spawn.getWorld().spawnCreature(spawn,
                    EntityType.IRON_GOLEM);
            boss.setMaxHealth(b.getHealth());
            boss.setHealth(b.getHealth());
            boss.setCanPickupItems(false);
            boss.setPlayerCreated(false);
            boss.setRemoveWhenFarAway(false);
            boss.setCustomNameVisible(true);
            boss.setCustomName(ChatColor.translateAlternateColorCodes('&',
                    b.getBossName() + " &8» &a" + (int) b.getHealth() + " HP"));
            bossNames.put(boss.getCustomName(), b);
            Util.spawnFirework(b.getSpawn());
            Util.spawnFirework(b.getSpawn());
            Util.spawnFirework(b.getSpawn());
        }
    }

    public void update(Boss boss, IronGolem g) {
        boss.setHealth((int) g.getHealth());
        g.setCustomName(ChatColor.translateAlternateColorCodes('&',
                boss.getBossName() + " &8» &a" + (int) boss.getHealth() + " HP"));
        bossNames.put(g.getCustomName(), boss);
        bosses.put(boss.getConfigName(), boss);
    }

    public Boss newBoss(Boss b) {
        String boss = b.getConfigName();
        bosses.remove(boss);
        bossNames.remove(boss);

        FileConfiguration config = plugin.getConfigManager().getConfig(
                "maps.yml");
        ConfigurationSection section = config.getConfigurationSection(plugin
                .getMapManager().getCurrentMap().getName());
        ConfigurationSection sec = section.getConfigurationSection("bosses");

        Boss bb = new Boss(boss, sec.getInt(boss + ".hearts") * 2,
                sec.getString(boss + ".name"), Util.parseLocation(plugin
                        .getMapManager().getCurrentMap().getWorld(),
                        sec.getString(boss + ".spawn")), Util.parseLocation(
                        plugin.getMapManager().getCurrentMap().getWorld(),
                        sec.getString(boss + ".chest")));
        bosses.put(boss, bb);

        return bb;
    }
}
