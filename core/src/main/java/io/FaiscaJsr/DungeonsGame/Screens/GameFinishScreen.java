package io.FaiscaJsr.DungeonsGame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.FaiscaJsr.DungeonsGame.Main;
import io.FaiscaJsr.DungeonsGame.Managers.LanguageManager;
import io.FaiscaJsr.DungeonsGame.Managers.TimeManager;
import io.FaiscaJsr.DungeonsGame.Tools.GamePreferences;
import io.FaiscaJsr.DungeonsGame.entities.Items.Time;

/**
 * Clase que representa la pantalla de finalización del juego
 *
 */
public class GameFinishScreen implements Screen {

    private Viewport viewport;
    private Stage stage;
    private Main game;
    private Sprite background;
    private BitmapFont font;

    /**
     * Constructor de la clase
     * Guarda la cantidad de veces que se ha jugado y la cantidad de tiempo que se
     * ha recogido
     *
     * @param game   juego
     * @param screen pantalla del juego
     */
    public GameFinishScreen(Main game, PlayScreen screen) {
        super();
        this.game = game;
        GamePreferences.guardarRun(Time.times, screen.timeManager.toString());
        Time.times = 0;
        TimeManager.timeLeft = 5 * 60;
        viewport = new StretchViewport(1920, 1080, new OrthographicCamera());
        stage = new Stage(viewport, ((Main) game).batch);
        Gdx.input.setInputProcessor(stage);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("GameOver/font2.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.genMipMaps = true;
        parameter.color = Color.WHITE;
        parameter.size = (int) Math.ceil(60);
        parameter.magFilter = TextureFilter.Linear;
        parameter.minFilter = TextureFilter.MipMapLinearNearest;
        font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        TextButton.TextButtonStyle estiloBoton = new TextButton.TextButtonStyle();
        estiloBoton.font = this.font;
        estiloBoton.up = new TextureRegionDrawable(new Texture("GameOver/RestartButton.png"));
        estiloBoton.down = new TextureRegionDrawable(new Texture("GameOver/RestartButton.png"));

        TextButton.TextButtonStyle estiloBotonExit = new TextButton.TextButtonStyle();
        estiloBotonExit.font = this.font;
        estiloBotonExit.up = new TextureRegionDrawable(new Texture("GameOver/ExitButton.png"));
        estiloBotonExit.down = new TextureRegionDrawable(new Texture("GameOver/ExitButton.png"));

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = this.font;

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        Label title = new Label(LanguageManager.get("win"), labelStyle);
        title.setAlignment(Align.center);
        table.add(title).size(400, 400).center();

        table.row();

        TextButton exitButton = new TextButton(LanguageManager.get("back"),
                estiloBotonExit);
        exitButton.pad(20);
        table.add(exitButton);
        stage.addActor(table);
        background = new Sprite(new Texture("GameOver/GameOverBackground.png"));
        background.setSize(1920, 1080);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
    }

    /**
     * Muestra la pantalla
     *
     * @see Screen#show()
     */
    @Override
    public void show() {

    }

    /**
     * Muestra la pantalla
     *
     * @see Screen#render(float)
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        background.draw(game.batch);
        game.batch.end();
        stage.draw();
    }

    /**
     * Cambia el tamaño de la ventana
     *
     * @see Screen#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {
    }

    /**
     * Pausa la animación
     *
     * @see Screen#pause()
     */
    @Override
    public void pause() {
    }

    /**
     * Reanuda la animación
     *
     * @see Screen#resume()
     */
    @Override
    public void resume() {
    }

    /**
     * Oculta la pantalla
     *
     * @see Screen#hide()
     */
    @Override
    public void hide() {
    }

    /**
     * Libera recursos
     *
     * @see Screen#dispose()
     */
    @Override
    public void dispose() {
        background.getTexture().dispose();
        stage.dispose();
    }

}
