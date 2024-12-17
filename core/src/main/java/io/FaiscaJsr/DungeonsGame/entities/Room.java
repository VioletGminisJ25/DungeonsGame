package io.FaiscaJsr.DungeonsGame.entities;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Room extends Sprite{
	public int x1;
	public int x2;
	public int y1;
	public int y2;

	public int w;
	public int h;

	public Vector2 center;

	public Room(int x, int y, int width, int height) {
		super();
		
		x1 = x;
		x2 = x + width;
		y1 = y;
		y2 = y + height;
		this.setX(x * Tile.DIM);
		this.setY(y * Tile.DIM);
		this.w = width;
		this.h = height;
		center = new Vector2(Math.round((x1 + x2) / 2),Math.round((y1 + y2) / 2));
	}

	public boolean intersects(Room room){
		return (x1 <= room.x2 && x2 >= room.x1 && y1 <= room.y2 && room.y2 >= room.y1);
	}
}
