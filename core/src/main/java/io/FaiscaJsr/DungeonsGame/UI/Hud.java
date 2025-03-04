package io.FaiscaJsr.DungeonsGame.UI;

import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.FaiscaJsr.DungeonsGame.Managers.AssetsManager;
import io.FaiscaJsr.DungeonsGame.Screens.PlayScreen;

public class Hud implements Disposable {
	// Elementos principales del HUD
	public Stage stage; // Escenario donde se renderizan los elementos del HUD
	private Viewport viewport; // Viewport para manejar la resolución del HUD

	// Fuentes para los textos del HUD
	private BitmapFont font;
	private BitmapFont littleFont;
	private Table leftTable;

	private PlayScreen screen;
	private Label healthLabel;
	private Label timeLabel;

	/**
	 * Constructor del HUD.
	 * 
	 * @param batch SpriteBatch utilizado para renderizar los elementos del HUD.
	 */
	public Hud(PlayScreen screen, SpriteBatch batch) {
		// Configuración del viewport y el escenario
		viewport = new FitViewport(1920, 1080, new OrthographicCamera());
		stage = new Stage(viewport, batch);
		this.screen = screen;

		// Inicialización de variables

		// // Cargar la fuente del HUD
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("GameOver/font2.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.genMipMaps = true;
		parameter.color = Color.WHITE;
		parameter.size = (int) Math.ceil(60);
		parameter.magFilter = TextureFilter.Linear;
		parameter.minFilter = TextureFilter.MipMapLinearNearest;
		font = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose(); // don't forget to dispose to avoid memory leaks!

		// // Cargar imágenes de corazones para representar vida
		TextureRegion heart = new TextureRegion(AssetsManager.getTexture("ui/hearts.png"), 32, 32);

		// // Crear la tabla principal para organizar el HUD
		Table rootTable = new Table();
		rootTable.top();
		rootTable.setSize(1920 / 5, 1080 / 5);
		rootTable.setFillParent(true);

		// Tabla izquierda para mostrar las vidas
		leftTable = new Table();
		leftTable.add(new Image(heart)).size(128, 128).pad(50).left();

		Label.LabelStyle labelStyle = new Label.LabelStyle();
		labelStyle.font = this.font;

		healthLabel = new Label("", labelStyle);
		healthLabel.setAlignment(Align.center);
		leftTable.add(healthLabel).size(32, 32).pad(5).left();

		Table rightTable = new Table();

		timeLabel = new Label("", labelStyle);
		// timeLabel.setAlignment(Align.center);
		rightTable.add(new Image(AssetsManager.getTexture("ui/time_1.png"))).size(128, 128).pad(50).right();
		rightTable.add(timeLabel).size(32, 32).padRight(200).right();

		// Tabla derecha para mostrar cerezas y gemas

		// Agregar tablas al HUD
		rootTable.add(leftTable).expandX().left().top();
		rootTable.add(rightTable).expandX().right().top();
		stage.addActor(rootTable);
		// rightTable.debug();
		// rootTable.debug();

	}

	public void updateHud() {
		// Actualiza la visualización de la vida
		healthLabel.setText(screen.player.getCurrentHealth()); // TODO: Cambiar fuente
		timeLabel.setText(screen.timeManager.toString());
	}

	/**
	 * Libera los recursos utilizados por el HUD.
	 */
	@Override
	public void dispose() {
		font.dispose();
		littleFont.dispose();
		stage.dispose();
	}

}