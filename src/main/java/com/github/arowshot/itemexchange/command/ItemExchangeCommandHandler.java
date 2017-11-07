package com.devotedmc.itemexchange.command;

import com.devotedmc.itemexchange.command.commands.CreateExchangeCommand;

import vg.civcraft.mc.civmodcore.command.CommandHandler;

public class ItemExchangeCommandHandler extends CommandHandler {
	@Override
	public void registerCommands() {
		addCommands(new CreateExchangeCommand("iec"));
	}
}
