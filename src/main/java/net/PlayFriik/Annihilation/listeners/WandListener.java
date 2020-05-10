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

import java.util.HashMap;
import java.util.List;

import net.PlayFriik.Annihilation.Annihilation;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BlockIterator;

public class WandListener implements Listener {
    private final Annihilation plugin;
    private static final String prefix = ChatColor.YELLOW.toString()
            + ChatColor.BOLD.toString();
    public static final String apprenticeName = prefix + "Apprentice Wand";
    public static final String masterName = prefix + "Master Wand";

    private static final int RANGE = 10;
    private static final double APPRENTICE_DAMAGE = 10.0;
    private static final int MASTER_FIRE_TICKS = 80;
    private static final long WAND_COOLDOWN = 5L;

    private final HashMap<String, Boolean> apprenticeCooldown = new HashMap<String, Boolean>();
    private final HashMap<String, Boolean> masterCooldown = new HashMap<String, Boolean>();

    public WandListener(Annihilation plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWandUse(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR
                || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player user = e.getPlayer();
            LivingEntity target = getTargetEntity(user);
            if (target == null)
                return;

            ItemStack item = user.getItemInHand();
            if (item == null)
                return;

            if (item.getType() == Material.BLAZE_ROD) {
                ItemMeta meta = item.getItemMeta();
                if (!meta.hasDisplayName())
                    return;
                if (meta.getDisplayName().contains(apprenticeName))
                    useApprenticeWand(user, target);
                if (meta.getDisplayName().contains(masterName))
                    useMasterWand(user, target);
            }
        }
    }

    private LivingEntity getTargetEntity(Player player) {
        List<Entity> nearby = player.getNearbyEntities(RANGE, RANGE, RANGE);
        LivingEntity target = null;
        BlockIterator bit = new BlockIterator(player, RANGE);
        while (bit.hasNext()) {
            Block b = bit.next();
            for (Entity e : nearby)
                if (e instanceof LivingEntity)
                    if (nearBlock(b, (LivingEntity) e))
                        target = (LivingEntity) e;
        }
        return target;
    }

    private boolean nearBlock(Block b, LivingEntity e) {
        Location bLoc = b.getLocation();
        Location eLoc = e.getLocation();
        double bx = bLoc.getX(), by = bLoc.getY(), bz = bLoc.getZ();
        double ex = eLoc.getX(), ey = eLoc.getY(), ez = eLoc.getZ();
        return Math.abs(bx - ex) < 0.5 && by - ey < e.getEyeHeight()
                && by - ey > -0.5 && Math.abs(bz - ez) < 0.5;
    }

    private void useApprenticeWand(Player user, LivingEntity target) {
        if (!apprenticeCooldown.containsKey(user.getName()))
            apprenticeCooldown.put(user.getName(), false);
        if (apprenticeCooldown.get(user.getName()))
            return;
        target.damage(APPRENTICE_DAMAGE, user);
        target.getWorld().playEffect(target.getEyeLocation(), Effect.POTION_BREAK,
                1, 1);
        
        final String name = user.getName();
        apprenticeCooldown.put(name, true);
        plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
            public void run() {
                apprenticeCooldown.put(name, false);
            }
        }, WAND_COOLDOWN * 20L);
    }

    private void useMasterWand(Player user, LivingEntity target) {
        if (!masterCooldown.containsKey(user.getName()))
            masterCooldown.put(user.getName(), false);
        if (masterCooldown.get(user.getName()))
            return;
        target.setFireTicks(MASTER_FIRE_TICKS);
        
        final String name = user.getName();
        masterCooldown.put(name, true);
        plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
            public void run() {
                masterCooldown.put(name, false);
            }
        }, WAND_COOLDOWN * 20L);
    }
}
