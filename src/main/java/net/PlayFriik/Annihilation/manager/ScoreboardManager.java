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

import net.PlayFriik.Annihilation.object.GameTeam;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardManager {
	public Scoreboard sb;
	public Objective obj;

	public HashMap<String, Score> scores = new HashMap<String, Score>();
	public HashMap<String, Team> teams = new HashMap<String, Team>();

	public void update() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.setScoreboard(sb);
		}
	}

	public void resetScoreboard(String objName) {
		sb = null;
		obj = null;

		scores.clear();
		teams.clear();

		for (Player p : Bukkit.getOnlinePlayers()) {
			p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		}

		sb = Bukkit.getScoreboardManager().getNewScoreboard();
		obj = sb.registerNewObjective("anni", "dummy");

		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(objName);

		setTeam(GameTeam.RED);
		setTeam(GameTeam.BLUE);
		setTeam(GameTeam.GREEN);
		setTeam(GameTeam.YELLOW);
	}

	public void setTeam(GameTeam t) {
		teams.put(t.name(), sb.registerNewTeam(t.name()));
		Team sbt = teams.get(t.name());
		sbt.setAllowFriendlyFire(false);
		sbt.setCanSeeFriendlyInvisibles(true);
		sbt.setPrefix(t.color().toString());
	}
}
