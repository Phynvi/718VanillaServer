package com.feather.net.packets.incoming.impl;

import com.feather.game.minigames.creations.StealingCreation;
import com.feather.game.player.Player;
import com.feather.game.player.Skills;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;

public class EnterIntegerPacket extends AbstractLogicPacket {
	
	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		if(!player.isRunning() || player.isDead())
			return;
		int value = stream.readInt();
		if((player.getInterfaceManager().containsInterface(762) && player.getInterfaceManager().containsInterface(
				763)) || player.getInterfaceManager().containsInterface(11)) {
			if(value < 0)
				return;
			Integer bank_item_X_Slot = (Integer) player.getTemporaryAttributtes().remove("bank_item_X_Slot");
			if(bank_item_X_Slot == null)
				return;
			player.getBank().setLastX(value);
			player.getBank().refreshLastX();
			if(player.getTemporaryAttributtes().remove("bank_isWithdraw") != null)
				player.getBank().withdrawItem(bank_item_X_Slot, value);
			else
				player.getBank().depositItem(bank_item_X_Slot, value, player.getInterfaceManager()
						.containsInterface(11) ? false : true);
		} else if(player.getInterfaceManager().containsInterface(206) && player.getInterfaceManager()
				.containsInterface(207)) {
			if(value < 0)
				return;
			Integer pc_item_X_Slot = (Integer) player.getTemporaryAttributtes().remove("pc_item_X_Slot");
			if(pc_item_X_Slot == null)
				return;
			if(player.getTemporaryAttributtes().remove("pc_isRemove") != null)
				player.getPriceCheckManager().removeItem(pc_item_X_Slot, value);
			else
				player.getPriceCheckManager().addItem(pc_item_X_Slot, value);
		} else if(player.getInterfaceManager().containsInterface(671) && player.getInterfaceManager()
				.containsInterface(665)) {
			if(player.getFamiliar() == null || player.getFamiliar().getBob() == null)
				return;
			if(value < 0)
				return;
			Integer bob_item_X_Slot = (Integer) player.getTemporaryAttributtes().remove("bob_item_X_Slot");
			if(bob_item_X_Slot == null)
				return;
			if(player.getTemporaryAttributtes().remove("bob_isRemove") != null)
				player.getFamiliar().getBob().removeItem(bob_item_X_Slot, value);
			else
				player.getFamiliar().getBob().addItem(bob_item_X_Slot, value);
		} else if(player.getInterfaceManager().containsInterface(335) && player.getInterfaceManager()
				.containsInterface(336)) {
			if(value < 0)
				return;
			if(player.getTemporaryAttributtes().get("lend_item_time") != null) {
				if(value <= 0)
					return;
				Integer slot = (Integer) player.getTemporaryAttributtes().remove("lend_item_time");
				if(value > 24) {
					player.getPackets().sendGameMessage("You can only lend for a maximum of 24 hours");
					return;
				}
				player.getTrade().lendItem(slot, value);
				player.getTemporaryAttributtes().remove("lend_item_time");
				return;
			}
			Integer trade_item_X_Slot = (Integer) player.getTemporaryAttributtes().remove("trade_item_X_Slot");
			if(trade_item_X_Slot == null)
				return;
			if(player.getTemporaryAttributtes().remove("trade_isRemove") != null)
				player.getTrade().removeItem(trade_item_X_Slot, value);
			else
				player.getTrade().addItem(trade_item_X_Slot, value);
		} else if(player.getTemporaryAttributtes().get("money_pouch_x") == Boolean.TRUE) {
			int amount = player.getPouch().getMoney();
			if(amount <= 0) {
				return;
			}
			if(value > player.getPouch().getMoney()) {
				player.getPouch().handleMoneyPouch(true, false, false, amount, 0);
			} else {
				player.getPouch().handleMoneyPouch(true, false, false, value, 0);
			}
		} else if(player.getTemporaryAttributtes().get("moneypouch") != null) {
			if(player.isLocked()) {
				return;
			}
			int amount = player.getPouch().getMoney();
			if(!player.getInventory().hasFreeSlots() && !player.getInventory().containsOneItem(995)) {
				player.getPackets().sendGameMessage("Not enough space in your inventory!");
				return;
			}
			if(amount <= 0) {
				return;
			}
			if(value > player.getPouch().getMoney()) {
				player.getPouch().handleMoneyPouch(false, false, true, amount, 0);
			} else {
				player.getPouch().handleMoneyPouch(false, false, true, value, 0);
			}
		} else if(player.getTemporaryAttributtes().get("skillId") != null) {
			if(player.getEquipment().wearingArmour()) {
				player.getDialogueManager().finishDialogue();
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You cannot do this while having armour on!");
				return;
			}
			int skillId = (Integer) player.getTemporaryAttributtes().remove("skillId");
			if(skillId == Skills.HITPOINTS && value <= 9)
				value = 10;
			else if(value < 1)
				value = 1;
			else if(value > 99)
				value = 99;
			player.getSkills().set(skillId, value);
			player.getSkills().setXp(skillId, Skills.getXPForLevel(value));
			player.getAppearence().loadAppearanceBlock();
			player.getDialogueManager().finishDialogue();
		} else if(player.getTemporaryAttributtes().get("kilnX") != null) {
			int index = (Integer) player.getTemporaryAttributtes().get("scIndex");
			int componentId = (Integer) player.getTemporaryAttributtes().get("scComponentId");
			int itemId = (Integer) player.getTemporaryAttributtes().get("scItemId");
			player.getTemporaryAttributtes().remove("kilnX");
			if(StealingCreation.proccessKilnItems(player, componentId, index, itemId, value))
				return;
		}
	}
	
	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.ENTER_INTEGER_PACKET };
	}
	
}
