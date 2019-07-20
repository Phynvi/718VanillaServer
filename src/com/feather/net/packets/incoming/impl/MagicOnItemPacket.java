package com.feather.net.packets.incoming.impl;

import com.feather.game.Animation;
import com.feather.game.Graphics;
import com.feather.game.item.Item;
import com.feather.game.player.Player;
import com.feather.game.player.Skills;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;

public class MagicOnItemPacket extends AbstractLogicPacket {
	
	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		int inventoryInter = stream.readInt() >> 16;
		int itemId = stream.readShort128();
		@SuppressWarnings("unused")
		int junk = stream.readShort();
		@SuppressWarnings("unused")
		int itemSlot = stream.readShortLE();
		int interfaceSet = stream.readIntV1();
		int spellId = interfaceSet & 0xFFF;
		int magicInter = interfaceSet >> 16;
		if(inventoryInter == 149 && magicInter == 192) {
			switch (spellId) {
				case 59:// High Alch
					if(player.getSkills().getLevel(Skills.MAGIC) < 55) {
						player.getPackets().sendGameMessage(
								"You do not have the required level to cast this spell.");
						return;
					}
					if(itemId == 995) {
						player.getPackets().sendGameMessage("You can't alch this!");
						return;
					}
					if(player.getEquipment().getWeaponId() == 1401 || player.getEquipment().getWeaponId() == 3054
							|| player.getEquipment().getWeaponId() == 19323) {
						if(!player.getInventory().containsItem(561, 1)) {
							player.getPackets().sendGameMessage(
									"You do not have the required runes to cast this spell.");
							return;
						}
						player.setNextAnimation(new Animation(9633));
						player.setNextGraphics(new Graphics(112));
						player.getInventory().deleteItem(561, 1);
						player.getInventory().deleteItem(itemId, 1);
						player.getInventory().addItem(995, new Item(itemId, 1).getDefinitions().getValue() >> 6);
					} else {
						if(!player.getInventory().containsItem(561, 1) || !player.getInventory().containsItem(554,
								5)) {
							player.getPackets().sendGameMessage(
									"You do not have the required runes to cast this spell.");
							return;
						}
						player.setNextAnimation(new Animation(713));
						player.setNextGraphics(new Graphics(113));
						player.getInventory().deleteItem(561, 1);
						player.getInventory().deleteItem(554, 5);
						player.getInventory().deleteItem(itemId, 1);
						player.getInventory().addItem(995, new Item(itemId, 1).getDefinitions().getValue() >> 6);
					}
					break;
				default:
					System.out.println("Spell:" + spellId + ", Item:" + itemId);
			}
			System.out.println("Spell:" + spellId + ", Item:" + itemId);
		}
	}
	
	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.MAGIC_ON_ITEM_PACKET };
	}
	
}
