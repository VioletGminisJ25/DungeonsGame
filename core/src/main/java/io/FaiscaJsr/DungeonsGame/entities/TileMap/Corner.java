
package io.FaiscaJsr.DungeonsGame.entities.TileMap;

import io.FaiscaJsr.DungeonsGame.ResourceLoader;

public class Corner extends Tile {

	public Corner(float x, float y,int rotationDegrees) {
		super(x,y,ResourceLoader.cornerTile(rotationDegrees));
	}
	
}
