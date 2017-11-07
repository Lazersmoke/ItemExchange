package com.github.arowshot.itemexchange.command;

import com.github.arowshot.itemexchange.command.commands.CreateExchangeCommand;

import vg.civcraft.mc.civmodcore.command.CommandHandler;

public class ItemExchangeCommandHandler extends CommandHandler {
	@Override
	public void registerCommands() {
		addCommands(new CreateExchangeCommand("iec"));
	}
}
