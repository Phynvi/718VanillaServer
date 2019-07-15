package com.feather.net.packets;

public enum ClientPacket {

	AFK_PACKET(-1),
	
	WALKING_PACKET(8),
	MINI_WALKING_PACKET(58),
	
	ACTION_BUTTON_1_PACKET(14),
	ACTION_BUTTON_2_PACKET(67),
	ACTION_BUTTON_3_PACKET(5),
	ACTION_BUTTON_4_PACKET(55),
	ACTION_BUTTON_5_PACKET(68),
	ACTION_BUTTON_6_PACKET(90),
	ACTION_BUTTON_7_PACKET(6),
	ACTION_BUTTON_8_PACKET(32),
	ACTION_BUTTON_9_PACKET(27),
	
	WORLD_MAP_CLICK(38),
	
	WORLD_LIST_UPDATE(87),
	
	CLAN_FORUM_THREAD(18),
	
	CLAN_NAME_AND_MOTTO(48),
	
	ITEM_EXAMINE(102);
	
	
	
	
	/**
	 * The value.
	 */
	private final byte value;

	/**
	 * Constructs a new {@code ClientPacket} {@code Object}.
	 * 
	 * @param value
	 *            The value.
	 */
	private ClientPacket(int value) {
		this.value = (byte) value;
	}

	/**
	 * Gets the return code value.
	 * 
	 * @return The value.
	 */
	public byte getValue() {
		return value;
	}
	
	
}
