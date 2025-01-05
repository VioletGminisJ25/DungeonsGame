package io.FaiscaJsr.DungeonsGame.entities.TileMap;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import io.FaiscaJsr.DungeonsGame.ResourceLoader;
import io.FaiscaJsr.DungeonsGame.Screens.PlayScreen;

public class Floor extends Tile{
	private FixtureDef groundBodyDef;
	private Body body;
	private static Sprite floor  = ResourceLoader.floorTile(); 

	public Floor(float x, float y,int rotationDegrees) {
		super(x,y,floor);
		floor.setRotation(rotationDegrees);
		
		
		
		
	}
}
