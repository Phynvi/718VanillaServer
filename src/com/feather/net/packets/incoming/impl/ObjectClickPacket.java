package com.feather.net.packets.incoming.impl;

import com.feather.game.player.Player;
import com.feather.io.InputStream;
import com.feather.net.decoders.handlers.ObjectHandler;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;

public class ObjectClickPacket extends AbstractLogicPacket {
	
	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		switch(packet.getId()) {
			case OBJECT_CLICK_1_PACKET:
				ObjectHandler.handleOption(player, stream, 1);
				return;
			case OBJECT_CLICK_2_PACKET:
				ObjectHandler.handleOption(player, stream, 2);
				return;
			case OBJECT_CLICK_3_PACKET:
				ObjectHandler.handleOption(player, stream, 3);
				return;
			case OBJECT_CLICK_4_PACKET:
				ObjectHandler.handleOption(player, stream, 4);
				return;
			case OBJECT_CLICK_5_PACKET:
				ObjectHandler.handleOption(player, stream, 5);
				return;
			case OBJECT_EXAMINE_PACKET:
				ObjectHandler.handleOption(player, stream, -1);
				return;
		}
	}
	
	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.OBJECT_CLICK_1_PACKET, ClientPacket.OBJECT_CLICK_2_PACKET, ClientPacket.OBJECT_CLICK_3_PACKET, ClientPacket.OBJECT_CLICK_4_PACKET, ClientPacket.OBJECT_CLICK_5_PACKET, ClientPacket.OBJECT_EXAMINE_PACKET };
	}
	
}
