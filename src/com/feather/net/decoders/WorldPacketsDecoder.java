package com.feather.net.decoders;

import com.feather.Settings;
import com.feather.game.Animation;
import com.feather.game.Graphics;
import com.feather.game.World;
import com.feather.game.WorldObject;
import com.feather.game.WorldTile;
import com.feather.game.item.FloorItem;
import com.feather.game.item.Item;
import com.feather.game.minigames.clanwars.ClanWars;
import com.feather.game.minigames.creations.StealingCreation;
import com.feather.game.npc.NPC;
import com.feather.game.npc.familiar.Familiar;
import com.feather.game.npc.familiar.Familiar.SpecialAttack;
import com.feather.game.player.CoordsEvent;
import com.feather.game.player.Inventory;
import com.feather.game.player.LendingManager;
import com.feather.game.player.Player;
import com.feather.game.player.Skills;
import com.feather.game.player.actions.PlayerCombat;
import com.feather.game.player.actions.PlayerFollow;
import com.feather.game.player.actions.Summoning;
import com.feather.game.player.chat.FriendChatsManager;
import com.feather.game.player.chat.PublicChatMessage;
import com.feather.game.player.chat.QuickChatMessage;
import com.feather.game.player.content.Commands;
import com.feather.game.player.content.Lend;
import com.feather.game.player.content.Shop;
import com.feather.game.player.content.SkillCapeCustomizer;
import com.feather.game.player.content.Vote;
import com.feather.game.player.content.Notes.Note;
import com.feather.game.player.skills.magic.Magic;
import com.feather.io.InputStream;
import com.feather.io.OutputStream;
import com.feather.net.Session;
import com.feather.net.decoders.handlers.ButtonHandler;
import com.feather.net.decoders.handlers.InventoryOptionsHandler;
import com.feather.net.decoders.handlers.NPCHandler;
import com.feather.net.decoders.handlers.ObjectHandler;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.PacketManager;
import com.feather.utils.ChatFilter;
import com.feather.utils.DisplayNames;
import com.feather.utils.Logger;
import com.feather.utils.Utils;
import com.feather.utils.huffman.Huffman;

public final class WorldPacketsDecoder extends Decoder {
	
	private static final byte[] PACKET_SIZES = new byte[104];
	
	private final static int WALKING_PACKET = 8;
	private final static int MINI_WALKING_PACKET = 58;
	private final static int AFK_PACKET = -1;
	public final static int ACTION_BUTTON1_PACKET = 14;
	public final static int ACTION_BUTTON2_PACKET = 67;
	public final static int ACTION_BUTTON3_PACKET = 5;
	public final static int ACTION_BUTTON4_PACKET = 55;
	public final static int ACTION_BUTTON5_PACKET = 68;
	public final static int ACTION_BUTTON6_PACKET = 90;
	public final static int ACTION_BUTTON7_PACKET = 6;
	public final static int ACTION_BUTTON8_PACKET = 32;
	public final static int ACTION_BUTTON9_PACKET = 27;
	public final static int WORLD_MAP_CLICK = 38;
	private final static int WORLD_LIST_UPDATE = 87;
	//
	private final static int LOBBY_MAIN_CLICK_PACKET = 91;
	private final static int LOBBY_FRIEND_CHAT_SETTINGS = 79;
	public final static int ACTION_BUTTON10_PACKET = 96;
	public final static int RECEIVE_PACKET_COUNT_PACKET = 33;
	private final static int MAGIC_ON_ITEM_PACKET = -1;
	private final static int PLAYER_OPTION_4_PACKET = 17;
	private final static int MOVE_CAMERA_PACKET = 103;
	private final static int INTERFACE_ON_OBJECT = 37;
	private final static int CLICK_PACKET = -1;
	private final static int MOUVE_MOUSE_PACKET = -1;
	private final static int KEY_TYPED_PACKET = -1;
	private final static int CLOSE_INTERFACE_PACKET = 54;
	private final static int COMMANDS_PACKET = 60;
	private final static int ITEM_ON_ITEM_PACKET = 3;
	private final static int IN_OUT_SCREEN_PACKET = -1;
	private final static int DONE_LOADING_REGION_PACKET = 30;
	private final static int PING_PACKET = 21;
	private final static int SCREEN_PACKET = 98;
	private final static int CHAT_TYPE_PACKET = 83;
	private final static int CHAT_PACKET = 53;
	private final static int PUBLIC_QUICK_CHAT_PACKET = 86;
	private final static int ADD_FRIEND_PACKET = 89;
	private final static int ADD_IGNORE_PACKET = 4;
	private final static int REMOVE_IGNORE_PACKET = 73;
	private final static int JOIN_FRIEND_CHAT_PACKET = 36;
	private final static int CHANGE_FRIEND_CHAT_PACKET = 22;
	private final static int KICK_FRIEND_CHAT_PACKET = 74;
	private final static int REMOVE_FRIEND_PACKET = 24;
	private final static int SEND_FRIEND_MESSAGE_PACKET = 82;
	private final static int SEND_FRIEND_QUICK_CHAT_PACKET = 0;
	private final static int OBJECT_CLICK1_PACKET = 26;
	private final static int OBJECT_CLICK2_PACKET = 59;
	private final static int OBJECT_CLICK3_PACKET = 40;
	private final static int OBJECT_CLICK4_PACKET = 23;
	private final static int OBJECT_CLICK5_PACKET = 80;
	private final static int OBJECT_EXAMINE_PACKET = 25;
	private final static int NPC_CLICK1_PACKET = 31;
	private final static int NPC_CLICK2_PACKET = 101;
	private final static int NPC_CLICK3_PACKET = 34;
	private final static int ATTACK_NPC = 20;
	private final static int PLAYER_OPTION_1_PACKET = 42;
	private final static int PLAYER_OPTION_2_PACKET = 46;
	private final static int ITEM_TAKE_PACKET = 57;
	private final static int DIALOGUE_CONTINUE_PACKET = 72;
	private final static int ENTER_INTEGER_PACKET = 81;
	private final static int ENTER_NAME_PACKET = 29;
	private final static int ENTER_STRING_PACKET = -1;
	private final static int SWITCH_INTERFACE_ITEM_PACKET = 76;
	private final static int INTERFACE_ON_PLAYER = 50;
	private final static int INTERFACE_ON_NPC = 66;
	private final static int COLOR_ID_PACKET = 97;
	private static final int NPC_EXAMINE_PACKET = 9;
	private final static int REPORT_ABUSE_PACKET = -1;
	private final static int JOIN_CLAN_CHAT_PACKET = 133;
	
	static {
		loadPacketSizes();
	}
	
	public static void loadPacketSizes() {
		PACKET_SIZES[0] = -1;
		PACKET_SIZES[1] = -2;
		PACKET_SIZES[2] = -1;
		PACKET_SIZES[3] = 16;
		PACKET_SIZES[4] = -1;
		PACKET_SIZES[5] = 8;
		PACKET_SIZES[6] = 8;
		PACKET_SIZES[7] = 3;
		PACKET_SIZES[8] = -1;
		PACKET_SIZES[9] = 3;
		PACKET_SIZES[10] = -1;
		PACKET_SIZES[11] = -1;
		PACKET_SIZES[12] = -1;
		PACKET_SIZES[13] = 7;
		PACKET_SIZES[14] = 8;
		PACKET_SIZES[15] = 6;
		PACKET_SIZES[16] = 2;
		PACKET_SIZES[17] = 3;
		PACKET_SIZES[18] = -1;
		PACKET_SIZES[19] = -2;
		PACKET_SIZES[20] = 3;
		PACKET_SIZES[21] = 0;
		PACKET_SIZES[22] = -1;
		PACKET_SIZES[23] = 9;
		PACKET_SIZES[24] = -1;
		PACKET_SIZES[25] = 9;
		PACKET_SIZES[26] = 9;
		PACKET_SIZES[27] = 8;
		PACKET_SIZES[28] = 4;
		PACKET_SIZES[29] = -1;
		PACKET_SIZES[30] = 0;
		PACKET_SIZES[31] = 3;
		PACKET_SIZES[32] = 8;
		PACKET_SIZES[33] = 4;
		PACKET_SIZES[34] = 3;
		PACKET_SIZES[35] = -1;
		PACKET_SIZES[36] = -1;
		PACKET_SIZES[37] = 17;
		PACKET_SIZES[38] = 4;
		PACKET_SIZES[39] = 4;
		PACKET_SIZES[40] = 9;
		PACKET_SIZES[41] = -1;
		PACKET_SIZES[42] = 3;
		PACKET_SIZES[43] = 7;
		PACKET_SIZES[44] = -2;
		PACKET_SIZES[45] = 7;
		PACKET_SIZES[46] = 3;
		PACKET_SIZES[47] = 4;
		PACKET_SIZES[48] = -1;
		PACKET_SIZES[49] = 3;
		PACKET_SIZES[50] = 11;
		PACKET_SIZES[51] = 3;
		PACKET_SIZES[52] = -1;
		PACKET_SIZES[53] = -1;
		PACKET_SIZES[54] = 0;
		PACKET_SIZES[55] = 8;
		PACKET_SIZES[56] = 3;
		PACKET_SIZES[57] = 7;
		PACKET_SIZES[58] = -1;
		PACKET_SIZES[59] = 9;
		PACKET_SIZES[60] = -1;
		PACKET_SIZES[61] = 7;
		PACKET_SIZES[62] = 7;
		PACKET_SIZES[63] = 12;
		PACKET_SIZES[64] = 4;
		PACKET_SIZES[65] = 3;
		PACKET_SIZES[66] = 11;
		PACKET_SIZES[67] = 8;
		PACKET_SIZES[68] = 8;
		PACKET_SIZES[69] = 15;
		PACKET_SIZES[70] = 1;
		PACKET_SIZES[71] = 2;
		PACKET_SIZES[72] = 6;
		PACKET_SIZES[73] = -1;
		PACKET_SIZES[74] = -1;
		PACKET_SIZES[75] = -2;
		PACKET_SIZES[76] = 16;
		PACKET_SIZES[77] = 3;
		PACKET_SIZES[78] = 1;
		PACKET_SIZES[79] = 3;
		PACKET_SIZES[80] = 9;
		PACKET_SIZES[81] = 4;
		PACKET_SIZES[82] = -2;
		PACKET_SIZES[83] = 1;
		PACKET_SIZES[84] = 1;
		PACKET_SIZES[85] = 3;
		PACKET_SIZES[86] = -1;
		PACKET_SIZES[87] = 4;
		PACKET_SIZES[88] = 3;
		PACKET_SIZES[89] = -1;
		PACKET_SIZES[90] = 8;
		PACKET_SIZES[91] = -2;
		PACKET_SIZES[92] = -1;
		PACKET_SIZES[93] = -1;
		PACKET_SIZES[94] = 9;
		PACKET_SIZES[95] = -2;
		PACKET_SIZES[96] = 8;
		PACKET_SIZES[97] = 2;
		PACKET_SIZES[98] = 6;
		PACKET_SIZES[99] = 2;
		PACKET_SIZES[100] = -2;
		PACKET_SIZES[101] = 3;
		PACKET_SIZES[102] = 7;
		PACKET_SIZES[103] = 4;
	}
	
	private Player player;
	private int chatType;
	
	public WorldPacketsDecoder(Session session, Player player) {
		super(session);
		this.player = player;
	}
	
	@Override
	public void decode(InputStream stream) {
		while (stream.getRemaining() > 0 && session.getChannel().isConnected() && !player.hasFinished()) {
			int packetId = stream.readPacket(player);
			if(packetId >= PACKET_SIZES.length || packetId < 0) {
				if(Settings.DEBUG)
					System.out.println("PacketId " + packetId + " has fake packet id.");
				break;
			}
			int length = PACKET_SIZES[packetId];
			if(length == -1)
				length = stream.readUnsignedByte();
			else if(length == -2)
				length = stream.readUnsignedShort();
			else if(length == -3)
				length = stream.readInt();
			else if(length == -4) {
				length = stream.getRemaining();
				if(Settings.DEBUG)
					System.out.println("Invalid size for PacketId " + packetId + ". Size guessed to be " + length);
			}
			if(length > stream.getRemaining()) {
				length = stream.getRemaining();
				if(Settings.DEBUG)
					System.out.println("PacketId " + packetId + " has fake size. - expected size " + length);
				// break;
				
			}
			
			ClientPacket packet_name = ClientPacket.getPacketFromId(packetId);
			
			if(packet_name == ClientPacket.UNKNOWN_PACKET) {
				System.out.println("Unknown Packet ID: " + packetId + ", Length: " + length);
			} else {
				if(packetId == -1) {
					System.out.println("Packet ID: " + packetId + ", Length: " + length);
				} else {
					if(packet_name != ClientPacket.PING_PACKET) {
						System.out.println("Packet: " + packet_name.toString() + ", Length: " + length);
					}
				}
			}
			
			int startOffset = stream.getOffset();
			processPackets(packetId, stream, length);
			stream.setOffset(startOffset + length);
		}
	}
	
	public static void decodeLogicPacket(final Player player, LogicPacket packet) {
		PacketManager.handleLogicPacket(player, packet);
	}
	
	public void processPackets(final int packetId, InputStream stream, int length) {
		player.setPacketsDecoderPing(Utils.currentTimeMillis());
		
		//PacketManager.handleLogicPacket(player, ClientPacket.getPacketFromId(packetId), stream);
		
		if(PacketManager.hasPacketHandler(ClientPacket.getPacketFromId(packetId))) { 
	    	PacketManager.handleLogicPacket(player, new LogicPacket(packetId, length, stream));
		}
		
		if(packetId == SWITCH_INTERFACE_ITEM_PACKET) {
			player.addLogicPacketToQueue(new LogicPacket(packetId, length, stream));
		} else if(packetId == CHAT_TYPE_PACKET) {
			chatType = stream.readUnsignedByte();
		} else if(packetId == CHAT_PACKET) {
			if(!player.hasStarted() && !World.containsLobbyPlayer(player.getUsername()))
				return;
			if(player.getLastPublicMessage() > Utils.currentTimeMillis())
				return;
			player.setLastPublicMessage(Utils.currentTimeMillis() + 300);
			int colorEffect = stream.readUnsignedByte();
			int moveEffect = stream.readUnsignedByte();
			String message = Huffman.readEncryptedMessage(200, stream);
			if(message == null || message.replaceAll(" ", "").equals(""))
				return;
			if(message.startsWith("::") || message.startsWith(";;")) {
				// if command exists and processed wont send message as public
				// message
				Commands.processCommand(player, message.replace("::", "").replace(";;", ""), false, false);
				return;
			}
			if(player.getMuted() > Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage(
						"You're account has been muted for breaking our rules. Please try again later.");
				return;
			}
			int effects = (colorEffect << 8) | (moveEffect & 0xff);
			if(chatType == 1)
				player.sendFriendsChannelMessage(Utils.fixChatMessage(message));
			else
				player.sendPublicChatMessage(new PublicChatMessage(Utils.fixChatMessage(message), effects));
			player.setLastMsg(message);
		}
	    
	}
	
}
