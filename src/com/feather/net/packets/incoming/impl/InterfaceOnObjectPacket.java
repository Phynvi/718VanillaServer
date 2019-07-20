package com.feather.net.packets.incoming.impl;

import com.feather.game.World;
import com.feather.game.WorldObject;
import com.feather.game.WorldTile;
import com.feather.game.item.Item;
import com.feather.game.player.Inventory;
import com.feather.game.player.Player;
import com.feather.io.InputStream;
import com.feather.net.decoders.handlers.ObjectHandler;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;
import com.feather.utils.Utils;

public class InterfaceOnObjectPacket extends AbstractLogicPacket {
	
	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		boolean forceRun = stream.readByte128() == 1;
		int itemId = stream.readShortLE128();
		int y = stream.readShortLE128();
		int objectId = stream.readIntV2();
		int interfaceHash = stream.readInt();
		final int interfaceId = interfaceHash >> 16;
		int slot = stream.readShortLE();
		int x = stream.readShort128();
		if(!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead()) {
			return;
		}
		long currentTime = Utils.currentTimeMillis();
		if(player.getLockDelay() >= currentTime || player.getEmotesManager().getNextEmoteEnd() >= currentTime) {
			return;
		}
		final WorldTile tile = new WorldTile(x, y, player.getPlane());
		int regionId = tile.getRegionId();
		if(!player.getMapRegionsIds().contains(regionId)) {
			return;
		}
		WorldObject mapObject = World.getRegion(regionId).getObject(objectId, tile);
		if(mapObject == null || mapObject.getId() != objectId) {
			return;
		}
		final WorldObject object = !player.isAtDynamicRegion() ? mapObject
				: new WorldObject(objectId, mapObject.getType(), mapObject.getRotation(), x, y, player.getPlane());
		final Item item = player.getInventory().getItem(slot);
		if(player.isDead() || Utils.getInterfaceDefinitionsSize() <= interfaceId) {
			return;
		}
		if(player.getLockDelay() > Utils.currentTimeMillis()) {
			return;
		}
		if(!player.getInterfaceManager().containsInterface(interfaceId)) {
			return;
		}
		if(item == null || item.getId() != itemId) {
			return;
		}
		
		player.stopAll(false); // false
		if(forceRun) {
			player.setRun(forceRun);
		}
		switch (interfaceId) {
			case Inventory.INVENTORY_INTERFACE: // inventory
				ObjectHandler.handleItemOnObject(player, object, interfaceId, item);
				break;
		}
	}
	
	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.INTERFACE_ON_OBJECT };
	}
	
}
