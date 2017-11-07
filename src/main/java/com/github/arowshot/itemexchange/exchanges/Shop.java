package com.github.arowshot.itemexchange.exchanges;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.arowshot.itemexchange.exchangerules.ExchangeRule;
import com.github.arowshot.itemexchange.util.GsonUtil;

import net.md_5.bungee.api.ChatColor;
import vg.civcraft.mc.civmodcore.inventorygui.Clickable;
import vg.civcraft.mc.civmodcore.inventorygui.ClickableInventory;
import vg.civcraft.mc.civmodcore.itemHandling.ISUtils;

public class Shop {
	private static String lorePrefix = "ExchangeShop:";
	
	private List<Exchange> exchanges;
	private String name;
	
	public Shop(String name, List<Exchange> exchanges) {
		this.exchanges = exchanges;
		this.name = name;
	}
	public Shop(String name) {
		this.exchanges = new ArrayList<Exchange>();
		this.name = name;
	}
	
	public void addExchange(Exchange exchange) {
		this.exchanges.add(exchange);
	}
	
	public String getName() {
		return name;
	}
	
	public List<Exchange> getExchanges() {
		return this.exchanges;
	}
	
	public List<String> getLore() {
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.DARK_AQUA + "Name:");
		lore.add(ChatColor.AQUA + getName());
		lore.add(ChatColor.DARK_AQUA + "Exchanges:");
		for(Exchange exchange : exchanges) {
			lore.add(ChatColor.AQUA + exchange.toString());
		}
		lore.add(loreSerialize());
		return lore;
	}
	
	public String loreSerialize() {
		String json = "";
		for(char c : (lorePrefix + GsonUtil.getGson().toJson(this)).toCharArray()) {
			json += "\u00A7" + c;
		}
		return json;
	}
	
	public void updateShop(ItemStack item) {
		ISUtils.setLore(item, (String[]) getLore().toArray());
	}
	
	public ItemStack toItem() {
		ItemStack item = new ItemStack(Material.STONE_BUTTON);
		ISUtils.setName(item, ChatColor.GREEN + "Shop");
		updateShop(item);
		return item;
	}
	
	public static boolean isValidShop(ItemStack item) {
		if(!item.hasItemMeta())
			return false;
		List<String> lore = item.getItemMeta().getLore();
		if(!lore.isEmpty()) {
			String lastLore = lore.get(lore.size()-1).replaceAll("\u00A7", "");
			if(lastLore.startsWith(lorePrefix)) {
				return true;
			} else {
				Logger.getAnonymousLogger().info(lastLore);
			}
		}
		return false;
	}
	
	public static Shop fromItem(ItemStack item) {
		List<String> lore = item.getItemMeta().getLore();
		if(!lore.isEmpty()) {
			String lastLore = lore.get(lore.size()-1).replaceAll("\u00A7", "");
			if(lastLore.startsWith(lorePrefix)) {
				lastLore = lastLore.replaceFirst(lorePrefix, "");
				try {
					return GsonUtil.getGson().fromJson(lastLore, Shop.class);
				} catch(Exception ex) {
					Logger.getLogger("ItemExchange").severe("Failed to parse shop from item");
				}
			}
		}
		return null;
	}
	
	public void ShowShop(Player player, final Inventory shopInventory) {
		int slots = (int) Math.ceil((getExchanges().size()) / 9f) * 9;
		ClickableInventory menu = new ClickableInventory(slots, getName());
		final Shop s = this;
		for(final Exchange ex : getExchanges()) {
			ItemStack vanityItem = new ItemStack(ex.getOutputs().get(0).getData().getItemType());
			ISUtils.addLore(vanityItem, ChatColor.DARK_AQUA + "Input:");
			ISUtils.addLore(vanityItem, new StringBuilder()
				.append(ChatColor.AQUA)
				.append(ex.getInput().getCount())
				.append(" ")
				.append(ex.getInput().getData().getItemType().toString())
				.toString());
			for(ExchangeRule er : ex.getInput().getRules()) {
				ISUtils.addLore(vanityItem, new StringBuilder()
					.append(ChatColor.GREEN)
					.append(er.getDisplayText())
					.toString());
			}
			
			
			
			ISUtils.addLore(vanityItem, ChatColor.DARK_AQUA + "Outputs:");
			ISUtils.addLore(vanityItem, new StringBuilder()
				.append(ChatColor.AQUA)
				.append(ex.getInput().getCount())
				.append(" ")
				.append(ex.getInput().getData().getItemType().toString())
				.toString());
			for(ExchangeRule er : ex.getInput().getRules()) {
				ISUtils.addLore(vanityItem, new StringBuilder()
					.append(ChatColor.GREEN)
					.append(er.getDisplayText())
					.toString());
			}
			
			
			
			Clickable exchangeButton = new Clickable(vanityItem) {
				public void clicked(Player player) {
					ex.ShowChooseInput(player, s, shopInventory);
				}
			};
			menu.addSlot(exchangeButton);
		}
		menu.showInventory(player);
	}
	
	public void ShowShopModify(Player player, ItemStack shopItem) {
		ClickableInventory menu = new ClickableInventory(18, getName());
		
		menu.showInventory(player);
	}
	
}
