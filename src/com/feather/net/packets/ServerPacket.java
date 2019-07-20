package com.feather.net.packets;

public enum ServerPacket {
	
	UNKNOWN_PACKET(),
	
	PING_PACKET(153);
	
	
	
	
	
	/**
	 * The value.
	 */
    private final int[] value;
	/**
	 * Constructs a new {@code ServerPacket} {@code Object}.
	 * 
	 * @param value
	 *            The value.
	 */
	private ServerPacket(int value) {
		this.value = new int[] { value };
	}
	
	private ServerPacket(int[] value) {
		this.value = value;
	}

	private ServerPacket() {
		this.value = new int[] { -1 };
	}
	
	/**
	 * Gets the return code value.
	 * 
	 * @return The value.
	 */
	public final int getValue() {
		return value[0];
	}
	
	public final int[] getValues() {
		return this.value;
	}
	
	public static ServerPacket getPacketFromId(final int id) {
		for(ServerPacket server_packet : ServerPacket.values()) {
			if(id == server_packet.getValue()) {
				return server_packet;
			}
		}
		
		return ServerPacket.UNKNOWN_PACKET;
	}
}
