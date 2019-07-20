package com.feather.net.packets.incoming.impl;

import com.feather.game.World;
import com.feather.game.npc.NPC;
import com.feather.game.player.Player;
import com.feather.game.player.actions.PlayerCombat;
import com.feather.game.player.actions.PlayerFollow;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;
import com.feather.utils.Utils;

public class PlayerOptionPacket extends AbstractLogicPacket {
	
	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		switch(packet.getId()) {
			case PLAYER_OPTION_1_PACKET:
			{
				if(!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead()) {
					return;
				}
				@SuppressWarnings("unused")
				boolean unknown = stream.readByte() == 1;
				int playerIndex = stream.readUnsignedShortLE128();
				Player p2 = World.getPlayers().get(playerIndex);
				if(p2 == null || p2.isDead() || p2.hasFinished() || !player.getMapRegionsIds().contains(p2.getRegionId())) {
					return;
				}
				if(player.getLockDelay() > Utils.currentTimeMillis() || !player.getControlerManager().canPlayerOption1(
						p2)) {
					return;
				}
				if(!player.isCanPvp()) {
					return;
				}
				if(!player.getControlerManager().canAttack(p2)) {
					return;
				}
				
				if(!player.isCanPvp() || !p2.isCanPvp()) {
					player.getPackets().sendGameMessage("You can only attack players in a player-vs-player area.");
					return;
				}
				if(!p2.isAtMultiArea() || !player.isAtMultiArea()) {
					if(player.getAttackedBy() != p2 && player.getAttackedByDelay() > Utils.currentTimeMillis()) {
						player.getPackets().sendGameMessage("You are already in combat.");
						return;
					}
					if(p2.getAttackedBy() != player && p2.getAttackedByDelay() > Utils.currentTimeMillis()) {
						if(p2.getAttackedBy() instanceof NPC) {
							p2.setAttackedBy(player); // changes enemy to player,
							// player has priority over
							// npc on single areas
						} else {
							player.getPackets().sendGameMessage("That player is already in combat.");
							return;
						}
					}
				}
				player.stopAll(false);
				player.getActionManager().setAction(new PlayerCombat(p2));
				return;
			}
			case PLAYER_OPTION_2_PACKET:
			{
				if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
						|| player.isDead())
					return;
				@SuppressWarnings("unused")
				boolean unknown = stream.readByte() == 1;
				int playerIndex = stream.readUnsignedShortLE128();
				Player p2 = World.getPlayers().get(playerIndex);
				if (p2 == null || p2.isDead() || p2.hasFinished()
						|| !player.getMapRegionsIds().contains(p2.getRegionId()))
					return;
				if (player.getLockDelay() > Utils.currentTimeMillis())
					return;
				player.stopAll(false);
				player.getActionManager().setAction(new PlayerFollow(p2));
				return;
			}
			case PLAYER_OPTION_4_PACKET:
			{
				boolean unknown = stream.readByte() == 1;
				int playerIndex = stream.readUnsignedShortLE128();
				Player p2 = World.getPlayers().get(playerIndex);
				if(p2 == null || p2.isDead() || p2.hasFinished() || !player.getMapRegionsIds().contains(p2.getRegionId())) {
					return;
				}
				if(player.getLockDelay() > Utils.currentTimeMillis()) {
					return;
				}
				player.stopAll(false);
				if(player.isCantTrade()) {
					player.getPackets().sendGameMessage("You are busy.");
					return;
				}
				if(p2.getInterfaceManager().containsScreenInter() || p2.isCantTrade()) {
					player.getPackets().sendGameMessage("The other player is busy.");
					return;
				}
				if(!p2.withinDistance(player, 14)) {
					player.getPackets().sendGameMessage("Unable to find player " + p2.getDisplayName());
					return;
				}
				
				if(p2.getTemporaryAttributtes().get("TradeTarget") == player) {
					p2.getTemporaryAttributtes().remove("TradeTarget");
					player.getTrade().openTrade(p2);
					p2.getTrade().openTrade(player);
					return;
				}
				player.getTemporaryAttributtes().put("TradeTarget", p2);
				player.getPackets().sendGameMessage("Sending " + p2.getDisplayName() + " a trade request...");
				p2.getPackets().sendTradeRequestMessage(player);
				return;
			}
			case PLAYER_OPTION_5_PACKET:
				break;
			case PLAYER_OPTION_6_PACKET:
				break;
			case PLAYER_OPTION_7_PACKET:
				break;
			case PLAYER_OPTION_9_PACKET:
				break;
		}
	
	}
	
	@Override
	public ClientPacket[] getHandledPackets() {
	    return new ClientPacket[] { ClientPacket.PLAYER_OPTION_1_PACKET, ClientPacket.PLAYER_OPTION_2_PACKET, ClientPacket.PLAYER_OPTION_4_PACKET, ClientPacket.PLAYER_OPTION_5_PACKET, ClientPacket.PLAYER_OPTION_6_PACKET, ClientPacket.PLAYER_OPTION_7_PACKET, ClientPacket.PLAYER_OPTION_9_PACKET };
	}
	
}
