package com.devotedmc.itemexchange.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.devotedmc.itemexchange.ItemExchangePlugin;
import com.devotedmc.itemexchange.exchanges.Exchange;
import com.devotedmc.itemexchange.exchanges.ExchangeIO;
import com.devotedmc.itemexchange.exchanges.ExchangeIO.ExchangeType;
import com.devotedmc.itemexchange.exchanges.Shop;

public class ItemExchangeListener implements Listener{
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent event) {
		Inventory top = event.getView().getTopInventory();
		if(top instanceof CraftingInventory && event.getWhoClicked() instanceof Player) {
			final Player player = (Player) event.getWhoClicked();
			final CraftingInventory inv = (CraftingInventory) top;
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(ItemExchangePlugin.getInstance(), new Runnable() {
				public void run() {
					List<ExchangeIO> inputs = new ArrayList<ExchangeIO>();
					List<ExchangeIO> outputs = new ArrayList<ExchangeIO>();
					List<Exchange> exchanges = new ArrayList<Exchange>();
					List<Shop> shops = new ArrayList<Shop>();
					for(ItemStack item : inv.getMatrix()) {
						if(item != null && item.getType() != Material.AIR) {
							if(ExchangeIO.isValidExchange(item)) {
								ExchangeIO e = ExchangeIO.fromItem(item);
								if(e.getType() == ExchangeType.INPUT) {
									inputs.add(e);
								} else if(e.getType() == ExchangeType.OUTPUT) {
									outputs.add(e);
								}
							} else if(Exchange.isValidExchange(item)) {
								Exchange e = Exchange.fromItem(item);
								exchanges.add(e);
							} else if(Shop.isValidShop(item)) {
								Shop e = Shop.fromItem(item);
								shops.add(e);
							} else {
								return; //foreign items not allowed
							}
						}
					}
					
					//TODO: Cap shop at 9 exchanges
					if (exchanges.size() == 0 && inputs.size() == 1 && outputs.size() > 0 && shops.size() == 0) {
						Exchange e = new Exchange(inputs.get(0), outputs);
						inv.setResult(e.toItem());
						player.updateInventory();
					} else if (exchanges.size() == 1 && inputs.size() == 0 && outputs.size() > 0 && shops.size() == 0) {
						outputs.addAll(exchanges.get(0).getOutputs());
						Exchange e = new Exchange(exchanges.get(0).getInput(), outputs);
						inv.setResult(e.toItem());
						player.updateInventory();
					} else if (exchanges.size() > 0 && inputs.size() == 0 && outputs.size() == 0 && shops.size() == 0) {
						Shop shop = new Shop("Shop", exchanges);
						inv.setResult(shop.toItem());
						player.updateInventory();
					} else if (exchanges.size() > 0 && inputs.size() == 0 && outputs.size() == 0 && shops.size() == 1) {
						shops.get(0).getExchanges().addAll(exchanges);
						inv.setResult(shops.get(0).toItem());
						player.updateInventory();
					}
				}
			});
		}
	}
}