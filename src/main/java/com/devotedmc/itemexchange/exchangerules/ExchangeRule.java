package com.devotedmc.itemexchange.exchangerules;

import org.bukkit.inventory.ItemStack;

import com.devotedmc.itemexchange.exchanges.Exchange;

import vg.civcraft.mc.civmodcore.inventorygui.Clickable;

public abstract class ExchangeRule {
	public abstract boolean conforms(ItemStack item);
	public abstract String getDisplayText();
	public abstract void showModifyRule(Exchange ex);
	public abstract Clickable makeModifyButton(Exchange ex);
}
