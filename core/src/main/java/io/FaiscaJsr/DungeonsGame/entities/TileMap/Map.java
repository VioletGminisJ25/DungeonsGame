package io.FaiscaJsr.DungeonsGame.entities.TileMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import io.FaiscaJsr.DungeonsGame.ResourceLoader;

public class Map {
	public static final int MAP_WIDTH = 1920 / 32;
	public static final int MAP_HEIGHT = 1080 / 32;
	public static final int MAX_ROOMS = 10;
	public static final int MIN_ROOMS = 4;
	public ArrayList<Floor> floors;

	public ArrayList<Room> rooms;
	private int[] dimensions;

	public static Random rand = new Random();

	public Map() {
		super();
		rooms = new ArrayList<Room>();
		floors = new ArrayList<Floor>();
		dimensions = new int[] { 8, 9, 10,11,12,13 };
		placeRooms();
	}

	public void placeRooms() {
		int cantRooms = rand.nextInt(MIN_ROOMS, MAX_ROOMS + 1);
		boolean error = false;
		Room room;
		do {
			int roomWidth = dimensions[rand.nextInt(0, dimensions.length)];
			int roomHeight = dimensions[rand.nextInt(0, dimensions.length)];
			int x = rand.nextInt(-(MAP_WIDTH / 2), MAP_WIDTH / 2) * 32;
			int y = rand.nextInt(-(MAP_HEIGHT / 2), MAP_HEIGHT / 2) * 32;

			room = new Room(new Vector2(x, y), roomWidth, roomHeight);
			error = false;
			if (rooms.size() > 0) {
				for (Room r : rooms) {
					if (room.collidesWith(r)) {
						error = true;
					}
				}
			}
			if (!error) {
				rooms.add(room);
			}
		} while (!(rooms.size() == cantRooms));

		Collections.sort(rooms, new RoomComparator());
		System.out.println("Rooms: " + rooms.size());
		for (int i = 0; i < rooms.size(); i++) {
			Room room2 = rooms.get(i);
			Vector2 prevCenter = rooms.get(i).center;

			if (i != 0) {
				prevCenter = rooms.get(i - 1).center;
			}

			if (rand.nextBoolean()) {
				horizontalCorridor(prevCenter.x, room2.center.x, prevCenter.y);
				verticalCorridor(prevCenter.y, room2.center.y, room2.center.x);
			} else {
				verticalCorridor(prevCenter.y , room2.center.y , prevCenter.x );
				horizontalCorridor(prevCenter.x, room2.center.x, room2.center.y);
			}

		}

	}

	public void createRooms(SpriteBatch batch) {
		corridorCreator(batch);
		for (int i = 0; i < rooms.size(); i++) {
			Room room = rooms.get(i);
			room.setup();
			room.createFloors(batch);
		}
	}

	private void corridorCreator(SpriteBatch batch) {
		// System.out.println(floors.size());
		for (Floor floor : floors) {
			batch.draw(floor.getSprite(), floor.position.x, floor.position.y);
		}
	}

	private void horizontalCorridor(float x1, float x2, float y) {
		y = y - 5;
		x1 = x1 - 5;
		x2 = x2 - 5;
		for (float x = Math.min(x1, x2); x <= Math.max(x1, x2); x += 32) {
			Floor floor = new Floor(x, y, 0); // Ajuste de posición
			floors.add(floor);
		}
	}

	private void verticalCorridor(float y1, float y2, float x) {
		x = x - 5;
		y1 = y1 - 5;
		y2 = y2 - 5;
		for (float y = Math.min(y1, y2); y <= Math.max(y1, y2); y += 32) {
			Floor floor = new Floor(x, y, 0); // Ajuste de posición
			floors.add(floor);
		}
	}
}
