package com.feather.net.packets;

public enum ClientPacket {

	UNKNOWN_PACKET(),
	
	AFK_PACKET(-1),
	ENTER_STRING_PACKET(-1),
	MAGIC_ON_ITEM_PACKET(-1),
	CLICK_PACKET(-1),
	MOVE_MOUSE_PACKET(-1),
	IN_OUT_SCREEN_PACKET(-1),
	
	WALKING_PACKET(8, -1),
	MINI_WALKING_PACKET(58, -1),
	ACTION_BUTTON_1_PACKET(14, 8),
	ACTION_BUTTON_2_PACKET(67, 8),
	ACTION_BUTTON_3_PACKET(5, 8),
	ACTION_BUTTON_4_PACKET(55, 8),
	ACTION_BUTTON_5_PACKET(68, 8),
	ACTION_BUTTON_6_PACKET(90, 8),
	ACTION_BUTTON_7_PACKET(6, 8),
	ACTION_BUTTON_8_PACKET(32, 8),
	ACTION_BUTTON_9_PACKET(27, 8),
	ACTION_BUTTON_10_PACKET(96, 8),
	WORLD_MAP_CLICK(38, 4),
	WORLD_LIST_UPDATE(87, 4),
	CLAN_FORUM_THREAD(18, -1),
	ITEM_ON_FLOOR_EXAMINE(102, 7),
	LOBBY_MAIN_CLICK_PACKET(91, -2),
	LOBBY_FRIEND_CHAT_SETTINGS(79, 3),
	RECEIVE_PACKET_COUNT_PACKET(33, 4),
	PLAYER_OPTION_4_PACKET(17, 3),
	MOVE_CAMERA_PACKET(103, 4),
	INTERFACE_ON_OBJECT(37, 17),
	CLOSE_INTERFACE_PACKET(54, 0),
	DEVELOPER_CONSOLE_PACKET(60, -1),
	ITEM_ON_ITEM_PACKET(3, 16),
	DONE_LOADING_REGION_PACKET(30, 0),
	PING_PACKET(21, 0),
	SCREEN_PACKET(98, 6),
	CHAT_TYPE_PACKET(83, 1),
	CHAT_PACKET(53, -1),
	FRIEND_QUICK_CHAT_PACKET(86, -1),
	ADD_FRIEND_PACKET(89, -1),
	ADD_IGNORE_PACKET(4, -1),
	REMOVE_IGNORE_PACKET(73, -1),
	JOIN_FRIEND_CHAT_PACKET(36, -1),
	CHANGE_FRIEND_CHAT_PACKET(22, -1),
	KICK_FRIEND_CHAT_PACKET(74, -1),
	REMOVE_FRIEND_PACKET(24, -1),
	PERSONAL_MESSAGE_PACKET(82, -2),
	OBJECT_CLICK_1_PACKET(26, 9),
	OBJECT_CLICK_2_PACKET(59, 9),
	OBJECT_CLICK_3_PACKET(40, 9),
	OBJECT_CLICK_4_PACKET(23, 9),
	OBJECT_CLICK_5_PACKET(80, 9),
	OBJECT_EXAMINE_PACKET(25, 9),
	NPC_CLICK_1_PACKET(31, 3),
	NPC_CLICK_2_PACKET(101, 3),
	NPC_CLICK_3_PACKET(34, 3),
	NPC_CLICK_4_PACKET(65, 3),
	ATTACK_NPC_PACKET(20, 3),
	PLAYER_OPTION_1_PACKET(42, 3),
	PLAYER_OPTION_2_PACKET(46, 3),
	PLAYER_OPTION_6_PACKET(49, 3),
	PLAYER_OPTION_5_PACKET(77, 3),
	PLAYER_OPTION_7_PACKET(51, 3),
    PLAYER_OPTION_9_PACKET(56, 3),
	ITEM_TAKE_PACKET(57, 7),
	DIALOGUE_CONTINUE_PACKET(72, 6),
	ENTER_INTEGER_PACKET(81, 4),
	ENTER_NAME_PACKET(29, -1),
	SWITCH_INTERFACE_ITEM_PACKET(76, 16),
	INTERFACE_ON_PLAYER(50, 11),
	INTERFACE_ON_NPC(66, 11),
	COLOR_ID_PACKET(97, 2),
	NPC_EXAMINE_PACKET(9, 3),
	REPORT_ABUSE_PACKET(11, -1),
	JOIN_CLAN_CHAT_PACKET(133),
	ENTER_LONG_TEXT_PACKET(48, -1),
    KICK_CLAN_CHAT_PACKET(92, -1),
    GRAND_EXCHANGE_ITEM_SELECT_PACKET(71, 2),
	
	// Possibly Correct?
	MOUSE_PRESSED_PACKET(15, 6),
	MOUSE_RELEASED_PACKET(13, 7),
	WINDOW_FOCUS_PACKET(70, 1),
	KEY_PRESSED_PACKET(1, -2);
	
	
	/**
	 * The ID.
	 */
	private final int packet_id;
	/**
	 * The Length
	 */
	private final int packet_length;

	/**
	 * Constructs a new {@code ClientPacket} {@code Enum}.
	 * 
	 * @param id  The Packet ID.
	 */
	private ClientPacket(int id) {
		this.packet_id = id;
		this.packet_length = -1;
	}

	/**
	 * Constructs a new {@code ClientPacket} {@code Enum}.
	 * 
	 * @param id      The Packet ID.
	 * @param length  The Packet Length
	 */
	private ClientPacket(int id, int length) {
		this.packet_id = id;
		this.packet_length = length;
	}
	
	/**
	 * Constructs a new {@code ClientPacket} {@code Enum}.
	 */
	private ClientPacket() {
		this.packet_id = -1;
		this.packet_length = -1;
	}
	
	/**
	 * Gets the Packet ID.
	 * 
	 * @return The ID.
	 */
	public final int getID() {
		return packet_id;
	}
	
	/**
	 * Gets the length of the Packet
	 * @return The Length
	 */
	public final int getLength() {
		return packet_length;
	}
	
	
	public static ClientPacket getPacketFromId(final int id) {
		for(ClientPacket client_packet : ClientPacket.values()) {
			if(id == client_packet.getID()) {
				return client_packet;
			}
		}
		
		return ClientPacket.UNKNOWN_PACKET;
	}
}
