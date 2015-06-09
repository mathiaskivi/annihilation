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
package net.PlayFriik.Annihilation.bar;

import java.util.HashMap;

import net.PlayFriik.Annihilation.Annihilation;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

;

public class BarUtil implements Listener {
    private static BarUtil instance;

    private Annihilation plugin;
    private HashMap<String, FakeDragon> players = new HashMap<String, FakeDragon>();

    private BarUtil() {

    }

    public static void init(Annihilation plugin) {
        instance = new BarUtil();
        instance.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(instance, plugin);
    }

    public static void setMessageAndPercent(Player player, String message,
            float percent) {
        FakeDragon dragon = instance.getDragon(player, message);
        if (message.length() > 64)
            dragon.name = message.substring(0, 63);
        else
            dragon.name = message;
        dragon.health = percent * FakeDragon.MAX_HEALTH;
        instance.sendDragon(dragon, player);
    }

    public static void setMessage(Player player, String message) {
        FakeDragon dragon = instance.getDragon(player, message);
        if (message.length() > 64)
            dragon.name = message.substring(0, 63);
        else
            dragon.name = message;
        dragon.health = FakeDragon.MAX_HEALTH;
        instance.sendDragon(dragon, player);
    }

    public static void setPercent(Player player, float percent) {
        if (!instance.hasBar(player))
            return;

        FakeDragon dragon = instance.getDragon(player, "");
        dragon.health = percent * FakeDragon.MAX_HEALTH;
        instance.sendDragon(dragon, player);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void PlayerLogout(PlayerQuitEvent event) {
        quit(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerKick(PlayerKickEvent event) {
        quit(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getTo() != null)
            handleTeleport(event.getPlayer(), event.getTo().clone());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerRespawnEvent event) {
        handleTeleport(event.getPlayer(), event.getRespawnLocation().clone());
    }

    private void quit(Player player) {
        removeBar(player);
    }

    private void handleTeleport(final Player player, final Location loc) {
        if (!hasBar(player))
            return;
        plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
            public void run() {
                FakeDragon oldDragon = getDragon(player, "");
                Object destroyPacket = getDragon(player, "").getDestroyPacket();
                Util.sendPacket(player, destroyPacket);
                players.remove(player.getName());

                FakeDragon dragon = addDragon(player, loc, oldDragon.name);
                dragon.health = oldDragon.health;
                sendDragon(dragon, player);
            }
        }, 2L);
    }

    private boolean hasBar(Player player) {
        return players.get(player.getName()) != null;
    }

    private void removeBar(Player player) {
        if (instance.hasBar(player)) {
            Util.sendPacket(player, instance.getDragon(player, "")
                    .getDestroyPacket());
            instance.players.remove(player.getName());
        }
    }

    private void sendDragon(FakeDragon dragon, Player player) {
        Object metaPacket = dragon.getMetaPacket(dragon.getWatcher());
        Object teleportPacket = dragon.getTeleportPacket(player.getLocation()
                .add(0, -300, 0));

        Util.sendPacket(player, metaPacket);
        Util.sendPacket(player, teleportPacket);
    }

    private FakeDragon getDragon(Player player, String message) {
        if (hasBar(player))
            return players.get(player.getName());
        else
            return addDragon(player, message);
    }

    private FakeDragon addDragon(Player player, String message) {
        FakeDragon dragon = Util.newDragon(message,
                player.getLocation().add(0, -300, 0));

        Util.sendPacket(player, dragon.getSpawnPacket());

        players.put(player.getName(), dragon);

        return dragon;
    }

    private FakeDragon addDragon(Player player, Location loc, String message) {
        FakeDragon dragon = Util.newDragon(message, loc.add(0, -300, 0));

        Util.sendPacket(player, dragon.getSpawnPacket());

        players.put(player.getName(), dragon);

        return dragon;
    }
}
