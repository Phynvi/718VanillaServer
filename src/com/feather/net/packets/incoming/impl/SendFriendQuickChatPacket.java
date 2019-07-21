package com.feather.net.packets.incoming.impl;

import com.feather.game.World;
import com.feather.game.player.Player;
import com.feather.game.player.chat.QuickChatMessage;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;
import com.feather.utils.Utils;

public class SendFriendQuickChatPacket extends AbstractLogicPacket {

	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		if(!player.hasStarted() && !World.containsLobbyPlayer(player.getUsername()))
			return;
		String username = stream.readString();
		int fileId = stream.readUnsignedShort();
		byte[] data = null;
		if(packet.getData().length > 3 + username.length()) {
			data = new byte[packet.getData().length - (3 + username.length())];
			stream.readBytes(data);
		}
		data = Utils.completeQuickMessage(player, fileId, data);
		Player p2 = World.getPlayerByDisplayName(username);
		if(p2 == null) {
			p2 = World.getLobbyPlayerByDisplayName(username); // getLobbyPlayerByDisplayName
			if(p2 == null) {
				return;
			}
		}
		player.getFriendsIgnores().sendQuickChatMessage(p2, new QuickChatMessage(fileId, data));
	}

	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.FRIEND_QUICK_CHAT_PACKET };
	}
	
}
