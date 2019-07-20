package com.feather.net.packets.incoming.impl;

import com.feather.game.World;
import com.feather.game.player.Player;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;

public class AddIgnorePacket extends AbstractLogicPacket {
	
	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		if(!player.hasStarted() && !World.containsLobbyPlayer(player.getUsername()))
			return;
		player.getFriendsIgnores().addIgnore(stream.readString(), stream.readUnsignedByte() == 1);
	}
	
	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.ADD_IGNORE_PACKET };
	}
	
}
