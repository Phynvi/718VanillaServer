package com.feather.net.packets.incoming.impl;

import com.feather.game.player.Player;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;

public class ScreenPacket extends AbstractLogicPacket {
	
	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		int displayMode = stream.readUnsignedByte();
		player.setScreenWidth(stream.readUnsignedShort());
		player.setScreenHeight(stream.readUnsignedShort());
		@SuppressWarnings("unused")
		boolean switchScreenMode = stream.readUnsignedByte() == 1;
		if(!player.hasStarted() || player.hasFinished() || displayMode == player.getDisplayMode() || !player
				.getInterfaceManager().containsInterface(742))
			return;
		player.setDisplayMode(displayMode);
		player.getInterfaceManager().removeAll();
		player.getInterfaceManager().sendInterfaces();
		player.getInterfaceManager().sendInterface(742);
	}
	
	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.SCREEN_PACKET };
	}
	
}
