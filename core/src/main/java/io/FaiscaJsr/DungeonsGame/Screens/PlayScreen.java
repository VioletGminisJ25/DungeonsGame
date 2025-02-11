package io.FaiscaJsr.DungeonsGame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.FaiscaJsr.DungeonsGame.Main;
import io.FaiscaJsr.DungeonsGame.Tools.WorldContactListener;
import io.FaiscaJsr.DungeonsGame.entities.BspTree;
import io.FaiscaJsr.DungeonsGame.entities.Enemy;
import io.FaiscaJsr.DungeonsGame.entities.Player;
import io.FaiscaJsr.DungeonsGame.entities.VirtualJoystick;
import io.FaiscaJsr.DungeonsGame.entities.Room.Room;
import io.FaiscaJsr.DungeonsGame.entities.TileMap.Tile;

public class PlayScreen implements Screen {

    public static final short PLAYER_BIT_MASK = 1;
    public static final short ENEMY_BIT_MASK = 2;
    public static final short GOAL_BIT_MASK = 4;
    public static final short WALL_BIT_MASK = 8;
    public static final short ITEM_BIT_MASK = 16;
    public static final short ATTCK_BIT_MASK = 32;

    private Main game;
    Texture texture;
    private OrthographicCamera camera;
    private Viewport viewport;
    public static World world;
    private Box2DDebugRenderer debugRenderer;
    public Player player;
    public static int PPM = 5;
    private TextureAtlas textureAtlas;
    public VirtualJoystick virtualJoystick;
    private Stage stage;
    private BspTree tree;

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

    public PlayScreen(Main game) {
        super();
        this.game = game;
        Box2D.init();
        camera = new OrthographicCamera();
        viewport = new FitViewport(1920 / PPM, 1080 / PPM, camera);
        stage = new Stage(viewport);
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new WorldContactListener());
        textureAtlas = new TextureAtlas("player/player2.atlas");
        debugRenderer = new Box2DDebugRenderer();
        virtualJoystick = new VirtualJoystick(0, 0, 20, 12);
        player = new Player(world, this, virtualJoystick);
        virtualJoystick.setPlayer(player);

        BspTree bspTree = new BspTree(new Rectangle(0, 0, 3000 / Tile.DIM, 3000 / Tile.DIM), player, this);
        tree = bspTree.Split(5, bspTree.container);
        tree.load(tree);
        BspTree.rooms.get(0).playerSpawn = true;
        player.body.setTransform(new Vector2(BspTree.rooms.get(0).center.x, BspTree.rooms.get(0).center.y), 0);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        tree.draw(game.batch);
        camera.position.set(player.body.getPosition().x, player.body.getPosition().y, 0);
        for (Enemy enemy : Room.enemies) {
            enemy.draw(game.batch);
        }
        player.draw(game.batch);
        game.batch.end();
        virtualJoystick.render();
        update(delta);
    }
    
    public void update(float delta) {

        HandleInput(delta);
        world.step(1 / 60f, 6, 2);
        for (Enemy enemy : Room.enemies) { // FIX: actualiza la lista de enemigos con cada enemigo = muerte instantanea
                                           // al recibir daño
            enemy.update(delta);

        }

        if (!Enemy.enemiesToHit.isEmpty()) {
            // System.out.println("Enemies to hit: " + enemiesToHit.size());

            for (Enemy enemy : Enemy.enemiesToHit) {
                if (player.isattack) {
                    if (enemy.getCurrentHealth() > 0) {
                        enemy.hit(20, delta);
                    } else {
                        enemy.enemyDead = true;
                        Enemy.enemiesToRemove.add(enemy);
                    }
                }
            }
            Enemy.enemiesToHit.clear();
        }

        for (Enemy enemy : Enemy.enemiesToRemove) {
            world.destroyBody(enemy.body);
        }
        Enemy.enemiesToRemove.clear();

        player.update(delta);
        game.batch.setProjectionMatrix(stage.getCamera().combined);
        camera.update();
        debugRenderer.render(world, stage.getCamera().combined);
    }

    public void HandleInput(float delta) {
        player.HandleInput(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        virtualJoystick.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        player.dispose();
        virtualJoystick.dispose();
        textureAtlas.dispose();
        debugRenderer.dispose();
        world.dispose();
    }

}
