package com.feather.net.packets.incoming.impl;

import com.feather.game.player.Player;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;

public class CloseInterfacePacket extends AbstractLogicPacket {

	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		if(player.hasStarted() && !player.hasFinished() && !player.isRunning()) { // used for old welcome screen
			player.run();
			return;
		}
		player.stopAll();
	}

	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.CLOSE_INTERFACE_PACKET };
	}
	
	
}
