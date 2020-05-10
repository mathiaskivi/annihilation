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

import java.util.HashMap;

import org.bukkit.entity.Player;

public class PlayerMeta {
    private static HashMap<String, PlayerMeta> metaTable = new HashMap<String, PlayerMeta>();

    public static PlayerMeta getMeta(Player player) {
        return getMeta(player.getName());
    }

    public static PlayerMeta getMeta(String username) {
        if (!metaTable.containsKey(username))
            metaTable.put(username, new PlayerMeta());
        return metaTable.get(username);
    }
    
    public static void reset() {
        metaTable.clear();
    }

    private GameTeam team;
    private Kit kit;
    private boolean alive;

    public PlayerMeta() {
        team = GameTeam.NONE;
        kit = Kit.CIVILIAN;
        alive = false;
    }

    public void setTeam(GameTeam t) {
        if (team != null)
            team = t;
        else
            team = GameTeam.NONE;
    }

    public GameTeam getTeam() {
        return team;
    }
    
    public void setKit(Kit k) {
        if (k != null)
            kit = k;
        else
            kit = Kit.CIVILIAN;
    }
    
    public Kit getKit() {
        return kit;
    }

    public void setAlive(boolean b) {
        alive = b;
    }

    public boolean isAlive() {
        return alive;
    }
}
