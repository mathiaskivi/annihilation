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

import java.util.HashSet;
import java.util.Set;

import net.PlayFriik.Annihilation.Util;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class WorldListener implements Listener {
    private static final Set<EntityType> hostileEntityTypes = new HashSet<EntityType>() {
        private static final long serialVersionUID = 42L;
        {
            add(EntityType.SKELETON);
            add(EntityType.CREEPER);
            add(EntityType.ZOMBIE);
            add(EntityType.SPIDER);
            add(EntityType.BAT);
            add(EntityType.ENDERMAN);
            add(EntityType.SLIME);
            add(EntityType.WITCH);
        }
    };

    @EventHandler
    public void onWaterFlow(BlockFromToEvent e) {
        if (Util.isEmptyColumn(e.getToBlock().getLocation()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent e) {
        if (isHostile(e.getEntityType()))
            e.setCancelled(true);
    }

    private boolean isHostile(EntityType entityType) {
        return hostileEntityTypes.contains(entityType);
    }
}
