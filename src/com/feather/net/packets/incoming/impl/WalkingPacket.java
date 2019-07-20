package com.feather.net.packets.incoming.impl;

import com.feather.game.player.Player;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;

public class WalkingPacket extends AbstractLogicPacket {

	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		if(!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead()) {
			return;
		}
		
		if(player.isLockDelayed()) {
			return;
		}
		if(player.isFrozen()) {
			player.getPackets().sendGameMessage("A magical force prevents you from moving.");
			return;
		}
		
		int length = stream.getLength();
		
		/*
		 * if (packetId == MINI_WALKING_PACKET) length -= 13;
		 */
		int baseX = stream.readUnsignedShort128();
		boolean forceRun = stream.readUnsigned128Byte() == 1;
		int baseY = stream.readUnsignedShort128();
		int steps = (length - 5) / 2;
		if(steps > 25) {
			steps = 25;
		}
		player.stopAll();
		if(forceRun) {
			player.setRun(forceRun);
		}
		for(int step = 0; step < steps; step++) {
			if(!player.addWalkSteps(baseX + stream.readUnsignedByte(), baseY + stream.readUnsignedByte(), 25, true)) {
				break;
			}
		}
	}

	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.WALKING_PACKET, ClientPacket.MINI_WALKING_PACKET };
	}
	
}
