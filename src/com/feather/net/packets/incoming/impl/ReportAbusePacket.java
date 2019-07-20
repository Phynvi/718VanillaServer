package com.feather.net.packets.incoming.impl;

import com.feather.game.player.Player;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;

public class ReportAbusePacket extends AbstractLogicPacket {
	
	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		if(!player.hasStarted())
			return;
		@SuppressWarnings("unused")
		String username = stream.readString();
		@SuppressWarnings("unused")
		int type = stream.readUnsignedByte();
		@SuppressWarnings("unused")
		boolean mute = stream.readUnsignedByte() == 1;
		@SuppressWarnings("unused")
		String unknown2 = stream.readString();
		
		// TODO
	}
	
	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.REPORT_ABUSE_PACKET };
	}
	
}
