package io.FaiscaJsr.DungeonsGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import io.FaiscaJsr.DungeonsGame.Managers.AssetsManager;
import io.FaiscaJsr.DungeonsGame.Screens.MainMenuScreen;
import io.FaiscaJsr.DungeonsGame.Screens.PlayScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public SpriteBatch batch;


	@Override
	public void create() {
        AssetsManager.load();
        AssetsManager.finishLoading();
		batch = new SpriteBatch();
		setScreen( new MainMenuScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
        AssetsManager.dipose();
		batch.dispose();
	}

}
