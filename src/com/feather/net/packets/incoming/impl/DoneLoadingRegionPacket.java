package com.feather.net.packets.incoming.impl;

import com.feather.game.player.Player;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;

public class DoneLoadingRegionPacket extends AbstractLogicPacket {
	
	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		/*
		 * if(!player.clientHasLoadedMapRegion()) { //load objects and items here
		 * player.setClientHasLoadedMapRegion(); } //player.refreshSpawnedObjects();
		 * //player.refreshSpawnedItems();
		 */
	}
	
	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.DONE_LOADING_REGION_PACKET };
	}
	
}
