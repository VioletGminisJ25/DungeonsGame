package io.FaiscaJsr.DungeonsGame.MapGenerator.TileMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import io.FaiscaJsr.DungeonsGame.entities.Player;
import io.FaiscaJsr.DungeonsGame.entities.Enemies.Enemy;
import io.FaiscaJsr.DungeonsGame.Managers.ResourceLoader;
import io.FaiscaJsr.DungeonsGame.MapGenerator.BspTree;
import io.FaiscaJsr.DungeonsGame.Screens.PlayScreen;

public class Goal extends Tile {
    private Body body;
    private FixtureDef goalBodyDef;
    private BodyDef bodyDef;
    public Fixture goalFixture;
    private Player player;
    private static int RoomCount = 1;
    public Vector2 coordinates;

    public Goal(float x, float y, int rotation, Player player) {
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
        goalBodyDef.filter.categoryBits = PlayScreen.GOAL_BIT_MASK;
        goalFixture = body.createFixture(goalBodyDef);
        goalFixture.setUserData(this);

    }

    public void reachGoal() {
        System.out.println("Reach Goal");
        if (Player.currentRoom < BspTree.rooms.size() - 1) {
            if (BspTree.rooms.get(Player.currentRoom).roomManager.finishRoom()) {
                Gdx.app.postRunnable(() -> {// no entiendo que es xd
                    player.body.setTransform(BspTree.rooms.get(++Player.currentRoom).playerCoordinatesSpawn, 0);
                    // Player.currentRoom++;
                    if (!BspTree.rooms.get(Player.currentRoom).enemiesSpawned) {
                        BspTree.rooms.get(Player.currentRoom).roomManager.roomInitialized();
                        BspTree.rooms.get(Player.currentRoom).enemiesSpawned = true;
                    }
                    // BspTree.rooms.get(Player.currentRoom - 1).enemies.clear();
                });
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
