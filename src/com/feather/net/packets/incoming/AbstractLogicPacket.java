package com.feather.net.packets.incoming;

import com.feather.game.player.Player;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;

public abstract class AbstractLogicPacket extends AbstractPacket {
	public abstract void onPacketReceived(Player player, LogicPacket packet, InputStream stream);
}
