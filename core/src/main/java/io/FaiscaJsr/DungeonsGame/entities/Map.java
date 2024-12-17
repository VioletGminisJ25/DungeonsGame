package io.FaiscaJsr.DungeonsGame.entities;

import java.util.ArrayList;
import java.util.Random;

public class Map {
	public static final int MAP_WIDTH = 800;
	public static final int MAP_HEIGHT = 480;
	public static final int MAX_ROOMS = 6;
	public static final int MIN_ROOMS = 2;
	public static final int MAX_ROOM_SIZE = 32*6;
	public static final int MIN_ROOM_SIZE = 32*2;
	Random rand = new Random();

	public void placeRooms(){
		ArrayList<Room> rooms = new ArrayList<Room>();
		int cantRooms = rand.nextInt(MIN_ROOMS,MAX_ROOMS+1);
		boolean error = false;
		for(int i = 0;i<cantRooms;i++){
			error = false;

			int roomWidth = MIN_ROOM_SIZE + rand.nextInt(MIN_ROOM_SIZE,MAX_ROOM_SIZE+1);
			int roomHeight = MIN_ROOM_SIZE + rand.nextInt(MIN_ROOM_SIZE,MAX_ROOM_SIZE+1);
			int x = rand.nextInt(MAP_WIDTH - roomWidth -1) +1;
			int y = rand.nextInt(MAP_HEIGHT - roomHeight -1) +1;
			Room room  = new Room(x,y,roomWidth,roomHeight);
			
			if(rooms.size() != 0){
				for (Room otherRoom : rooms) {
					if(!room.intersects(otherRoom)){
						error =true; 
					}
				}
			}

			if(!error){
				rooms.add(room);
			}
		}
	
	}
}
