package com.feather.net.packets.incoming.impl;

import com.feather.Settings;
import com.feather.game.player.Inventory;
import com.feather.game.player.Player;
import com.feather.game.player.content.Shop;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;
import com.feather.utils.Utils;

public class SwitchInterfaceItemPacket extends AbstractLogicPacket {
	
	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		stream.readShortLE128();
		int fromInterfaceHash = stream.readIntV1();
		int toInterfaceHash = stream.readInt();
		int fromSlot = stream.readUnsignedShort();
		int toSlot = stream.readUnsignedShortLE128();
		stream.readUnsignedShortLE();
		
		int toInterfaceId = toInterfaceHash >> 16;
		int toComponentId = toInterfaceHash - (toInterfaceId << 16);
		int fromInterfaceId = fromInterfaceHash >> 16;
		int fromComponentId = fromInterfaceHash - (fromInterfaceId << 16);
		
		if(Utils.getInterfaceDefinitionsSize() <= fromInterfaceId || Utils
				.getInterfaceDefinitionsSize() <= toInterfaceId)
			return;
		if(!player.getInterfaceManager().containsInterface(fromInterfaceId) || !player.getInterfaceManager()
				.containsInterface(toInterfaceId))
			return;
		if(fromComponentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(fromInterfaceId) <= fromComponentId)
			return;
		if(toComponentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(toInterfaceId) <= toComponentId)
			return;
		if(fromInterfaceId == Inventory.INVENTORY_INTERFACE && fromComponentId == 0
				&& toInterfaceId == Inventory.INVENTORY_INTERFACE && toComponentId == 0) {
			toSlot -= 28;
			if(toSlot < 0 || toSlot >= player.getInventory().getItemsContainerSize() || fromSlot >= player
					.getInventory().getItemsContainerSize())
				return;
			player.getInventory().switchItem(fromSlot, toSlot);
		} else if(fromInterfaceId == 763 && fromComponentId == 0 && toInterfaceId == 763 && toComponentId == 0) {
			if(toSlot >= player.getInventory().getItemsContainerSize() || fromSlot >= player.getInventory()
					.getItemsContainerSize())
				return;
			player.getInventory().switchItem(fromSlot, toSlot);
		} else if(fromInterfaceId == 762 && toInterfaceId == 762) {
			player.getBank().switchItem(fromSlot, toSlot, fromComponentId, toComponentId);
		} else if(fromInterfaceId == 1265 && toSlot == 65535) { // Shop drag item to inventory
			Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
			if(player.isBuying) {
				shop.buy(player, fromSlot, shop.quantity);
			}
		}
		if(Settings.DEBUG)
			System.out.println("Switch item " + fromInterfaceId + ", " + fromSlot + ", " + toSlot);
	}
	
	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.SWITCH_INTERFACE_ITEM_PACKET };
	}
	
}
