package io.FaiscaJsr.DungeonsGame.entities.TileMap;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import io.FaiscaJsr.DungeonsGame.Managers.ResourceLoader;
import io.FaiscaJsr.DungeonsGame.Screens.PlayScreen;

public class Wall extends Tile {
    private Body body;
    private FixtureDef wallBodyDef;
    private BodyDef bodyDef;
    private Fixture wallFixture;

    public Wall(float x, float y, int rotationDegrees) {
        super(x, y, ResourceLoader.wallTile(rotationDegrees));
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x + Tile.DIM / 2, y + Tile.DIM / 2);
        body = PlayScreen.world.createBody(bodyDef);
        wallBodyDef = new FixtureDef();
        wallBodyDef.density = 20.0f;
        wallBodyDef.friction = 0.0f;
        PolygonShape wallShape = new PolygonShape();
        wallShape.setAsBox(Tile.DIM / 2, Tile.DIM / 2);
        wallBodyDef.shape = wallShape;
        wallBodyDef.filter.categoryBits = PlayScreen.WALL_BIT_MASK;
        wallFixture = body.createFixture(wallBodyDef);

    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
