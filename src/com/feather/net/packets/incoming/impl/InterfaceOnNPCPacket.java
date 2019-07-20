package com.feather.net.packets.incoming.impl;

import com.feather.Settings;
import com.feather.game.World;
import com.feather.game.WorldTile;
import com.feather.game.item.Item;
import com.feather.game.npc.NPC;
import com.feather.game.npc.familiar.Familiar;
import com.feather.game.npc.familiar.Familiar.SpecialAttack;
import com.feather.game.player.Inventory;
import com.feather.game.player.Player;
import com.feather.game.player.actions.PlayerCombat;
import com.feather.game.player.actions.Summoning;
import com.feather.game.player.skills.magic.Magic;
import com.feather.io.InputStream;
import com.feather.net.decoders.handlers.InventoryOptionsHandler;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;
import com.feather.utils.Utils;

public class InterfaceOnNPCPacket extends AbstractLogicPacket {
	
	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		if(!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
			return;
		if(player.getLockDelay() > Utils.currentTimeMillis())
			return;
		@SuppressWarnings("unused")
		boolean unknown = stream.readByte() == 1;
		int interfaceHash = stream.readInt();
		int npcIndex = stream.readUnsignedShortLE();
		int interfaceSlot = stream.readUnsignedShortLE128();
		@SuppressWarnings("unused")
		int junk2 = stream.readUnsignedShortLE();
		int interfaceId = interfaceHash >> 16;
		int componentId = interfaceHash - (interfaceId << 16);
		if(Utils.getInterfaceDefinitionsSize() <= interfaceId)
			return;
		if(!player.getInterfaceManager().containsInterface(interfaceId))
			return;
		if(componentId == 65535)
			componentId = -1;
		if(componentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId)
			return;
		NPC npc = World.getNPCs().get(npcIndex);
		if(npc == null || npc.isDead() || npc.hasFinished() || !player.getMapRegionsIds().contains(npc
				.getRegionId()))
			return;
		player.stopAll(false);
		if(interfaceId != Inventory.INVENTORY_INTERFACE) {
			if(!npc.getDefinitions().hasAttackOption()) {
				player.getPackets().sendGameMessage("You can't attack this npc.");
				return;
			}
		}
		switch (interfaceId) {
			case Inventory.INVENTORY_INTERFACE:
				Item item = player.getInventory().getItem(interfaceSlot);
				if(item == null || !player.getControlerManager().processItemOnNPC(npc, item))
					return;
				InventoryOptionsHandler.handleItemOnNPC(player, npc, item);
				break;
			case 1165:
				Summoning.attackDreadnipTarget(npc, player);
				break;
			case 662:
			case 747:
				if(player.getFamiliar() == null)
					return;
				player.resetWalkSteps();
				if((interfaceId == 747 && componentId == 15) || (interfaceId == 662 && componentId == 65)
						|| (interfaceId == 662 && componentId == 74) || interfaceId == 747 && componentId == 18
						|| interfaceId == 747 && componentId == 24) {
					if((interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 18)) {
						if(player.getFamiliar().getSpecialAttack() != SpecialAttack.ENTITY)
							return;
					}
					if(npc instanceof Familiar) {
						Familiar familiar = (Familiar) npc;
						if(familiar == player.getFamiliar()) {
							player.getPackets().sendGameMessage("You can't attack your own familiar.");
							return;
						}
						if(!player.getFamiliar().canAttack(familiar.getOwner())) {
							player.getPackets().sendGameMessage(
									"You can only attack players in a player-vs-player area.");
							return;
						}
					}
					if(!player.getFamiliar().canAttack(npc)) {
						player.getPackets().sendGameMessage("You can only use your familiar in a multi-zone area.");
						return;
					} else {
						player.getFamiliar().setSpecial(interfaceId == 662 && componentId == 74
								|| interfaceId == 747 && componentId == 18);
						player.getFamiliar().setTarget(npc);
					}
				}
				break;
			case 193:
				switch (componentId) {
					case 28:
					case 32:
					case 24:
					case 20:
					case 30:
					case 34:
					case 26:
					case 22:
					case 29:
					case 33:
					case 25:
					case 21:
					case 31:
					case 35:
					case 27:
					case 23:
						if(Magic.checkCombatSpell(player, componentId, 1, false)) {
							player.setNextFaceWorldTile(new WorldTile(npc.getCoordFaceX(npc.getSize()), npc
									.getCoordFaceY(npc.getSize()), npc.getPlane()));
							if(!player.getControlerManager().canAttack(npc))
								return;
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
									if(player.getAttackedBy() != npc && player.getAttackedByDelay() > Utils
											.currentTimeMillis()) {
										player.getPackets().sendGameMessage("You are already in combat.");
										return;
									}
									if(npc.getAttackedBy() != player && npc.getAttackedByDelay() > Utils
											.currentTimeMillis()) {
										player.getPackets().sendGameMessage("This npc is already in combat.");
										return;
									}
								}
							}
							player.getActionManager().setAction(new PlayerCombat(npc));
						}
						break;
				}
			case 192:
				switch (componentId) {
					case 25: // air strike
					case 28: // water strike
					case 30: // earth strike
					case 32: // fire strike
					case 34: // air bolt
					case 39: // water bolt
					case 42: // earth bolt
					case 45: // fire bolt
					case 49: // air blast
					case 52: // water blast
					case 58: // earth blast
					case 63: // fire blast
					case 70: // air wave
					case 73: // water wave
					case 77: // earth wave
					case 80: // fire wave
					case 84: // air surge
					case 87: // water surge
					case 89: // earth surge
					case 66: // Sara Strike
					case 67: // Guthix Claws
					case 68: // Flame of Zammy
					case 93:
					case 91: // fire surge
					case 99: // storm of Armadyl
					case 36: // bind
					case 55: // snare
					case 81: // entangle
						if(Magic.checkCombatSpell(player, componentId, 1, false)) {
							player.setNextFaceWorldTile(new WorldTile(npc.getCoordFaceX(npc.getSize()), npc
									.getCoordFaceY(npc.getSize()), npc.getPlane()));
							if(!player.getControlerManager().canAttack(npc))
								return;
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
									if(player.getAttackedBy() != npc && player.getAttackedByDelay() > Utils
											.currentTimeMillis()) {
										player.getPackets().sendGameMessage("You are already in combat.");
										return;
									}
									if(npc.getAttackedBy() != player && npc.getAttackedByDelay() > Utils
											.currentTimeMillis()) {
										player.getPackets().sendGameMessage("This npc is already in combat.");
										return;
									}
								}
							}
							player.getActionManager().setAction(new PlayerCombat(npc));
						}
						break;
				}
				break;
		}
		if(Settings.DEBUG)
			System.out.println("Spell:" + componentId);
	}
	
	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.INTERFACE_ON_NPC };
	}
	
}
