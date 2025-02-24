package io.FaiscaJsr.DungeonsGame.entities.TileMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.FaiscaJsr.DungeonsGame.Managers.AssetsManager;
import io.FaiscaJsr.DungeonsGame.Managers.ResourceLoader;


public class Floor extends Tile{

	public Floor(float x, float y,int rotationDegrees) {
		super(x,y, ResourceLoader.floorTile());
        super.getSprite().setRotation(rotationDegrees);
	}

}
