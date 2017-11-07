package com.github.arowshot.itemexchange.command.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.arowshot.itemexchange.exchangerules.ExchangeRule;
import com.github.arowshot.itemexchange.exchangerules.rules.DamageRule;
import com.github.arowshot.itemexchange.exchanges.ExchangeIO;
import com.github.arowshot.itemexchange.exchanges.ExchangeIO.ExchangeType;

import vg.civcraft.mc.civmodcore.command.PlayerCommand;

public class CreateExchangeCommand extends PlayerCommand {
	public CreateExchangeCommand(String name) {
		super(name);
		setIdentifier("iec");
		setDescription("Creates an exchange input or output.");
		setUsage("/iec <input|output>");
		setArguments(1, 1);
	}
	
	private void makeExchangeIO(Player player, ExchangeType type) {
		ItemStack held = player.getInventory().getItemInMainHand();
		if(player.getInventory().firstEmpty() == -1) {
			player.sendMessage(ChatColor.RED + "Not enough space in inventory.");
		} else {
			List<ExchangeRule> exchangeRules = new ArrayList<ExchangeRule>();
			if(DamageRule.itemAllowed(held)) {
				exchangeRules.add(new DamageRule(held.getDurability(), held.getDurability(), held.getType()));
			}
			ExchangeIO result = new ExchangeIO(type, held.getData(), held.getAmount(), exchangeRules);
			player.getInventory().addItem(result.toItem());
		}
	}
	
	public boolean execute(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.MAGIC + "Players only you fool");
			return false;
		}
		Player player = (Player) sender;
		if(args[0].equalsIgnoreCase("input")) {
			makeExchangeIO(player, ExchangeType.INPUT);
		} else if(args[0].equalsIgnoreCase("output")) {
			makeExchangeIO(player, ExchangeType.OUTPUT);
		} else {
			player.sendMessage("Please specify if the item is an input or output.");
		}
		return true;
	}

	public List<String> tabComplete(CommandSender sender, String[] args) {
		return null;
	}
}
