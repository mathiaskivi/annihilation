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
package net.PlayFriik.Annihilation;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHooks {
	public static boolean vault = false;
	private static VaultHooks inst;

	public static VaultHooks instance() {
		if (vault) {
			if (inst == null) {
				inst = new VaultHooks();
			}
			return inst;
		} else {
			return null;
		}
	}

	public static Permission permission;
	public static Chat chat;

	public static Permission getPermissionManager() {
		return permission;
	}

	public static Chat getChatManager() {
		return chat;
	}

	public boolean setupPermissions() {
		if (!vault) {
			return false;
		}

		RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
		return (permission != null);
	}

	public boolean setupChat() {
		if (!vault) {
			return false;
		}

		RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
		if (chatProvider != null) {
			chat = chatProvider.getProvider();
		}

		return (chat != null);
	}

	@SuppressWarnings("deprecation")
	public static String getGroup(String name) {
		if (!vault) {
			return "";
		}

		String prefix = VaultHooks.getChatManager().getPlayerPrefix(Bukkit.getPlayer(name));
		String group = VaultHooks.getPermissionManager().getPrimaryGroup(Bukkit.getPlayer(name));

		if (prefix == null || prefix == "") {
			prefix = VaultHooks.getChatManager().getGroupPrefix(Bukkit.getPlayer(name).getWorld(), group);
		}

		return ChatColor.translateAlternateColorCodes('&', prefix);
	}
}
