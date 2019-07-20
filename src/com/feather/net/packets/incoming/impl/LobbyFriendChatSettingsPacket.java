package com.feather.net.packets.incoming.impl;

import com.feather.game.player.Player;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;

public class LobbyFriendChatSettingsPacket extends AbstractLogicPacket {
	
	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		int idk = stream.readByte();
		int status = stream.readByte();
		int idk3 = stream.readByte();
		player.getFriendsIgnores().setPrivateStatus(status);
	}
	
	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.LOBBY_FRIEND_CHAT_SETTINGS };
	}
	
}
