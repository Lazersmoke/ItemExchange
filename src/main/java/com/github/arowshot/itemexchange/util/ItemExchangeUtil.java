package com.github.arowshot.itemexchange.util;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.github.arowshot.itemexchange.exchanges.Exchange;
import com.github.arowshot.itemexchange.exchanges.ExchangeIO;
import com.github.arowshot.itemexchange.exchanges.ExchangeIO.ExchangeType;

import vg.civcraft.mc.civmodcore.itemHandling.TagManager;

import com.github.arowshot.itemexchange.exchanges.Shop;

public class ItemExchangeUtil {
	public static Shop getShop(Inventory inv) {
		List<Exchange> exchanges = new ArrayList<Exchange>();
		ExchangeIO input = null;
		List<ExchangeIO> outputs = new ArrayList<ExchangeIO>();
		for(ItemStack item : inv) {
			if(item == null)
				continue;
			if(ItemExchangeUtil.isSomething(item, Shop.class)) {
				return ItemExchangeUtil.parseSomething(item, Shop.class);
			}
			if(ItemExchangeUtil.isSomething(item, Exchange.class)) {
				exchanges.add(ItemExchangeUtil.parseSomething(item, Exchange.class));
			}
			if(ItemExchangeUtil.isSomething(item, ExchangeIO.class)) {
				ExchangeIO io = ItemExchangeUtil.parseSomething(item, ExchangeIO.class);
				if(io.getType() == ExchangeType.INPUT) {
					if(input != null) {
						if(!outputs.isEmpty())
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
		if(outputs != null && !outputs.isEmpty())
			exchanges.add(new Exchange(input, outputs));
		if(!exchanges.isEmpty()) {
			return new Shop("Shop", exchanges);
		}
		return null;
	}
	
	
	public static ItemStack[] duplicateContents(Inventory inventory) {
		ItemStack[] inv = inventory.getContents();
		ItemStack[] clone = new ItemStack[inv.length];
		for(int i = 0; i < inv.length; i++) {
			if(inv[i] != null)
				clone[i] = inv[i].clone();
		}
		return clone;
	}
	
	public static String getName(MaterialData dat) {
		System.out.println(dat.toString());
		return dat.toString();
	}
	
	public static <T extends Serializable> boolean isSomething(ItemStack item, Class<T> classOfT) {
		TagManager itemTags = new TagManager(item);
		if(itemTags.getString("itemexchange:"+classOfT)!="") {
			return true;
		} else {
			return false;
		}
	}
	
	public static <T extends Serializable> T parseSomething(ItemStack item, Class<T> classOfT) {
		TagManager itemTags = new TagManager(item);
		String possibleData = itemTags.getString("itemexchange:"+classOfT);
		if(possibleData!="") { 
			try {
				return GsonUtil.getGson().fromJson(possibleData, classOfT);
			} catch(Exception ex) {
				Logger.getLogger("ItemExchange").severe("Failed to parse item");
			}
		}
		return null;
	}
	
	public static <T extends Serializable> ItemStack serializeSomething(ItemStack item, T theT) {
		TagManager itemTags = new TagManager(item);
		itemTags.setString("itemexchange:"+theT.getClass(),
				GsonUtil.getGson().toJson(theT));
		System.out.println(itemTags.getString("itemexchange:"+theT.getClass()));
		return itemTags.enrichWithNBT(item);
	}
}
