package com.feather.net.packets.incoming.impl;

import com.feather.Settings;
import com.feather.game.World;
import com.feather.game.WorldTile;
import com.feather.game.item.Item;
import com.feather.game.npc.NPC;
import com.feather.game.npc.familiar.Familiar.SpecialAttack;
import com.feather.game.player.Inventory;
import com.feather.game.player.LendingManager;
import com.feather.game.player.Player;
import com.feather.game.player.actions.PlayerCombat;
import com.feather.game.player.content.Lend;
import com.feather.game.player.skills.magic.Magic;
import com.feather.io.InputStream;
import com.feather.net.decoders.handlers.InventoryOptionsHandler;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;
import com.feather.utils.Utils;

public class InterfaceOnPlayerPacket extends AbstractLogicPacket {
	
	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		if(!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
			return;
		if(player.getLockDelay() > Utils.currentTimeMillis())
			return;
		@SuppressWarnings("unused")
		int junk1 = stream.readUnsignedShort();
		int playerIndex = stream.readUnsignedShort();
		int interfaceHash = stream.readIntV2();
		@SuppressWarnings("unused")
		int junk2 = stream.readUnsignedShortLE128();
		@SuppressWarnings("unused")
		boolean unknown = stream.read128Byte() == 1;
		int interfaceId = interfaceHash >> 16;
		int componentId = interfaceHash - (interfaceId << 16);
		int slot = stream.readUnsignedShortLE128();
		if(Utils.getInterfaceDefinitionsSize() <= interfaceId)
			return;
		if(!player.getInterfaceManager().containsInterface(interfaceId))
			return;
		if(componentId == 65535)
			componentId = -1;
		if(componentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId)
			return;
		Player p2 = World.getPlayers().get(playerIndex);
		if(p2 == null || p2.isDead() || p2.hasFinished() || !player.getMapRegionsIds().contains(p2.getRegionId()))
			return;
		player.stopAll(false);
		if(interfaceId == 679) {
			Item item = player.getInventory().getItems().get(slot);
			if(item == null)
				return;
			if(LendingManager.isLendedItem(player, item)) {
				Lend lend = LendingManager.getLend(player);
				if(lend == null)
					return;
				if(!lend.getLender().equals(p2.getUsername())) {
					player.getPackets().sendGameMessage("You can't give your lent item to a stranger...");
					return;
				}
				player.getDialogueManager().startDialogue("LendReturn", lend);
				return;
			}
			player.getPackets().sendGameMessage("Nothing interesting happens.");
		}
		
		switch (interfaceId) {
			case 662:
			case 747:
				if(player.getFamiliar() == null)
					return;
				player.resetWalkSteps();
				if((interfaceId == 747 && componentId == 15) || (interfaceId == 662 && componentId == 65)
						|| (interfaceId == 662 && componentId == 74) || interfaceId == 747 && componentId == 18) {
					if((interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 24
							|| interfaceId == 747 && componentId == 18)) {
						if(player.getFamiliar().getSpecialAttack() != SpecialAttack.ENTITY)
							return;
					}
					if(!player.isCanPvp() || !p2.isCanPvp()) {
						player.getPackets().sendGameMessage(
								"You can only attack players in a player-vs-player area.");
						return;
					}
					if(!player.getFamiliar().canAttack(p2)) {
						player.getPackets().sendGameMessage("You can only use your familiar in a multi-zone area.");
						return;
					} else {
						player.getFamiliar().setSpecial(interfaceId == 662 && componentId == 74
								|| interfaceId == 747 && componentId == 18);
						player.getFamiliar().setTarget(p2);
					}
				}
				break;
			case Inventory.INVENTORY_INTERFACE:// Item on player
				if(!player.getControlerManager().processItemOnPlayer(p2, junk1))
					return;
				InventoryOptionsHandler.handleItemOnPlayer(player, p2, junk1);
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
							player.setNextFaceWorldTile(new WorldTile(p2.getCoordFaceX(p2.getSize()), p2
									.getCoordFaceY(p2.getSize()), p2.getPlane()));
							if(!player.getControlerManager().canAttack(p2))
								return;
							if(!player.isCanPvp() || !p2.isCanPvp()) {
								player.getPackets().sendGameMessage(
										"You can only attack players in a player-vs-player area.");
								return;
							}
							if(!p2.isAtMultiArea() || !player.isAtMultiArea()) {
								if(player.getAttackedBy() != p2 && player.getAttackedByDelay() > Utils
										.currentTimeMillis()) {
									player.getPackets().sendGameMessage("That " + (player
											.getAttackedBy() instanceof Player ? "player" : "npc")
											+ " is already in combat.");
									return;
								}
								if(p2.getAttackedBy() != player && p2.getAttackedByDelay() > Utils
										.currentTimeMillis()) {
									if(p2.getAttackedBy() instanceof NPC) {
										p2.setAttackedBy(player); // changes enemy
										// to player,
										// player has
										// priority over
										// npc on single
										// areas
									} else {
										player.getPackets().sendGameMessage("That player is already in combat.");
										return;
									}
								}
							}
							player.getActionManager().setAction(new PlayerCombat(p2));
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
					case 86: // teleblock
					case 84: // air surge
					case 87: // water surge
					case 89: // earth surge
					case 91: // fire surge
					case 99: // storm of armadyl
					case 36: // bind
					case 66: // Sara Strike
					case 67: // Guthix Claws
					case 68: // Flame of Zammy
					case 55: // snare
					case 81: // entangle
						if(Magic.checkCombatSpell(player, componentId, 1, false)) {
							player.setNextFaceWorldTile(new WorldTile(p2.getCoordFaceX(p2.getSize()), p2
									.getCoordFaceY(p2.getSize()), p2.getPlane()));
							if(!player.getControlerManager().canAttack(p2))
								return;
							if(!player.isCanPvp() || !p2.isCanPvp()) {
								player.getPackets().sendGameMessage(
										"You can only attack players in a player-vs-player area.");
								return;
							}
							if(!p2.isAtMultiArea() || !player.isAtMultiArea()) {
								if(player.getAttackedBy() != p2 && player.getAttackedByDelay() > Utils
										.currentTimeMillis()) {
									player.getPackets().sendGameMessage("That " + (player
											.getAttackedBy() instanceof Player ? "player" : "npc")
											+ " is already in combat.");
									return;
								}
								if(p2.getAttackedBy() != player && p2.getAttackedByDelay() > Utils
										.currentTimeMillis()) {
									if(p2.getAttackedBy() instanceof NPC) {
										p2.setAttackedBy(player); // changes enemy
										// to player,
										// player has
										// priority over
										// npc on single
										// areas
									} else {
										player.getPackets().sendGameMessage("That player is already in combat.");
										return;
									}
								}
							}
							player.getActionManager().setAction(new PlayerCombat(p2));
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
		return new ClientPacket[] { ClientPacket.INTERFACE_ON_PLAYER };
	}
	
}
