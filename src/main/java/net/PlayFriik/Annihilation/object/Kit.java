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
import java.util.List;

import net.PlayFriik.Annihilation.listeners.SoulboundListener;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public enum Kit {
    CIVILIAN(Material.WORKBENCH) {
        {
            spawnItems.add(new ItemStack(Material.WOOD_SWORD));
            spawnItems.add(new ItemStack(Material.WOOD_PICKAXE));
            spawnItems.add(new ItemStack(Material.WOOD_AXE));
            spawnItems.add(new ItemStack(Material.WORKBENCH));
            lore.add("You are the backbone.");
            lore.add("");
            lore.add("Fuel all facets of the");
            lore.add("war machine with your");
            lore.add("set of wooden tools and");
            lore.add("prepare for battle!");
        }
    },
    WARRIOR(Material.STONE_SWORD) {
        {
            spawnItems.add(new ItemStack(Material.STONE_SWORD));
            spawnItems.add(new ItemStack(Material.WOOD_PICKAXE));
            spawnItems.add(new ItemStack(Material.WOOD_AXE));
            spawnItems.add(new Potion(PotionType.INSTANT_HEAL, 1)
                    .toItemStack(1));
            spawnItems.get(0).addEnchantment(Enchantment.KNOCKBACK, 1);
            lore.add("You are the sword.");
            lore.add("");
            lore.add("You deal +1 damage with");
            lore.add("any melee weapon.");
            lore.add("");
            lore.add("Spawn with a knockback");
            lore.add("sword and a health potion");
            lore.add("which enable you to move");
            lore.add("immediately on the enemy");
            lore.add("and attack!");
        }
    },
    ARCHER(Material.BOW) {
        {
            spawnItems.add(new ItemStack(Material.WOOD_SWORD));
            spawnItems.add(new ItemStack(Material.BOW));
            spawnItems.add(new ItemStack(Material.WOOD_PICKAXE));
            spawnItems.add(new ItemStack(Material.WOOD_AXE));
            spawnItems.add(new ItemStack(Material.WOOD_SPADE));
            spawnItems.add(new Potion(PotionType.INSTANT_HEAL, 1)
                    .toItemStack(1));
            spawnItems.add(new ItemStack(Material.ARROW, 16));
            spawnItems.get(1).addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
            lore.add("You are the rain.");
            lore.add("");
            lore.add("You deal +1 damage with");
            lore.add("a bow and can craft arrows");
            lore.add("without feathers.");
            lore.add("");
            lore.add("Place flint on top of sticks");
            lore.add("in your crafting interface");
            lore.add("to create 3 arrows!");
        }
    },

    MINER(Material.STONE_PICKAXE) {
        {
            spawnItems.add(new ItemStack(Material.WOOD_SWORD));
            spawnItems.add(new ItemStack(Material.STONE_PICKAXE));
            spawnItems.add(new ItemStack(Material.WOOD_AXE));
            spawnItems.add(new ItemStack(Material.FURNACE));
            spawnItems.add(new ItemStack(Material.COAL, 4));
            spawnItems.get(1).addEnchantment(Enchantment.DIG_SPEED, 1);
            lore.add("You are the hands.");
            lore.add("");
            lore.add("Spawn with an efficiency");
            lore.add("stone pickaxe and mine");
            lore.add(")precious resources for");
            lore.add("your team more quickly with");
            lore.add("your doubled ore drops!");
        }
    },
    LUMBERJACK(Material.STONE_AXE) {
        {
            spawnItems.add(new ItemStack(Material.WOOD_SWORD));
            spawnItems.add(new ItemStack(Material.WOOD_PICKAXE));
            spawnItems.add(new ItemStack(Material.STONE_AXE));
            spawnItems.get(2).addEnchantment(Enchantment.DIG_SPEED, 1);
            lore.add("You are the wedge.");
            lore.add("");
            lore.add("Spawn with an efficiency");
            lore.add("stone axe and use your");
            lore.add("doubled log drops to obtain");
            lore.add("large amounts of wood for");
            lore.add("your team's builders!");
        }
    },
    SCOUT(Material.FISHING_ROD) {
        {
            spawnItems.add(new ItemStack(Material.GOLD_SWORD));
            spawnItems.add(new ItemStack(Material.FISHING_ROD));
            spawnItems.add(new ItemStack(Material.WOOD_PICKAXE));
            spawnItems.add(new ItemStack(Material.WOOD_AXE));
            ItemMeta meta = spawnItems.get(1).getItemMeta();
            meta.setDisplayName("Grapple");
            spawnItems.get(1).setItemMeta(meta);
            lore.add("You are the feet.");
            lore.add("");
            lore.add("Use your permanent speed");
            lore.add("boost to maneuver around");
            lore.add("the battlefield and your");
            lore.add("grapple to climb obstacles");
            lore.add("and enemy defenses!");
            lore.add("");
            lore.add("You can only wear light");
            lore.add("armor; anything heavier");
            lore.add("than chainmail will slow");
            lore.add("you down.");
        }
    },
    BERSERKER(Material.CHAINMAIL_CHESTPLATE) {
        {
            spawnItems.add(new ItemStack(Material.STONE_SWORD));
            spawnItems.add(new ItemStack(Material.WOOD_PICKAXE));
            spawnItems.add(new ItemStack(Material.WOOD_AXE));
            spawnItems.add(new Potion(PotionType.INSTANT_HEAL, 1)
                    .toItemStack(1));
            lore.add("You are the tank.");
            lore.add("");
            lore.add("Start each life with only");
            lore.add("7 hearts. For every enemy");
            lore.add("kill, you garner yourself");
            lore.add("an extra heart, allowing");
            lore.add("you to gain up to 15 hearts!");
        }
    },
    OPERATIVE(Material.SOUL_SAND) {
        {
            spawnItems.add(new ItemStack(Material.WOOD_SWORD));
            spawnItems.add(new ItemStack(Material.WOOD_PICKAXE));
            spawnItems.add(new ItemStack(Material.WOOD_AXE));
            spawnItems.add(new ItemStack(Material.SOUL_SAND));
            ItemMeta meta = spawnItems.get(3).getItemMeta();
            meta.setDisplayName(ChatColor.AQUA + "Return Point");
            spawnItems.get(3).setItemMeta(meta);
            lore.add("You are the <something>");
            lore.add("");
            lore.add("Carry out your plans for");
            lore.add("offense and safely escape!");
            lore.add("");
            lore.add("You spawn with a block of");
            lore.add("soulsand, which when placed");
            lore.add("will set a return point. In");
            lore.add("90 seconds, you will be");
            lore.add("teleported back. Make sure");
            lore.add("to place it safe from enemies!");
        }
    };

    static {
        for (Kit kit : values())
            kit.init();
    }

    private ItemStack icon;
    List<String> lore = new ArrayList<String>();
    List<ItemStack> spawnItems = new ArrayList<ItemStack>();
    ItemStack[] spawnArmor = new ItemStack[] {
            new ItemStack(Material.LEATHER_BOOTS),
            new ItemStack(Material.LEATHER_LEGGINGS),
            new ItemStack(Material.LEATHER_CHESTPLATE),
            new ItemStack(Material.LEATHER_HELMET) };

    Kit(Material m) {
        icon = new ItemStack(m);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName(getName());
        icon.setItemMeta(meta);
    }

    private void init() {
        for (int i = 0; i < lore.size(); i++) {
            String s = lore.get(i);
            s = ChatColor.AQUA + s;
            lore.set(i, s);
        }
        ItemMeta meta = icon.getItemMeta();
        meta.setLore(lore);
        icon.setItemMeta(meta);
    }

    public static Kit getKit(String name) {
        for (Kit type : values()) {
            if (type.name().equalsIgnoreCase(name))
                return type;
        }
        return null;
    }

    public void give(Player recipient, GameTeam team) {
        PlayerInventory inv = recipient.getInventory();
        inv.clear();

        for (ItemStack item : spawnItems) {
            ItemStack toGive = item.clone();
            SoulboundListener.soulbind(toGive);
            inv.addItem(toGive);
        }

        recipient.removePotionEffect(PotionEffectType.SPEED);

        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta compassMeta = compass.getItemMeta();
        compassMeta.setDisplayName(team.color() + "Pointing to "
                + team.toString() + " Nexus");
        compass.setItemMeta(compassMeta);
        SoulboundListener.soulbind(compass);

        inv.addItem(compass);
        recipient.setCompassTarget(team.getNexus().getLocation());

        inv.setArmorContents(spawnArmor);
        colorizeArmor(inv, getTeamColor(team));

        for (ItemStack armor : inv.getArmorContents())
            SoulboundListener.soulbind(armor);

        if (this == SCOUT)
            addScoutParticles(recipient);

        if (this == BERSERKER)
            recipient.setMaxHealth(14.0);
        else
            recipient.setMaxHealth(20.0);
    }

    private Color getTeamColor(GameTeam team) {
        switch (team) {
        case RED:
            return Color.RED;
        case YELLOW:
            return Color.YELLOW;
        case GREEN:
            return Color.GREEN;
        case BLUE:
            return Color.BLUE;
        default:
            return Color.WHITE;
        }
    }

    private void colorizeArmor(PlayerInventory inv, Color color) {
        for (ItemStack item : inv.getArmorContents()) {
            if (item.getItemMeta() instanceof LeatherArmorMeta) {
                LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
                meta.setColor(color);
                item.setItemMeta(meta);
            }
        }
    }

    public String getName() {
        return name().substring(0, 1) + name().substring(1).toLowerCase();
    }

    public boolean isOwnedBy(Player p) {
        return p.isOp()
                || this == CIVILIAN
                || p.hasPermission("annihilation.class."
                        + getName().toLowerCase());
    }

    public void addScoutParticles(Player p) {
        if (this != SCOUT)
            return;
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
                Integer.MAX_VALUE, 0, true), true);
    }

    public ItemStack getIcon() {
        return icon;
    }
}
