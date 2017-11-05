package com.devotedmc.itemexchange.exchangerules.rules;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.devotedmc.itemexchange.exchangerules.ExchangeRule;
import com.devotedmc.itemexchange.exchanges.Exchange;

import vg.civcraft.mc.civmodcore.inventorygui.Clickable;

public class DamageRule extends ExchangeRule {
	private Material itemType;
	
	private short damageValueMin = -1;
	private short damageValueMax = -1;
	
	public DamageRule(short minDamage, short maxDamage, Material itemType) {
		this.damageValueMin = minDamage;
		this.damageValueMax = maxDamage;
		this.itemType = itemType;
	}
	
	private static float getPercent(Material itemType, short damageValue) {
		return 100 - (((float)damageValue / (float)itemType.getMaxDurability()) * 100f);
	}
	
	@Override
	public String getDisplayText() {
		if(this.damageValueMin == this.damageValueMax) {
			return String.format("Durability is %d (%.2f%%)",
					itemType.getMaxDurability() - this.damageValueMax,
					getPercent(itemType, this.damageValueMax));
		}
		if(this.damageValueMin == -1) {
			return String.format("Durability below %d (%.2f%%)",
					itemType.getMaxDurability() - this.damageValueMax,
					getPercent(itemType, this.damageValueMax));
		} else if(this.damageValueMax == -1) {
			return String.format("Durability above %d (%.2f%%)",
					itemType.getMaxDurability() - this.damageValueMin,
					getPercent(itemType, this.damageValueMin));
		}
		
		return String.format("Durability between %d and %d (%.2f%% and %.2f%%)",
				itemType.getMaxDurability() - this.damageValueMin,
				itemType.getMaxDurability() - this.damageValueMax,
				getPercent(itemType, this.damageValueMin),
				getPercent(itemType, this.damageValueMax));
	}
	
	private static short getInversedDurability(ItemStack item) {
		return (short) (item.getType().getMaxDurability() - item.getDurability());
	}
	
	public static boolean itemAllowed(ItemStack item) {
		return item.getType().getMaxDurability() > 0;
	}
	
	public short getDamageValueMin() {
		return damageValueMin;
	}

	public void setDamageValueMin(short damageValueMin) {
		this.damageValueMin = damageValueMin;
	}

	public short getDamageValueMax() {
		return damageValueMax;
	}

	public void setDamageValueMax(short damageValueMax) {
		this.damageValueMax = damageValueMax;
	}

	public Material getItemType() {
		return itemType;
	}

	public void setItemType(Material itemType) {
		this.itemType = itemType;
	}

	@Override
	public boolean conforms(ItemStack item) {
		if(this.damageValueMin == -1) {
			return getInversedDurability(item) < this.damageValueMax;
		} else if(this.damageValueMax == -1) {
			return getInversedDurability(item) > this.damageValueMin;
		}
		return getInversedDurability(item) > this.damageValueMin && getInversedDurability(item) < this.damageValueMax;
	}

	@Override
	public void showModifyRule(Exchange ex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Clickable makeModifyButton(Exchange ex) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
