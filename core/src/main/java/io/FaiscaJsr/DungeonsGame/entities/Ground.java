package io.FaiscaJsr.DungeonsGame.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;

import io.FaiscaJsr.DungeonsGame.ResourceLoader;

public class Ground extends Tile{

	private static Sprite ground  = ResourceLoader.groundTile(); 

	public Ground(float x, float y) {
		super(x,y,ground);
	}
}
