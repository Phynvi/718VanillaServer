package com.feather.net.packets.incoming.impl;

import com.feather.game.player.Player;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;

public class WorldMapClick extends AbstractLogicPacket {
	
	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		int coordinateHash = stream.readInt();
		int x = coordinateHash >> 14;
		int y = coordinateHash & 0x3fff;
		int plane = coordinateHash >> 28;
		Integer hash = (Integer) player.getTemporaryAttributtes().get("worldHash");
		if(hash == null || coordinateHash != hash)
			player.getTemporaryAttributtes().put("worldHash", coordinateHash);
		else {
			player.getTemporaryAttributtes().remove("worldHash");
			player.getHintIconsManager().addHintIcon(x, y, plane, 20, 0, 2, -1, true);
			player.getPackets().sendConfig(1159, coordinateHash);
		}
	}
	
	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.WORLD_MAP_CLICK };
	}
	
}
