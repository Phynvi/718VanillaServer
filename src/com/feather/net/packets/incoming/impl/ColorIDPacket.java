package com.feather.net.packets.incoming.impl;

import com.feather.game.player.Player;
import com.feather.game.player.content.SkillCapeCustomizer;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;

public class ColorIDPacket extends AbstractLogicPacket {
	
	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		if(!player.hasStarted())
			return;
		int colorId = stream.readUnsignedShort();
		if(player.getTemporaryAttributtes().get("SkillcapeCustomize") != null)
			SkillCapeCustomizer.handleSkillCapeCustomizerColor(player, colorId);
	}
	
	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.COLOR_ID_PACKET };
	}
	
}
