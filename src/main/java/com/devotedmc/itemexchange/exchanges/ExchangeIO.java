package com.devotedmc.itemexchange.exchanges;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.devotedmc.itemexchange.exchangerules.ExchangeRule;
import com.devotedmc.itemexchange.util.GsonUtil;

import net.md_5.bungee.api.ChatColor;
import vg.civcraft.mc.civmodcore.itemHandling.ISUtils;

public class ExchangeIO {
	private static String lorePrefix = "ExchangeItemIO:";
	
	private ExchangeType type;
	private MaterialData data;
	private int count;
	private List<ExchangeRule> rules;
	
	public ExchangeIO(ExchangeType io, MaterialData data, int i, List<ExchangeRule> rules) {
		this.setType(io);
		this.setData(data);
		this.setCount(i);
		if(rules != null)
			this.setRules(rules);
		else
			this.setRules(new ArrayList<ExchangeRule>());
	}
	
	public ItemStack toItem() {
		ItemStack item = new ItemStack(Material.STONE_BUTTON);
		if(getType() == ExchangeType.INPUT)
			ISUtils.setName(item, ChatColor.GREEN + "Item Input");
		if(getType() == ExchangeType.OUTPUT)
			ISUtils.setName(item, ChatColor.GREEN + "Item Output");
		ISUtils.addLore(item, ChatColor.DARK_AQUA + "Item:");
		ISUtils.addLore(item, new StringBuilder()
				.append(ChatColor.AQUA)
				.append(getCount())
				.append(" ")
				.append(getData().getItemType().toString())
				.toString());
		ISUtils.addLore(item, ChatColor.DARK_AQUA + "Rules:");
		if(getRules().size() == 0) {
			ISUtils.addLore(item, ChatColor.AQUA + "None");
		} else {
			for(ExchangeRule er : getRules()) {
				ISUtils.addLore(item, new StringBuilder()
					.append(ChatColor.GREEN)
					.append(er.getDisplayText())
					.toString());
			}
		}
		String json = "";
		for(char c : ("ExchangeItemIO:" + GsonUtil.getGson().toJson(this)).toCharArray()) {
			json += "\u00A7" + c;
		}
		ISUtils.addLore(item, json);
		return item;
	}
	
	public boolean conforms(ItemStack is) {
		boolean conforms = true;
		for(ExchangeRule rule : getRules()) {
			if(!rule.conforms(is)) {
				conforms = false;
			}
		}
		if(!is.getData().equals(getData().getItemType())) {
			return false;
		}
		return conforms;
	}
	
	public static boolean isValidExchange(ItemStack item) {
		if(!item.hasItemMeta())
			return false;
		List<String> lore = item.getItemMeta().getLore();
		if(!lore.isEmpty()) {
			String lastLore = lore.get(lore.size()-1).replaceAll("\u00A7", "");
			if(lastLore.startsWith(lorePrefix)) {
				return true;
			}
		}
		return false;
	}
	
	public static ExchangeIO fromItem(ItemStack item) {
		List<String> lore = item.getItemMeta().getLore();
		if(!lore.isEmpty()) {
			String lastLore = lore.get(lore.size()-1).replaceAll("\u00A7", "");
			if(lastLore.startsWith(lorePrefix)) {
				lastLore = lastLore.replaceFirst(lorePrefix, "");
				try {
					return GsonUtil.getGson().fromJson(lastLore, ExchangeIO.class);
				} catch(Exception ex) {
					ex.printStackTrace();
					Logger.getLogger("ItemExchange").severe("Failed to parse exchange from item");
				}
			}
		}
		return null;
	}
	
	public ExchangeType getType() {
		return type;
	}

	public void setType(ExchangeType type) {
		this.type = type;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int i) {
		this.count = i;
	}

	public List<ExchangeRule> getRules() {
		return rules;
	}

	public void setRules(List<ExchangeRule> rules) {
		this.rules = rules;
	}

	public MaterialData getData() {
		return data;
	}

	public void setData(MaterialData data) {
		this.data = data;
	}

	public enum ExchangeType {
		INPUT,
		OUTPUT
	}
}
