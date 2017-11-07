package com.github.arowshot.itemexchange;

import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import com.github.arowshot.itemexchange.command.ItemExchangeCommandHandler;
import com.github.arowshot.itemexchange.exchangerules.rules.DamageRule;
import com.github.arowshot.itemexchange.listeners.ItemExchangeListener;

import vg.civcraft.mc.civmodcore.ACivMod;
import vg.civcraft.mc.civmodcore.command.CommandHandler;

public class ItemExchangePlugin extends ACivMod {
	private static ItemExchangePlugin plugin;
	private CommandHandler handle;
	
	@Override
	public void onEnable() {
		plugin = this;
		handle = new ItemExchangeCommandHandler();
		handle.registerCommands();
		super.onEnable();
		registerListeners();
		
		DamageRule rule = new DamageRule((short)100, (short)-1, Material.DIAMOND_AXE);
		ItemStack newItem = new ItemStack(Material.DIAMOND_AXE);
		newItem.setDurability((short) 1500);
		if(!getServer().getOnlinePlayers().isEmpty())
			getServer().getPlayer("ArowShot").getInventory().addItem(newItem);
		Logger.getGlobal().info(rule.getDisplayText());
		Logger.getGlobal().info(DamageRule.itemAllowed(newItem) ? "Material is allowed" : "Material is not allowed");
		Logger.getGlobal().info(rule.conforms(newItem) ? "Item can be sold" : "Item can not be sold");
		//Logger.getGlobal().info(newItem.getData().getData()+"");
		
		//ExchangeIO input = new ExchangeIO(ExchangeType.INPUT, Material.STONE, 8, null);
		//ExchangeIO output = new ExchangeIO(ExchangeType.INPUT, Material.DIRT, 8, null);
		//List<ExchangeIO> outputs = new ArrayList<ExchangeIO>();
		//outputs.add(output);
		//Exchange exc = new Exchange(input, outputs);
		//List<Exchange> exchanges = new ArrayList<Exchange>();
		//exchanges.add(exc);
		//Shop shop = new Shop("Test shop", exchanges);
		//if(!getServer().getOnlinePlayers().isEmpty())
		//	exc.ShowChooseInput(getServer().getPlayer("ArowShot"), shop, null);
	}
	
	@Override
	protected String getPluginName() {
		return "ItemExchangePlugin";
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return handle.execute(sender, cmd, args);
	}
	
	public void registerListeners() {
		plugin.getServer().getPluginManager()
			.registerEvents(new ItemExchangeListener(), plugin);
	}

	public static ItemExchangePlugin getInstance() {
		return plugin;
	}
	
}
