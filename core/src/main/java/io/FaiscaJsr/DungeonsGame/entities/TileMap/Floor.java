package io.FaiscaJsr.DungeonsGame.entities.TileMap;

import com.badlogic.gdx.graphics.g2d.Sprite;


import io.FaiscaJsr.DungeonsGame.ResourceLoader;


public class Floor extends Tile{
	private static Sprite floor  = ResourceLoader.floorTile();

	public Floor(float x, float y,int rotationDegrees) {
		super(x,y,floor);
		floor.setRotation(rotationDegrees);
	}

}
