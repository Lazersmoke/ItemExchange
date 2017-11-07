package com.devotedmc.itemexchange.gui;

import org.bukkit.entity.Player;

import com.devotedmc.itemexchange.exchanges.Shop;

import vg.civcraft.mc.civmodcore.inventorygui.ClickableInventory;

public class ExchangeMenu {
	public static void ShowShop(Player player, Shop shop) {
		ClickableInventory menu = new ClickableInventory(18, shop.getName());
		/*ItemStack testStack = new ItemStack(Material.BARRIER);
		ISUtils.setName(testStack, ChatColor.LIGHT_PURPLE + "This is a test");
		ISUtils.addLore(testStack, ChatColor.RED + "1");
		Clickable testclickable = new Clickable(testStack) {
			public void clicked(Player player) {
				player.sendMessage(ChatColor.AQUA + "Well done!");
			}
		};
		menu.setSlot(testclickable, 4);
		ItemStack testStack2 = new ItemStack(Material.SAPLING);
		ISUtils.setName(testStack2, ChatColor.LIGHT_PURPLE + "This is a test");
		ISUtils.addLore(testStack2, ChatColor.RED + "2");
		Clickable testclickable2 = new Clickable(testStack2) {
			public void clicked(Player player) {
				player.sendMessage(ChatColor.GREEN + "Well done! Goodbye!");
				ClickableInventory.forceCloseInventory(player);
			}
		};
		menu.setSlot(testclickable2, 5);*/
		menu.showInventory(player);
	}
	
	public static void ShowShopModify(Player player, Shop shop) {
		ClickableInventory menu = new ClickableInventory(18, shop.getName());
		
		menu.showInventory(player);
	}
	
	public static void ShowSelectExchangeModify(Player player, Shop shop) {
		ClickableInventory menu = new ClickableInventory(18, shop.getName());
		
		menu.showInventory(player);
	}
}
