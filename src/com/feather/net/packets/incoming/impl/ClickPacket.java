package com.feather.net.packets.incoming.impl;

import com.feather.game.player.Player;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;

public class ClickPacket extends AbstractLogicPacket {
	
	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		int mouseHash = stream.readShortLE128();
		int mouseButton = mouseHash >> 15;
		int time = mouseHash - (mouseButton << 15); // time
		int positionHash = stream.readIntV1();
		int y = positionHash >> 16; // y;
		int x = positionHash - (y << 16); // x
		@SuppressWarnings("unused")
		boolean clicked;
		// mass click or stupid autoclicker, lets stop lagg
		if(time <= 1 || x < 0 || x > player.getScreenWidth() || y < 0 || y > player.getScreenHeight()) {
			// player.getSession().getChannel().close();
			clicked = false;
			return;
		}
		clicked = true;
	}
	
	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.CLICK_PACKET };
	}
	
}
