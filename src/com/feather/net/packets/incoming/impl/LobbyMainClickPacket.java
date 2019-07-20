package com.feather.net.packets.incoming.impl;

import com.feather.game.player.Player;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;

public class LobbyMainClickPacket extends AbstractLogicPacket {
	
	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		int idk1 = stream.readShort();
		String idk2 = stream.readString();
		String idk3 = stream.readString();
		String idk4 = stream.readString();
		int idk5 = stream.readByte();
		if(idk3.equalsIgnoreCase("account_settings.ws?mod=recoveries")) {
			// open recover question link
		} else if(idk3.equalsIgnoreCase("account_settings.ws?mod=email")) {
			// opens players email
		} else if(idk3.equalsIgnoreCase("account_settings.ws?mod=messages")) {
			// opens players messages
		} else if(idk3.equalsIgnoreCase("purchasepopup.ws?externalName=rs")) {
			// open donation page
		}
	}
	
	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.LOBBY_MAIN_CLICK_PACKET };
	}
	
}
