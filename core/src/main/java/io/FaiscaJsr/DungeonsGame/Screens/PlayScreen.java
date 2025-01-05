package io.FaiscaJsr.DungeonsGame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.FaiscaJsr.DungeonsGame.Main;
import io.FaiscaJsr.DungeonsGame.entities.Player;
import io.FaiscaJsr.DungeonsGame.entities.TileMap.Map;
import io.FaiscaJsr.DungeonsGame.entities.TileMap.Room;
import io.FaiscaJsr.DungeonsGame.entities.TileMap.Tile;

public class PlayScreen implements Screen {
	private Main game;
	Texture texture;
	private OrthographicCamera camera;
	private Viewport viewport;
	private Map map;
	public static World world;
	private Box2DDebugRenderer debugRenderer;
	private Player player;
	private int PPM = 5;
	private TextureAtlas textureAtlas;

	

	public TextureAtlas getTextureAtlas() {
		return textureAtlas;
	}

	public PlayScreen(Main game) {
		super();
		this.game = game;
		world = new World(new Vector2(0, 0), true);
		textureAtlas = new TextureAtlas("player/Player.atlas");
		camera = new OrthographicCamera();
		viewport = new FitViewport(1920/PPM, 1080/PPM, camera); 
		map = new Map();
		debugRenderer = new Box2DDebugRenderer();
		player = new Player(world,this);
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
		map.createRooms(game.batch);
		camera.position.set(player.body.getPosition().x, player.body.getPosition().y, 0);
		player.draw(game.batch);
		game.batch.end();
		update(delta);
	}
	
	public void update(float delta) {
		
		HandleInput(delta);
		world.step(1 / 60f, 6, 2);
		player.update(delta);
		game.batch.setProjectionMatrix(camera.combined);
		camera.update();
		debugRenderer.render(world, camera.combined);

	}

	public void HandleInput(float delta) {
		player.HandleInput();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);

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

	}

}
