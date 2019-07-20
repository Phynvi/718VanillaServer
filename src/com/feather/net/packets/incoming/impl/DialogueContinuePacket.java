package com.feather.net.packets.incoming.impl;

import com.feather.Settings;
import com.feather.game.player.Player;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;
import com.feather.utils.Logger;
import com.feather.utils.Utils;

public class DialogueContinuePacket extends AbstractLogicPacket {
	
	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		int interfaceHash = stream.readInt();
		int junk = stream.readShort128();
		int interfaceId = interfaceHash >> 16;
		int buttonId = (interfaceHash & 0xFF);
		if(Utils.getInterfaceDefinitionsSize() <= interfaceId) {
			// hack, or server error or client error
			// player.getSession().getChannel().close();
			return;
		}
		if(!player.isRunning() || !player.getInterfaceManager().containsInterface(interfaceId))
			return;
		if(Settings.DEBUG)
			Logger.log(this, "Dialogue: " + interfaceId + ", " + buttonId + ", " + junk);
		int componentId = interfaceHash - (interfaceId << 16);
		player.getDialogueManager().continueDialogue(interfaceId, componentId);
	}
	
	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.DIALOGUE_CONTINUE_PACKET };
	}
	
}
