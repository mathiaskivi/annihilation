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
package net.PlayFriik.Annihilation.chat;

import net.PlayFriik.Annihilation.Annihilation;
import net.PlayFriik.Annihilation.object.GameTeam;
import net.PlayFriik.Annihilation.object.PlayerMeta;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    private final Annihilation plugin;

    public ChatListener(Annihilation plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(final AsyncPlayerChatEvent e) {
        Player sender = e.getPlayer();
        PlayerMeta meta = PlayerMeta.getMeta(sender);
        GameTeam team = meta.getTeam();
        boolean isAll = false;
        boolean dead = !meta.isAlive() && plugin.getPhase() > 0;
        String msg = e.getMessage();

        if (e.getMessage().startsWith("!") && !e.getMessage().equalsIgnoreCase("!")) {
            isAll = true;
            msg = msg.substring(1);
        }

        if (team == GameTeam.NONE)
            isAll = true;

        if (isAll)
            ChatUtil.allMessage(team, sender, msg, dead);
        else
            ChatUtil.teamMessage(team, sender, msg, dead);

        e.setCancelled(true);
    }
}
