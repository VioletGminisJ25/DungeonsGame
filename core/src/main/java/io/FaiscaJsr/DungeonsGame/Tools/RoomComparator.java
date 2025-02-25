package io.FaiscaJsr.DungeonsGame.Tools;

import java.util.Comparator;

import io.FaiscaJsr.DungeonsGame.MapGenerator.Room.Room;

public class RoomComparator implements Comparator<Room> {

	@Override
	public int compare(Room o1, Room o2) {
		return o1.center.x < o2.center.x ? -1 : o1.center.x > o2.center.x ? 1 : 0;
	}

}
