package io.FaiscaJsr.DungeonsGame.Screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.FaiscaJsr.DungeonsGame.Main;
import io.FaiscaJsr.DungeonsGame.Managers.TimeManager;
import io.FaiscaJsr.DungeonsGame.MapGenerator.BspTree;
import io.FaiscaJsr.DungeonsGame.MapGenerator.TileMap.Tile;
import io.FaiscaJsr.DungeonsGame.Tools.WorldContactListener;
import io.FaiscaJsr.DungeonsGame.UI.Hud;
import io.FaiscaJsr.DungeonsGame.entities.Player;
import io.FaiscaJsr.DungeonsGame.entities.VirtualJoystick;
import io.FaiscaJsr.DungeonsGame.entities.Enemies.Enemy;
import io.FaiscaJsr.DungeonsGame.entities.Items.Heart;
import io.FaiscaJsr.DungeonsGame.entities.Items.Time;

public class PlayScreen implements Screen {

	public static final short PLAYER_BIT_MASK = 1;
	public static final short ENEMY_BIT_MASK = 2;
	public static final short GOAL_BIT_MASK = 4;
	public static final short WALL_BIT_MASK = 8;
	public static final short ITEM_BIT_MASK = 16;
	public static final short ATTACK_BIT_MASK = 32;
	public static final short REVERSE_GOAL_BIT_MASK = 64;

	public Main game;
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
	private Hud hud;

	public TextureAtlas getTextureAtlas() {
		return textureAtlas;
	}

	public TimeManager timeManager;
	public static  ArrayList<Body> bodiesToRemove = new ArrayList<>();

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
		hud = new Hud(this, game.batch);

		BspTree bspTree = new BspTree(new Rectangle(0, 0, 3000 / Tile.DIM, 3000 / Tile.DIM), player, this);
		tree = bspTree.Split(5, bspTree.container);
		tree.load(tree);
		BspTree.rooms.get(0).playerSpawn = true;
		player.body.setTransform(BspTree.rooms.get(0).playerCoordinatesSpawn, 0);
		BspTree.rooms.get(0).roomManager.roomInitialized();
		System.out.println("Rooms: " + BspTree.rooms.size());

		timeManager = new TimeManager(this);

	}

	@Override
	public void show() {
		game.stopMusic();
	}

	@Override
	public void render(float delta) {
		update(delta);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.begin();
		BspTree.draw(game.batch);
		if (player.body != null) {
			camera.position.set(player.body.getPosition().x, player.body.getPosition().y, 0);
		}
		Time.draw(delta, game.batch);
		Heart.draw(delta, game.batch);
		player.draw(game.batch);

		game.batch.end();

		virtualJoystick.render();
		// debugRenderer.render(world, stage.getCamera().combined);
		if (player.isDead()) {
			if (player.death.isAnimationFinished(player.stateTimer)) {
				GameOverScreen();
			}
		}

		hud.stage.draw();

	}

	public void GameOverScreen() {
		GameOverScreen gameOverScreen = new GameOverScreen(game);
		game.setScreen(gameOverScreen);
		dispose();
		System.out.println("Game Over");
	}

	public void update(float delta) {
		world.step(1 / 60f, 6, 2);
		HandleInput(delta);
		// for (Enemy enemy : Room.enemies) { // FIX: actualiza la lista de enemigos con
		// cada enemigo = muerte instantanea
		// // al recibir daño

		// enemy.update(delta);

		// }

		BspTree.rooms.get(Player.currentRoom).roomManager.update(delta);

		if (!Enemy.enemiesToHit.isEmpty()) {
			// System.out.println("Enemies to hit: " + enemiesToHit.size());

			for (Enemy enemy : Enemy.enemiesToHit) {
				if (enemy.getCurrentHealth() > 0) {
					if (player.isattack) {
						enemy.hit(20, delta);
					}
				}
				if (enemy.getCurrentHealth() - enemy.getDamage() <= 0) {
					enemy.enemyDead = true;
					Enemy.enemiesToRemove.add(enemy);
				}

			}
			Enemy.enemiesToHit.clear();

		}

		for (Enemy enemy : Enemy.enemiesToRemove) {
			if (enemy.body != null) {
				world.destroyBody(enemy.body);
				enemy.body = null;
			}
			// Room.enemies.remove(enemy);
		}

		Enemy.enemiesToRemove.clear();

		for (Body body : bodiesToRemove) {
			world.destroyBody(body);
		}
		bodiesToRemove.clear();

		player.update(delta);
		game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
		game.batch.setProjectionMatrix(stage.getCamera().combined);
		camera.update();
		timeManager.update(delta);
		hud.updateHud();
	}

	public void HandleInput(float delta) {
		player.HandleInput(delta);
	}

	public void FinishScreen() {
		GameFinishScreen gameFinishScreen = new GameFinishScreen(game);
		game.setScreen(gameFinishScreen);
		dispose();
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
		BspTree.dispose();
		virtualJoystick.dispose();
		textureAtlas.dispose();
		debugRenderer.dispose();
		world.dispose();

	}

}
