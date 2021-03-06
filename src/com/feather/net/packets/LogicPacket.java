package com.feather.net.packets;

import com.feather.io.InputStream;

public class LogicPacket {

	private ClientPacket id;
	byte[] data;

	public LogicPacket(int id, int size, InputStream stream) {
		this.id = ClientPacket.getPacketFromId(id);
		data = new byte[size];
		stream.getBytes(data, 0, size);
	}

	public ClientPacket getId() {
		return id;
	}

	public byte[] getData() {
		return data;
	}

}
