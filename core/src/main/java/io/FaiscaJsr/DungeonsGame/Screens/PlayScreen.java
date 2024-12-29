package io.FaiscaJsr.DungeonsGame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.FaiscaJsr.DungeonsGame.Main;

import io.FaiscaJsr.DungeonsGame.entities.Map;
import io.FaiscaJsr.DungeonsGame.entities.Tile;
import io.FaiscaJsr.DungeonsGame.entities.TileMap;



public class PlayScreen implements Screen{
	private Main game;
	Texture texture;
	private OrthographicCamera camera;
	private Viewport viewport;
	private Map map;
	TileMap tilemap;

	public PlayScreen(Main game) {
		super();
		this.game = game;
		texture = new Texture("libgdx.png");
		camera = new OrthographicCamera();
		viewport = new FitViewport(1920, 1080,camera);
		map =new Map();
		tilemap = new TileMap(new Vector2(0-TileMap.getWidth()*Tile.DIM,0-TileMap.getHeight()*Tile.DIM)); 
	}

	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
		game.batch.setProjectionMatrix(camera.combined);
		camera.update();
		tilemap.createFloors(game.batch);
		// map.createFloors(game.batch);
		game.batch.end();
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
