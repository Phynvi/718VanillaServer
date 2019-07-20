package com.feather.net.packets.incoming.impl;

import com.feather.game.World;
import com.feather.game.player.Player;
import com.feather.game.player.chat.FriendChatsManager;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;

public class JoinFriendChatPacket extends AbstractLogicPacket {
	
	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		if(!player.hasStarted() && !World.containsLobbyPlayer(player.getUsername()))
			return;
		FriendChatsManager.joinChat(stream.readString(), player);
	}
	
	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.JOIN_FRIEND_CHAT_PACKET };
	}
	
}
