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

import net.PlayFriik.Annihilation.Annihilation;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AnnihilationCommand implements CommandExecutor {
	private Annihilation plugin;

	public AnnihilationCommand(Annihilation plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.WHITE + "Annihilation v" + plugin.getDescription().getVersion());
			sender.sendMessage(ChatColor.GRAY + "Commands:");
			sender.sendMessage(ChatColor.GRAY + "/anni " + ChatColor.DARK_GRAY + "-" + ChatColor.WHITE + " Show all available commands.");
			sender.sendMessage(ChatColor.GRAY + "/anni start " + ChatColor.DARK_GRAY + "-" + ChatColor.WHITE + " Start the game.");
		}

		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("start")) {
				if (sender.hasPermission("annihilation.command.start")) {
					if (!plugin.startTimer()) {
						sender.sendMessage(ChatColor.RED + "The game has already started.");
					} else {
						sender.sendMessage(ChatColor.GREEN + "The game has been started.");
					}
				} else sender.sendMessage(ChatColor.RED + "You cannot use this command!");
			}
		}
		return false;
	}
}