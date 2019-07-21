package com.feather.net.packets.incoming.impl;

import com.feather.game.player.Player;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;

public class KeyPressedPacket extends AbstractLogicPacket {
	
	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		int length = stream.getLength();
		
		System.out.println("KPP Length: " + length);
		
		byte[] data = new byte[length];
		
		stream.getBytes(data, 0, length);
		
		for(int i = 0; i < length; i++) {
			System.out.println("KPP [" + i + "]: " + data[i]);
		}
		
		System.out.println("Key Pressed: " + Character.toString((char)data[0]));
	}
	
	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.KEY_PRESSED_PACKET };
	}
	
}
