package io.FaiscaJsr.DungeonsGame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.FaiscaJsr.DungeonsGame.entities.Tile;

public class ResourceLoader {
	static Texture tiles = new Texture("tileset_complet.png");
	public static Sprite groundTile(){
		int x = 32;
		int y = 32;
		return new Sprite(new TextureRegion(tiles,x,y,Tile.DIM*2,Tile.DIM*2));
	}
}
