package com.feather.net.packets.incoming.impl;

import com.feather.game.World;
import com.feather.game.player.Player;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;

public class AddFriendPacket extends AbstractLogicPacket {
	
	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		if(!player.hasStarted() && !World.containsLobbyPlayer(player.getUsername()))
			return;
		player.getFriendsIgnores().addFriend(stream.readString());
	}
	
	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.ADD_FRIEND_PACKET };
	}
	
}
