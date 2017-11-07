package com.github.arowshot.itemexchange.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.github.arowshot.itemexchange.exchanges.Exchange;
import com.github.arowshot.itemexchange.exchanges.ExchangeIO;
import com.github.arowshot.itemexchange.exchanges.ExchangeIO.ExchangeType;
import com.github.arowshot.itemexchange.exchanges.Shop;

public class ItemExchangeUtil {
	public static Shop getShop(Inventory inv) {
		List<Exchange> exchanges = new ArrayList<Exchange>();
		ExchangeIO input = null;
		List<ExchangeIO> outputs = new ArrayList<ExchangeIO>();
		for(ItemStack item : inv) {
			if(Shop.isValidShop(item)) {
				return Shop.fromItem(item);
			}
			if(Exchange.isValidExchange(item)) {
				exchanges.add(Exchange.fromItem(item));
			}
			if(ExchangeIO.isValidExchange(item)) {
				ExchangeIO io = ExchangeIO.fromItem(item);
				if(io.getType() == ExchangeType.INPUT) {
					if(input != null) {
						exchanges.add(new Exchange(input, outputs));
						outputs = new ArrayList<ExchangeIO>();
					}
					input = io;
				}
				if(io.getType() == ExchangeType.OUTPUT) {
					outputs.add(io);
				}
			}
		}
		if(!exchanges.isEmpty()) {
			return new Shop("Shop", exchanges);
		}
		return null;
	}
	
	
	public static ItemStack[] duplicateContents(Inventory inventory) {
		ItemStack[] inv = inventory.getContents();
		ItemStack[] clone = new ItemStack[inv.length];
		for(int i = 0; i < inv.length; i++) {
			clone[i] = inv[i].clone();
		}
		return clone;
	}
	
	public static String getName(MaterialData dat) {
		System.out.println(dat.toString());
		return dat.toString();
	}
}
