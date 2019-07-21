package com.feather.net.packets.incoming.impl;

import com.feather.game.player.Player;
import com.feather.game.player.chat.QuickChatMessage;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;
import com.feather.utils.Utils;

public class PublicQuickChatPacket extends AbstractLogicPacket {

	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		// TODO
		/*
		if(!player.hasStarted())
			return;
		if(player.getLastPublicMessage() > Utils.currentTimeMillis())
			return;
		player.setLastPublicMessage(Utils.currentTimeMillis() + 300);
		// just tells you which client script created packet
		@SuppressWarnings("unused")
		boolean secondClientScript = stream.readByte() == 1;// script 5059
		// or 5061
		int fileId = stream.readUnsignedShort();
		byte[] data = null;
		
		int length = packet.getData().length;
		
		if(length > 3) {
			data = new byte[length - 3];
			stream.readBytes(data);
		}
		data = Utils.completeQuickMessage(player, fileId, data);
		if(chatType == 0)
			player.sendPublicChatMessage(new QuickChatMessage(fileId, data));
		else if(chatType == 1)
			player.sendFriendsChannelQuickMessage(new QuickChatMessage(fileId, data));
		
		*/
	}

	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.FRIEND_QUICK_CHAT_PACKET };
	}
	
}
