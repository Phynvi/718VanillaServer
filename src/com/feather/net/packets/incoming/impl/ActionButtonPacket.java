package com.feather.net.packets.incoming.impl;

import com.feather.game.player.Player;
import com.feather.io.InputStream;
import com.feather.net.decoders.handlers.ButtonHandler;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;

public class ActionButtonPacket extends AbstractLogicPacket {
	
	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		ButtonHandler.handleButtons(player, stream, packet.getId().getID());
	}
	
	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.ACTION_BUTTON_1_PACKET, ClientPacket.ACTION_BUTTON_2_PACKET, ClientPacket.ACTION_BUTTON_3_PACKET, ClientPacket.ACTION_BUTTON_4_PACKET, ClientPacket.ACTION_BUTTON_5_PACKET, ClientPacket.ACTION_BUTTON_6_PACKET, ClientPacket.ACTION_BUTTON_7_PACKET, ClientPacket.ACTION_BUTTON_8_PACKET, ClientPacket.ACTION_BUTTON_9_PACKET, ClientPacket.ACTION_BUTTON_10_PACKET };
	}
	
}
