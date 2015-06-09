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
package net.PlayFriik.Annihilation.object;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.PlayFriik.Annihilation.Annihilation;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Shop implements Listener {
    private static class ShopItem {
        private ItemStack item;
        private int price;

        public ShopItem(Material type, int qty, int price) {
            item = new ItemStack(type);
            this.price = price;
            item.setAmount(qty);
        }

        public ShopItem setName(String name) {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.WHITE + name);
            item.setItemMeta(meta);
            return this;
        }

        public ItemStack getShopStack() {
            ItemStack stack = item.clone();
            String priceStr = ChatColor.GOLD.toString() + price + " Gold";
            ItemMeta meta = stack.getItemMeta();
            if (meta.hasLore())
                meta.getLore().add(priceStr);
            else
                meta.setLore(Arrays.asList(priceStr));
            stack.setItemMeta(meta);
            return stack;
        }

        public ItemStack getItemStack() {
            return item;
        }

        public int getPrice() {
            return price;
        }

        public String getName() {
            String name;
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName()) {
                name = meta.getDisplayName();
            } else {
                name = item.getType().name();
                name = name.replace("_", " ").toLowerCase();
                name = WordUtils.capitalize(name);
                name += ChatColor.WHITE; // In case it's a wand
            }
            if (item.getAmount() > 1)
                name = item.getAmount() + " " + name;

            return name;
        }
    }

    private String name;
    private ArrayList<ShopItem> items;

    public Shop(Annihilation plugin, String name, Configuration config) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.name = name;
        loadConfig(config);
    }

    @EventHandler
    public void onSignClick(PlayerInteractEvent e) {
        if (e.getClickedBlock() != null) {
            Material type = e.getClickedBlock().getType();
            if (type == Material.WALL_SIGN || type == Material.SIGN_POST) {
                Sign sign = (Sign) e.getClickedBlock().getState();
                String line0 = sign.getLine(0);
                String line1 = sign.getLine(1);
                if (line0.equals(ChatColor.DARK_PURPLE + "[Shop]")
                        && line1.equals(name)) {
                    openShop(e.getPlayer());
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onShopInventoryClick(InventoryClickEvent e) {
        Player buyer = (Player) e.getWhoClicked();
        if (e.getInventory().getName().equals(name + " Shop")) {
            int slot = e.getRawSlot();
            if (slot < e.getInventory().getSize() && slot >= 0) {
                if (slot < items.size() && items.get(slot) != null) {
                    sellItem(buyer, items.get(slot));
                }
                e.setCancelled(true);
            }
            buyer.updateInventory();
        }
    }

    private void openShop(Player player) {
        int size = 9 * (int) Math.ceil(items.size() / 9.0);
        Inventory shopInv = Bukkit.getServer().createInventory(player, size,
                name + " Shop");
        for (int i = 0; i < items.size(); i++) {
            ShopItem item = items.get(i);
            if (item != null)
                shopInv.setItem(i, item.getShopStack());
            else
                shopInv.setItem(i, null);
        }
        player.openInventory(shopInv);
    }

    private void sellItem(Player buyer, ShopItem item) {
        Inventory buyerInv = buyer.getInventory();
        ItemStack stackToGive = item.getItemStack();
        int price = item.getPrice();

        String stackName = ChatColor.WHITE + item.getName();

        if (buyerInv.contains(Material.GOLD_INGOT, price)) {
            buyerInv.removeItem(new ItemStack(Material.GOLD_INGOT, price));
            buyerInv.addItem(stackToGive);
            buyer.sendMessage(ChatColor.GREEN + "Purchased " + stackName);
        } else {
            buyer.sendMessage(ChatColor.RED + "Insufficient gold to purchase "
                    + stackName);
        }
    }

    private void loadConfig(Configuration config) {
        items = new ArrayList<ShopItem>();

        List<String> list = config.getStringList(name.toLowerCase());
        for (String entry : list) {
            if (entry.equalsIgnoreCase("nextline")) {
                int end = 9 * (int) Math.ceil(items.size() / 9.0);
                for (int i = items.size(); i < end; i++)
                    items.add(null);
            } else {
                String[] params = entry.split(",");
                if (params.length >= 3) {
                    Material type = Material.getMaterial(params[0]);
                    int qty = Integer.valueOf(params[1]);
                    int price = Integer.valueOf(params[2]);
                    ShopItem item = new ShopItem(type, qty, price);
                    if (params.length >= 4) {
                        String itemName = params[3].replace("\"", "");
                        // Longest method name ever. Great job bukkit team.
                        item.setName(ChatColor.translateAlternateColorCodes(
                                '&', itemName));
                    }
                    items.add(item);
                }
            }
        }
    }
}
