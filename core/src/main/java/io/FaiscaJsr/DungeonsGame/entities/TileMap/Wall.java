package io.FaiscaJsr.DungeonsGame.entities.TileMap;

import com.badlogic.gdx.graphics.g2d.Sprite;

import io.FaiscaJsr.DungeonsGame.ResourceLoader;

public class Wall extends Tile { 

	public Wall(float x, float y,int rotationDegrees) {
		super(x,y,ResourceLoader.wallTile(rotationDegrees));
	}
	
}
