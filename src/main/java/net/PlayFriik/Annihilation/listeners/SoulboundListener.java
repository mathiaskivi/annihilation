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

import java.util.Arrays;
import java.util.Iterator;

import net.PlayFriik.Annihilation.manager.SoundManager;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SoulboundListener implements Listener {
    private static final String soulboundTag = ChatColor.GOLD + "Soulbound";

    @EventHandler
    public void onSoulboundDrop(PlayerDropItemEvent e) {
        if (isSoulbound(e.getItemDrop().getItemStack())) {
            Player p = e.getPlayer();
            SoundManager.playSoundForPlayer(p, Sound.BLAZE_HIT, 1F, 0.25F, 0.5F);
            e.getItemDrop().remove();
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Iterator<ItemStack> it = e.getDrops().iterator();
        while (it.hasNext()) {
            if (isSoulbound(it.next()))
                it.remove();
        }
    }

    public static boolean isSoulbound(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (item.hasItemMeta())
            if (meta.hasLore())
                if (meta.getLore().contains(soulboundTag))
                    return true;
        return false;
    }
    
    public static void soulbind(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        if (!meta.hasLore())
            meta.setLore(Arrays.asList(soulboundTag));
        else
            meta.getLore().add(soulboundTag);
        stack.setItemMeta(meta);
    }
}
