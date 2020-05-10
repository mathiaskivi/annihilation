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

import org.bukkit.Location;
import org.bukkit.Material;

public class Nexus {
    private final GameTeam team;
    private final Location location;
    private int health;

    public Nexus(GameTeam team, Location location, int health) {
        this.team = team;
        this.location = location;
        this.health = health;

        location.getBlock().setType(Material.ENDER_STONE);
    }

    public GameTeam getTeam() {
        return team;
    }

    public Location getLocation() {
        return location;
    }

    public int getHealth() {
        return health;
    }

    public void damage(int amount) {
        health -= amount;
        if (health <= 0) {
            health = 0;
            location.getBlock().setType(Material.BEDROCK);
        }
    }

    public boolean isAlive() {
        return health > 0;
    }
}
