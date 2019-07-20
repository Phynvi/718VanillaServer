package com.feather.net.packets.incoming.impl;

import com.feather.game.World;
import com.feather.game.WorldTile;
import com.feather.game.item.FloorItem;
import com.feather.game.player.CoordsEvent;
import com.feather.game.player.Player;
import com.feather.io.InputStream;
import com.feather.net.packets.ClientPacket;
import com.feather.net.packets.LogicPacket;
import com.feather.net.packets.incoming.AbstractLogicPacket;
import com.feather.utils.Utils;

public class ItemTakePacket extends AbstractLogicPacket {
	
	@Override
	public void onPacketReceived(Player player, LogicPacket packet, InputStream stream) {
		if(!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
			return;
		long currentTime = Utils.currentTimeMillis();
		if(player.getLockDelay() > currentTime)
			// || player.getFreezeDelay() >= currentTime)
			return;
		int y = stream.readUnsignedShort();
		int x = stream.readUnsignedShortLE();
		final int id = stream.readUnsignedShort();
		boolean forceRun = stream.read128Byte() == 1;
		final WorldTile tile = new WorldTile(x, y, player.getPlane());
		final int regionId = tile.getRegionId();
		if(!player.getMapRegionsIds().contains(regionId))
			return;
		final FloorItem item = World.getRegion(regionId).getGroundItem(id, tile, player);
		if(item == null)
			return;
		player.stopAll(false);
		if(forceRun)
			player.setRun(forceRun);
		player.setCoordsEvent(new CoordsEvent(tile, new Runnable() {
			@Override
			public void run() {
				final FloorItem item = World.getRegion(regionId).getGroundItem(id, tile, player);
				if(item == null)
					return;
				/*
				 * if (player.getRights() > 0 || player.isSupporter())
				 * player.getPackets().sendGameMessage("This item was dropped by [Username] "
				 * +item.getOwner().getUsername()+
				 * " [DiplayName] "+item.getOwner().getDisplayName());
				 */ player.setNextFaceWorldTile(tile);
				player.addWalkSteps(tile.getX(), tile.getY(), 1);
				World.removeGroundItem(player, item);
			}
		}, 1, 1));
	}
	
	@Override
	public ClientPacket[] getHandledPackets() {
		return new ClientPacket[] { ClientPacket.ITEM_TAKE_PACKET };
	}
	
}
