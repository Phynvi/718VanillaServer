package com.feather.net.packets.incoming.impl;

import com.feather.game.World;
import com.feather.game.npc.NPC;
import com.feather.game.npc.familiar.Familiar;
import com.feather.game.player.Player;
import com.feather.game.player.actions.PlayerCombat;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;
import com.feather.utils.Utils;

public class AttackNPCPacket extends AbstractLogicPacket {

	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		if(!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead()) {
			return;
		}
		if(player.getLockDelay() > Utils.currentTimeMillis()) {
			return;
		}
		int npcIndex = stream.readUnsignedShort128();
		boolean forceRun = stream.read128Byte() == 1;
		if(forceRun) {
			player.setRun(forceRun);
		}
		NPC npc = World.getNPCs().get(npcIndex);
		if(npc == null || npc.isDead() || npc.hasFinished() || !player.getMapRegionsIds().contains(npc
				.getRegionId()) || !npc.getDefinitions().hasAttackOption()) {
			return;
		}
		if(!player.getControlerManager().canAttack(npc)) {
			return;
		}
		if(npc instanceof Familiar) {
			Familiar familiar = (Familiar) npc;
			if(familiar == player.getFamiliar()) {
				player.getPackets().sendGameMessage("You can't attack your own familiar.");
				return;
			}
			if(!familiar.canAttack(player)) {
				player.getPackets().sendGameMessage("You can't attack this npc.");
				return;
			}
		} else if(!npc.isForceMultiAttacked()) {
			if(!npc.isAtMultiArea() || !player.isAtMultiArea()) {
				if(player.getAttackedBy() != npc && player.getAttackedByDelay() > Utils.currentTimeMillis()) {
					player.getPackets().sendGameMessage("You are already in combat.");
					return;
				}
				if(npc.getAttackedBy() != player && npc.getAttackedByDelay() > Utils.currentTimeMillis()) {
					player.getPackets().sendGameMessage("This npc is already in combat.");
					return;
				}
			}
		}
		player.stopAll(false);
		player.getActionManager().setAction(new PlayerCombat(npc));
	}

	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.ATTACK_NPC_PACKET };
	}
	
}
