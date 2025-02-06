package io.FaiscaJsr.DungeonsGame.Entities;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.FaiscaJsr.DungeonsGame.Entities.Room.Room;
import io.FaiscaJsr.DungeonsGame.Entities.TileMap.Floor;
import io.FaiscaJsr.DungeonsGame.Entities.TileMap.Tile;

public class BspTree {
	public Rectangle container;
	public Rectangle room;
	public BspTree left;
	public BspTree right;
	private static Random rnd = new Random();
	private ArrayList<Floor> floors;
	public static ArrayList<Room> rooms;
	private final static int MIN_ROOM_SIZE = 4;
    private Player player;

	public BspTree(Rectangle a ,Player player) {
		container = a;
		System.out.println(container);
		floors = new ArrayList<Floor>();
		rooms = new ArrayList<Room>();
        this.player = player;
	}

	public BspTree Split(int numberOfOperations, Rectangle container) {
		BspTree node = new BspTree(container,player);

		if (numberOfOperations == 0) {
			return node;
		}

		Rectangle[] splitedContainer = SplitContainer(container);
		// Console.WriteLine(numberOfOperations + "");
		node.left = Split(numberOfOperations - 1, splitedContainer[0]);
		System.out.println(numberOfOperations);

		// Debug.Log(numberOfOperations);
		node.right = Split(numberOfOperations - 1, splitedContainer[1]);
		System.out.println(numberOfOperations);

		return node;
	}

	private Rectangle[] SplitContainer(Rectangle container) {
		Rectangle c1, c2;
		boolean horizontal = rnd.nextBoolean();
		if (horizontal) {
			c1 = new Rectangle(container.x, container.y, container.width / 2,
					container.height);
			c2 = new Rectangle(container.x + (container.width / 2) * Tile.DIM, container.y, container.width / 2,
					container.height);
		} else {
			c1 = new Rectangle(container.x, container.y, container.width,
					container.height / 2);
			c2 = new Rectangle(container.x, container.y + (container.height / 2) * Tile.DIM, container.width,
					container.height / 2);
		}
		return new Rectangle[] { c1, c2 };
	}

	private int numHabitaciones = 0;

	public void load(BspTree node) {

		if (node.left != null) {
			 load(node.left);
		}
		if (node.right != null) {
				load(node.right);

		}else{
			if(node.container.width>MIN_ROOM_SIZE && node.container.height>MIN_ROOM_SIZE){

				System.out.println(node.container.x);
				System.out.println(node.container.y);
				Room room = new Room(numHabitaciones, new Vector2(node.container.x , node.container.y),(int) node.container.width,(int) node.container.height,player);
				room.setup();
				room.load();
				rooms.add(room);
				numHabitaciones++;
			}
		}
	}

	public void draw(SpriteBatch batch) {
		for (Room room : rooms) {
			room.draw(batch);
		}
		for (Floor floor : floors) {
			batch.draw(floor.getSprite(), floor.position.x, floor.position.y);
		}
	}
}
