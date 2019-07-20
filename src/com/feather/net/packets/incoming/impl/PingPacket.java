package com.feather.net.packets.incoming.impl;

import com.feather.game.player.Player;
import com.feather.io.InputStream;
import com.feather.io.OutputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.ServerPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;

public class PingPacket extends AbstractLogicPacket {
	
	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		OutputStream output_packet = new OutputStream(0);
		output_packet.writePacket(player, ServerPacket.PING_PACKET.getValue());
		player.getSession().write(output_packet);
	}
	
	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.PING_PACKET };
	}
	
}
