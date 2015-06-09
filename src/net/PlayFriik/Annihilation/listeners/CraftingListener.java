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

import net.PlayFriik.Annihilation.object.Kit;
import net.PlayFriik.Annihilation.object.PlayerMeta;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class CraftingListener implements Listener {
    private ShapedRecipe arrowRecipe;

    public CraftingListener() {
        arrowRecipe = new ShapedRecipe(new ItemStack(Material.ARROW, 3));
        arrowRecipe.shape("F", "S");
        arrowRecipe.setIngredient('F', Material.FLINT);
        arrowRecipe.setIngredient('S', Material.STICK);
        Bukkit.addRecipe(arrowRecipe);
    };

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent e) {
        Player player = (Player) e.getView().getPlayer();
        if (e.getRecipe() instanceof ShapedRecipe) {
            ShapedRecipe recipe = (ShapedRecipe) e.getRecipe();
            if (PlayerMeta.getMeta(player).getKit() != Kit.ARCHER) {
                if (sameRecipe(recipe, arrowRecipe)) {
                    e.getInventory().setResult(null);
                }
            }
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getRecipe() instanceof ShapedRecipe) {
            ShapedRecipe recipe = (ShapedRecipe) e.getRecipe();
            if (PlayerMeta.getMeta(player).getKit() != Kit.ARCHER) {
                if (sameRecipe(recipe, arrowRecipe)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    private boolean sameRecipe(ShapedRecipe r1, ShapedRecipe r2) {
        if (r1 == r2)
            return true;
        if (r1 == null || r2 == null)
            return false;
        if (!r1.getResult().equals(r2.getResult()))
            return false;
        if (Arrays.equals(r1.getShape(), r2.getShape()))
            return true;
        return false;
    }
}
