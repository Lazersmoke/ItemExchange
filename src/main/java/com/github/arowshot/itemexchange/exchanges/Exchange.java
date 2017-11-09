package com.github.arowshot.itemexchange.exchanges;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.arowshot.itemexchange.util.ItemExchangeUtil;
import com.github.arowshot.itemexchange.util.Serializable;
import com.github.arowshot.itemexchange.util.TransactionFailedException;

import net.md_5.bungee.api.ChatColor;
import vg.civcraft.mc.civmodcore.inventorygui.Clickable;
import vg.civcraft.mc.civmodcore.inventorygui.ClickableInventory;
import vg.civcraft.mc.civmodcore.itemHandling.ISUtils;

public class Exchange implements Serializable {
	private ExchangeIO input;
	private List<ExchangeIO> outputs;
	
	
	public Exchange(ExchangeIO input, List<ExchangeIO> outputs) {
		this.setInput(input);
		this.setOutputs(outputs);
	}
	
	public ItemStack toItem() {
		ItemStack item = new ItemStack(Material.STONE_BUTTON);
		ISUtils.setName(item, ChatColor.GREEN + "Item Exchange");
		ISUtils.addLore(item, ChatColor.DARK_AQUA + "Input:");
		ISUtils.addLore(item, new StringBuilder()
				.append(ChatColor.AQUA)
				.append(input.getCount())
				.append(" ")
				.append(ItemExchangeUtil.getName(getInput().getData()))
				.append(" (")
				.append(input.getRules().size())
				.append(" rules)")
				.toString());
		
		ISUtils.addLore(item, ChatColor.DARK_AQUA + "Outputs:");
		for(ExchangeIO output : outputs) {
			ISUtils.addLore(item, new StringBuilder()
					.append(ChatColor.AQUA)
					.append(output.getCount())
					.append(" ")
					.append(ItemExchangeUtil.getName(output.getData()))
					.append(" (")
					.append(output.getRules().size())
					.append(" rules)")
					.toString());
		}
		/*String json = "";
		for(char c : (lorePrefix + GsonUtil.getGson().toJson(this)).toCharArray()) {
			json += "\u00A7" + c;
		}
		ISUtils.addLore(item, json);*/
		item = ItemExchangeUtil.serializeSomething(item, this);
		return item;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder()
				.append(input.getCount())
				.append(" ")
				.append(ItemExchangeUtil.getName(getInput().getData()))
				.append(" -> ");
		
		for(ExchangeIO item : outputs) {
			sb.append(item.getCount())
			.append(" ")
			.append(item.getData().getItemType())
			.append(", ");
		}
		
		return sb.toString().substring(0, sb.length()-2); //cut off last comma
	}

	public ExchangeIO getInput() {
		return input;
	}

	public void setInput(ExchangeIO input) {
		this.input = input;
	}

	public List<ExchangeIO> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<ExchangeIO> outputs) {
		this.outputs = outputs;
	}
	
	public void ShowExchangeModify(Player player, Shop shop, Inventory shopInventory) {
		ClickableInventory menu = new ClickableInventory(18, "");
		
		menu.showInventory(player);
	}
	
	public void ShowChooseInput(final Player player, final Shop shop, final Inventory shopInventory) {		
		List<ItemStack> acceptableItems = findAcceptableItems(player.getInventory(), getInput());
		int slots = (int) Math.ceil((acceptableItems.size()+1) / 9f) * 9;

		ClickableInventory menu = new ClickableInventory(slots, "Choose an input stack:");
		
		ItemStack backButtonItem = new ItemStack(Material.BARRIER);
		ISUtils.setName(backButtonItem, ChatColor.RED + "<- Go back");
		//ISUtils.addLore() for screen help
		Clickable backButton = new Clickable(backButtonItem) {
			public void clicked(Player player) {
				shop.ShowShop(player, shopInventory);
			}
		};
		menu.setSlot(backButton, slots-1);
		
		for(final ItemStack is : acceptableItems) {
			ItemStack vanityItem = is.clone();
			//Loop through rules and apply transforms to make the item look pretty
			ISUtils.addLore(vanityItem, ChatColor.AQUA + "Click to pay with this stack.");
			Clickable purchaseButton = new Clickable(vanityItem) {
				public void clicked(Player player) {
					ItemStack remove = is.clone();
					remove.setAmount(input.getCount());
					ItemStack[] playerInventoryContents = ItemExchangeUtil.duplicateContents(player.getInventory());
					ItemStack[] shopInventoryContents = ItemExchangeUtil.duplicateContents(shopInventory);
					try {
						//Make sure exchange still exists
						//unfinished
						
						//Remove items from player inventory
						if(!player.getInventory().removeItem(remove).isEmpty()) {
							player.sendMessage(ChatColor.RED + "Failed to remove items from inventory");
							throw new TransactionFailedException("Failed to remove items from inventory");
						}
						
						//Remove items from shop
			            if (!shopInventory.addItem(remove).isEmpty()) {
			            	player.sendMessage(ChatColor.RED + "Failed to add items to shop");
			            	throw new TransactionFailedException("Failed to add items to shop");
			            }
						
						//Remove items from shop inventory and add them to player inventory
						for(ExchangeIO output : getOutputs()) {		
							List<ItemStack> acceptableOutputs = findAcceptableItems(shopInventory, output);
							//Remove types of items where there are not enough to output
							for(int i = 0; i < acceptableOutputs.size(); i++) {
								if(acceptableOutputs.get(i).getAmount() < output.getCount()) {
									acceptableOutputs.remove(i);
									i--;
								}
							}
							if(acceptableOutputs.size() == 0) {
								player.sendMessage(ChatColor.RED + "Not enough output items in chest");
								throw new TransactionFailedException("Not enough output items in chest");
							}
							ItemStack outputItem = acceptableOutputs.get(0).clone();
							outputItem.setAmount(output.getCount());
							if(!shopInventory.removeItem(outputItem).isEmpty()) {
								player.sendMessage(ChatColor.RED + "Failed to remove output items from chest");
								throw new TransactionFailedException("Failed to remove output items from chest");
							}
							if(!player.getInventory().addItem(outputItem).isEmpty()) {
								player.sendMessage(ChatColor.RED + "Failed to add items to inventory");
								throw new TransactionFailedException("Failed to add items to inventory");
							}
						}
						player.sendMessage(ChatColor.GREEN + "Exchanged items successfully!");
					} catch(TransactionFailedException ex) {
						//reset inventories
						player.getInventory().setContents(playerInventoryContents);
						shopInventory.setContents(shopInventoryContents);
					} finally {
						ShowChooseInput(player, shop, shopInventory);
						//in order to refresh the gui
					}
				}
			};
			menu.addSlot(purchaseButton);
		}
		
		menu.showInventory(player);
	}
	
	public static List<ItemStack> findAcceptableItems(Inventory i, ExchangeIO model) {
		List<ItemStack> acceptableItems = new ArrayList<ItemStack>();
		invloop: for(ItemStack invitem : i) {
			if(invitem == null || invitem.getType().equals(Material.AIR))
				continue;
			if(model.conforms(invitem)) {
				for(ItemStack accitem : acceptableItems) {
					if(invitem == null || invitem.getType().equals(Material.AIR))
						continue;
					if(accitem.isSimilar(invitem)) {
						accitem.setAmount(accitem.getAmount() + invitem.getAmount()); //caps at 64
						continue invloop; // woah labels
					}
				}
				acceptableItems.add(invitem.clone());
			}
		}
		return acceptableItems;
	}
	
	public void ShowModifyInput(Player player, Shop shop, Inventory shopInventory) {
		ClickableInventory menu = new ClickableInventory(18, "");
		
		menu.showInventory(player);
	}
}
