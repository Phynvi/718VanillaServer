package com.feather.net.packets.incoming.impl;

import com.feather.game.World;
import com.feather.game.player.Player;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;
import com.feather.utils.Utils;
import com.feather.utils.huffman.Huffman;

public class SendFriendMessagePacket extends AbstractLogicPacket {

	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		if(!player.hasStarted() && !World.containsLobbyPlayer(player.getUsername()))
			return;
		if(player.getMuted() > Utils.currentTimeMillis()) {
			player.getPackets().sendGameMessage("You temporary muted. Recheck in 48 hours.");
			return;
		}
		String username = stream.readString();
		Player p2 = World.getPlayerByDisplayName(username);
		if(p2 == null) {
			p2 = World.getLobbyPlayerByDisplayName(username); // getLobbyPlayerByDisplayName
			if(p2 == null) {
				return;
			}
		}
		
		player.getFriendsIgnores().sendMessage(p2, Utils.fixChatMessage(Huffman.readEncryptedMessage(150, stream)));
	}

	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.PERSONAL_MESSAGE_PACKET };
	}
	
	
}
