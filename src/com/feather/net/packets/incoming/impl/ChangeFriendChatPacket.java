package com.feather.net.packets.incoming.impl;

import com.feather.game.player.Player;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;

public class ChangeFriendChatPacket extends AbstractLogicPacket {
	
	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		if(!player.hasStarted() || !player.getInterfaceManager().containsInterface(1108))
			return;
		player.getFriendsIgnores().changeRank(stream.readString(), stream.readUnsignedByte128());
	}
	
	@Override
	public ClientPacket[] getHandledPackets() {
		// TODO Auto-generated method stub
		return new ClientPacket[] { ClientPacket.CHANGE_FRIEND_CHAT_PACKET };
	}
	
}
