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
package net.PlayFriik.Annihilation.commands;

import static net.PlayFriik.Annihilation.Translation._;
import net.PlayFriik.Annihilation.manager.VotingManager;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class VoteCommand implements CommandExecutor {
	private final VotingManager votingManager;

	public VoteCommand(VotingManager votingManager) {
		this.votingManager = votingManager;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!votingManager.isRunning()) {
			sender.sendMessage(ChatColor.RED + _("INFO_COMMAND_VOTING_ENDED"));
		} else if (args.length == 0) {
			listMaps(sender);
		} else if (!votingManager.vote(sender, args[0])) {
			sender.sendMessage(ChatColor.RED + _("INFO_COMMAND_VOTING_INVALID"));
			listMaps(sender);
		}
		return true;
	}

	private void listMaps(CommandSender sender) {
		sender.sendMessage(ChatColor.DARK_AQUA + _("INFO_COMMAND_VOTING_MAPS"));
		int count = 0;
		for (String map : votingManager.getMaps().values()) {
			count ++;
			sender.sendMessage(ChatColor.DARK_GRAY + " - " + ChatColor.AQUA + "[" + count + "] " + ChatColor.GRAY + map);
		}
	}
}