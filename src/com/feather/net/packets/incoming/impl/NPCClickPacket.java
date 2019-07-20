package com.feather.net.packets.incoming.impl;

import com.feather.game.player.Player;
import com.feather.io.InputStream;
import com.feather.net.decoders.handlers.NPCHandler;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;

public class NPCClickPacket extends AbstractLogicPacket {
	
	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		switch(packet.getId()) {
			case NPC_CLICK_1_PACKET:
			    NPCHandler.handleOption1(player, stream);
				return;
			case NPC_CLICK_2_PACKET:
				NPCHandler.handleOption2(player, stream);
				return;
			case NPC_CLICK_3_PACKET:
				NPCHandler.handleOption3(player, stream);
				return;
			case NPC_CLICK_4_PACKET:
				//TODO
				return;
			case NPC_EXAMINE_PACKET:
				NPCHandler.handleExamine(player, stream);
				return;
		}
	}
	
	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.NPC_CLICK_1_PACKET, ClientPacket.NPC_CLICK_2_PACKET, ClientPacket.NPC_CLICK_3_PACKET, ClientPacket.NPC_CLICK_4_PACKET, ClientPacket.NPC_EXAMINE_PACKET };
	}
	
}
