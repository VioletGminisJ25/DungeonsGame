
package io.FaiscaJsr.DungeonsGame.MapGenerator.TileMap;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import io.FaiscaJsr.DungeonsGame.Managers.ResourceLoader;
import io.FaiscaJsr.DungeonsGame.Screens.PlayScreen;

/**
 * Clase que representa una esquina.
 */
public class Corner extends Tile {

    private Body body;
    private FixtureDef cornerBodyDef;
    private BodyDef bodyDef;
    private Fixture cornerFixture;

    /**
     * Constructor de la esquina.
     *
     * @param x               Posición en el eje X.
     * @param y               Posición en el eje Y.
     * @param rotationDegrees Rotación de la esquina.
     */
    public Corner(float x, float y, int rotationDegrees) {
        super(x, y, ResourceLoader.cornerTile(rotationDegrees));
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x + Tile.DIM / 2, y + Tile.DIM / 2);
        body = PlayScreen.world.createBody(bodyDef);
        cornerBodyDef = new FixtureDef();
        cornerBodyDef.density = 20.0f;
        cornerBodyDef.friction = 0.0f;
        PolygonShape wallShape = new PolygonShape();
        wallShape.setAsBox(Tile.DIM / 2, Tile.DIM / 2);
        cornerBodyDef.shape = wallShape;
        cornerBodyDef.filter.categoryBits = PlayScreen.WALL_BIT_MASK;
        cornerFixture = body.createFixture(cornerBodyDef);
    }

}
