package com.feather.net.packets.incoming.impl;

import com.feather.Settings;
import com.feather.game.player.Player;
import com.feather.game.player.content.Commands;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;
import com.feather.utils.Logger;

public class DeveloperConsolePacket extends AbstractLogicPacket {

	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		if(!player.isRunning())
			return;
		boolean clientCommand = stream.readUnsignedByte() == 1;
		@SuppressWarnings("unused")
		boolean unknown = stream.readUnsignedByte() == 1;
		String command = stream.readString();
		if(!Commands.processCommand(player, command, true, clientCommand) && Settings.DEBUG)
			Logger.log(this, "Command: " + command);
	}

	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.DEVELOPER_CONSOLE_PACKET };
	}
	
}
