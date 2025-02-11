package io.FaiscaJsr.DungeonsGame.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import io.FaiscaJsr.DungeonsGame.ResourceLoader;
import io.FaiscaJsr.DungeonsGame.Entities.TileMap.Tile;
import io.FaiscaJsr.DungeonsGame.Screens.PlayScreen;

public class Goal extends Tile{
  private Body body;
	private FixtureDef goalBodyDef;
	private BodyDef bodyDef;
	public Fixture goalFixture;
	private Player player;
	private static int RoomCount = 0;
	public Goal(float x, float y,int rotation,Player player) {
		super(x, y, ResourceLoader.goalTile(rotation));
        this.player = player;
		 bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set(x +(Tile.DIM), y + (Tile.DIM/2));
		body = PlayScreen.world.createBody(bodyDef);
		goalBodyDef = new FixtureDef();
		goalBodyDef.density = 20.0f;
		goalBodyDef.friction = 0.0f;
		PolygonShape wallShape = new PolygonShape();
		wallShape.setAsBox(Tile.DIM , Tile.DIM/2);
		goalBodyDef.shape = wallShape;
        goalBodyDef.filter.categoryBits = PlayScreen.GOAL_BIT_MASK;
		goalFixture = body.createFixture(goalBodyDef);
		goalFixture.setUserData(this);
	}

	public void reachGoal(){
		System.out.println("Reach Goal");
		if(RoomCount<BspTree.rooms.size()){
            Gdx.app.postRunnable(()->{//no entiendo que es xd
                player.body.setTransform(BspTree.rooms.get(RoomCount++).center, 0);
            });

		}
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
