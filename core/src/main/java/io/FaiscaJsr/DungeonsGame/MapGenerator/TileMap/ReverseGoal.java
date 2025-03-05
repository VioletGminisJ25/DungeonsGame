package io.FaiscaJsr.DungeonsGame.MapGenerator.TileMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import io.FaiscaJsr.DungeonsGame.Managers.ResourceLoader;
import io.FaiscaJsr.DungeonsGame.MapGenerator.BspTree;
import io.FaiscaJsr.DungeonsGame.Screens.PlayScreen;
import io.FaiscaJsr.DungeonsGame.entities.Player;

/**
 * Clase que representa el objetivo inverso.
 * Para retroceder en la habitación.
 */
public class ReverseGoal extends Tile {
    private Body body;
    private FixtureDef goalBodyDef;
    private BodyDef bodyDef;
    public Fixture goalFixture;
    private Player player;
    private static int RoomCount = 1;
    public Vector2 coordinates;

    /**
     * Constructor del objetivo inverso.
     *
     * @param x        Posicion de la puerta inversa en el eje X.
     * @param y        Posicion de la puerta inversa en el eje Y.
     * @param rotation Rotacion de la puerta inversa.
     * @param player   Jugador.
     */
    public ReverseGoal(float x, float y, int rotation, Player player) {
        super(x, y, ResourceLoader.goalTile(rotation));
        this.player = player;
        this.coordinates = new Vector2(x, y);
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x + (Tile.DIM), y + (Tile.DIM / 2));
        body = PlayScreen.world.createBody(bodyDef);
        goalBodyDef = new FixtureDef();
        goalBodyDef.density = 20.0f;
        goalBodyDef.friction = 0.0f;
        PolygonShape wallShape = new PolygonShape();
        wallShape.setAsBox(Tile.DIM, Tile.DIM / 2);
        goalBodyDef.shape = wallShape;
        goalBodyDef.filter.categoryBits = PlayScreen.REVERSE_GOAL_BIT_MASK;
        // goalBodyDef.filter.maskBits = PlayScreen.PLAYER_BIT_MASK |
        // PlayScreen.ENEMY_BIT_MASK;
        goalFixture = body.createFixture(goalBodyDef);
        goalFixture.setUserData(this);
    }

    /**
     * Método que actualiza el objetivo inverso.
     * Se verifica si el jugador ha llegado al objetivo inverso.
     * Si es el caso, se cambia la posición del jugador.
     */
    public void reachGoal() {
        if (Player.currentRoom > 0) {
            Gdx.app.postRunnable(() -> {// no entiendo que es xd
                player.body.setTransform(BspTree.rooms.get(--Player.currentRoom).center, 0);
            });
        }
    }

    /**
     * Método que libera los recursos utilizados por el objetivo inverso.
     */
    @Override
    public void dispose() {
        super.dispose();
    }
}
