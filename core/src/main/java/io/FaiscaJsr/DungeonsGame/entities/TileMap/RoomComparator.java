package io.FaiscaJsr.DungeonsGame.entities.TileMap;

import java.util.Comparator;

public class RoomComparator implements Comparator<Room> {

	@Override
	public int compare(Room o1, Room o2) {
		return o1.center.x < o2.center.x ? -1 : o1.center.x > o2.center.x ? 1 : 0;
	}

}
