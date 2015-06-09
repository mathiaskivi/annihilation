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
import java.util.HashMap;

import net.PlayFriik.Annihilation.Annihilation;
import net.PlayFriik.Annihilation.Util;
import net.PlayFriik.Annihilation.object.GameTeam;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;

public class SignManager {
    private Annihilation plugin;

    private HashMap<GameTeam, ArrayList<Location>> signs = new HashMap<GameTeam, ArrayList<Location>>();

    public SignManager(Annihilation instance) {
        this.plugin = instance;
    }

    public void loadSigns() {
        ConfigurationSection config = plugin.getConfigManager().getConfig(
                "maps.yml");
        for (GameTeam team : GameTeam.teams()) {
            signs.put(team, new ArrayList<Location>());
            String name = team.name().toLowerCase();
            for (String l : config.getStringList("lobby.signs." + name)) {
                Location loc = Util.parseLocation(
                        Bukkit.getWorld("lobby"), l);
                if (loc != null)
                    addTeamSign(team, loc);
            }
        }
    }

    public void addTeamSign(GameTeam team, Location loc) {
        Block b = loc.getBlock();
        if (b == null)
            return;

        Material m = b.getType();
        if (m == Material.SIGN_POST || m == Material.WALL_SIGN) {
            signs.get(team).add(loc);
            updateSigns(team);
        }
    }

    public void updateSigns(GameTeam t) {
        if (t == GameTeam.NONE)
            return;

        for (Location l : signs.get(t)) {
            Block b = l.getBlock();
            if (b == null)
                return;

            Material m = b.getType();
            if (m == Material.SIGN_POST || m == Material.WALL_SIGN) {
                Sign s = (Sign) b.getState();
                s.setLine(0, ChatColor.DARK_PURPLE + "[Team]");
                s.setLine(1, t.coloredName());
                s.setLine(2, ChatColor.UNDERLINE.toString() + t.getPlayers().size()
                        + (t.getPlayers().size() == 1 ? " Player" : " Players"));
                if (t.getNexus() != null && plugin.getPhase() > 0)
                    s.setLine(3, ChatColor.BOLD.toString() + "Nexus: "
                            + t.getNexus().getHealth());
                else
                    s.setLine(3, " ");
                s.update(true);
            }
        }
    }
}
