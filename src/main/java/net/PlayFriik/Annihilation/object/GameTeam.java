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
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public enum GameTeam {
    RED, YELLOW, GREEN, BLUE, NONE;

    private final ChatColor color;
    private List<Location> spawns;
    private Nexus nexus;

    GameTeam() {
        if (name().equals("NONE"))
            color = ChatColor.WHITE;
        else
            color = ChatColor.valueOf(name());

        spawns = new ArrayList<Location>();
    }

    @Override
    public String toString() {
        return name().substring(0, 1) + name().substring(1).toLowerCase();
    }

    public String coloredName() {
        return color().toString() + toString();
    }

    public ChatColor color() {
        return color;
    }

    public Nexus getNexus() {
        if (this != NONE)
            return nexus;
        else
            return null;
    }

    public void loadNexus(Location loc, int health) {
        if (this != NONE)
            nexus = new Nexus(this, loc, health);
    }

    public void addSpawn(Location loc) {
        if (this != NONE)
            spawns.add(loc);
    }

    public Location getRandomSpawn() {
        if (!spawns.isEmpty() && this != NONE)
            return spawns.get(new Random().nextInt(spawns.size()));
        return null;
    }

    public List<Location> getSpawns() {
        return spawns;
    }

    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<Player>();
        for (Player p : Bukkit.getOnlinePlayers())
            if (PlayerMeta.getMeta(p).getTeam() == this && this != NONE)
                players.add(p);
        return players;
    }

    public static GameTeam[] teams() {
        return new GameTeam[] { RED, YELLOW, GREEN, BLUE };
    }

    public Color getColor(GameTeam gt) {
        if (gt == GameTeam.RED) return Color.RED;
        if (gt == GameTeam.BLUE) return Color.BLUE;
        if (gt == GameTeam.GREEN) return Color.LIME;
        if (gt == GameTeam.YELLOW) return Color.YELLOW;

        return null;
    }
}
