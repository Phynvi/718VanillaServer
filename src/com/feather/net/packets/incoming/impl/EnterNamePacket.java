package com.feather.net.packets.incoming.impl;

import com.feather.game.World;
import com.feather.game.minigames.clanwars.ClanWars;
import com.feather.game.player.Player;
import com.feather.game.player.content.Vote;
import com.feather.game.player.content.Notes.Note;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;
import com.feather.utils.DisplayNames;
import com.feather.utils.Utils;

public class EnterNamePacket extends AbstractLogicPacket {
	
	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		if(!player.isRunning() || player.isDead())
			return;
		String value = stream.readString();
		if(value.equals(""))
			return;
		if(player.getInterfaceManager().containsInterface(1108))
			player.getFriendsIgnores().setChatPrefix(value);
		else if(player.getTemporaryAttributtes().get("yellcolor") == Boolean.TRUE) {
			if(value.length() != 6) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"The HEX yell color you wanted to pick cannot be longer and shorter then 6.");
			} else if(Utils.containsInvalidCharacter(value) || value.contains("_")) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"The requested yell color can only contain numeric and regular characters.");
			} else {
				player.setYellColor(value);
				player.getDialogueManager().startDialogue("SimpleMessage",
						"Your yell color has been changed to <col=" + player.getYellColor() + ">" + player
								.getYellColor() + "</col>.");
			}
			player.getTemporaryAttributtes().put("yellcolor", Boolean.FALSE);
		} else if(player.getTemporaryAttributtes().get("entering_note") == Boolean.TRUE) {
			player.getNotes().add(new Note(value, 1));
			player.getNotes().refresh();
			player.getTemporaryAttributtes().put("entering_note", Boolean.FALSE);
			return;
		} else if(player.getTemporaryAttributtes().get("editing_note") == Boolean.TRUE) {
			Note note = (Note) player.getTemporaryAttributtes().get("curNote");
			player.getNotes().getNotes().get(player.getNotes().getNotes().indexOf(note));
			player.getNotes().refresh();
			player.getTemporaryAttributtes().put("editing_note", Boolean.FALSE);
		} else if(player.getTemporaryAttributtes().get("view_name") == Boolean.TRUE) {
			player.getTemporaryAttributtes().remove("view_name");
			Player other = World.getPlayerByDisplayName(value);
			if(other == null) {
				player.getPackets().sendGameMessage("Couldn't find player.");
				return;
			}
			ClanWars clan = other.getCurrentFriendChat() != null ? other.getCurrentFriendChat().getClanWars()
					: null;
			if(clan == null) {
				player.getPackets().sendGameMessage("This player's clan is not in war.");
				return;
			}
			if(clan.getSecondTeam().getOwnerDisplayName() != other.getCurrentFriendChat().getOwnerDisplayName()) {
				player.getTemporaryAttributtes().put("view_prefix", 1);
			}
			player.getTemporaryAttributtes().put("view_clan", clan);
			ClanWars.enter(player);
		} else if(player.getTemporaryAttributtes().remove("setdisplay") != null) {
			if(Utils.invalidAccountName(Utils.formatPlayerNameForProtocol(value))) {
				player.getPackets().sendGameMessage("Invalid name!");
				return;
			}
			if(!DisplayNames.setDisplayName(player, value)) {
				player.getPackets().sendGameMessage("Name already in use!");
				return;
			}
			player.getPackets().sendGameMessage("Changed display name!");
		} else if(player.getTemporaryAttributtes().remove("checkvoteinput") != null)
			Vote.checkVote(player, value);
	}
	
	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.ENTER_NAME_PACKET };
	}
	
}
