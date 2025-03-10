package io.FaiscaJsr.DungeonsGame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.FaiscaJsr.DungeonsGame.Main;
import io.FaiscaJsr.DungeonsGame.Managers.LanguageManager;
import io.FaiscaJsr.DungeonsGame.Tools.GamePreferences;

/**
 * Pantalla de los Records
 *
 */
public class RecordsScreen implements Screen {
    private final Main game;
    private Stage stage;
    private Skin skin;
    private Table table;
    private ScrollPane scrollPane;
    private BitmapFont font;
    private Label.LabelStyle labelStyle;
    private Sprite background;

    /**
     * Constructor
     *
     * @param game Instancia de Main
     */
    public RecordsScreen(Main game) {
        this.game = game;
    }

    /**
     * Muestra la pantalla
     *
     * @see Screen#show()
     */
    @Override
    public void show() {
        // GamePreferences.getPreferences().remove(GamePreferences.RUNS);
        // GamePreferences.getPreferences().flush();
        stage = new Stage(new ScreenViewport());
        background = new Sprite(new Texture(Gdx.files.internal("MainMenu/upscalemedia-transformed.jpeg")));
        background.setSize(1920, 1080);
        Gdx.input.setInputProcessor(stage);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("GameOver/font2.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.genMipMaps = true;
        parameter.color = Color.WHITE;
        parameter.size = (int) Math.ceil(40);
        parameter.magFilter = TextureFilter.Linear;
        parameter.minFilter = TextureFilter.MipMapLinearNearest;
        // generator.scaleForPixelHeight((int) 20);
        // parameter.genMipMaps = false;
        // parameter.magFilter = TextureFilter.Nearest;
        font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        TextButton.TextButtonStyle estiloBoton = new TextButton.TextButtonStyle();
        estiloBoton.font = this.font;
        estiloBoton.up = new TextureRegionDrawable(new Texture("MainMenu/button2.png"));
        estiloBoton.down = new TextureRegionDrawable(new Texture("MainMenu/button2.png"));

        labelStyle = new Label.LabelStyle();
        labelStyle.font = this.font;

        Label title = new Label(LanguageManager.get("records"), labelStyle);
        title.setFontScale(2);
        title.setAlignment(Align.center);

        table = new Table();
        cargarRecords();

        TextButton btnVolver = new TextButton(LanguageManager.get("back"), estiloBoton);
        btnVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        Table mainTable = new Table();
        scrollPane = new ScrollPane(table, new ScrollPaneStyle());
        mainTable.setFillParent(true);
        mainTable.bottom();
        mainTable.add(title).pad(10).row();
        mainTable.add(scrollPane).expandX().fillY().height(500).padBottom(20).row();
        mainTable.add(btnVolver).pad(10);

        stage.addActor(mainTable);
    }

    /**
     * Carga los registros
     */
    private void cargarRecords() {
        table.clear();
        String registros = GamePreferences.getPreferences().getString(GamePreferences.RUNS, "");

        if (registros.isEmpty()) {
            table.add(new Label(LanguageManager.get("norecords"), labelStyle)).pad(10).row();
        } else {
            String[] runs = registros.split(";");

            for (int i = 0; i < runs.length; i++) {
                String[] datos = runs[i].split(",");
                int vecesReloj = Integer.parseInt(datos[0]);
                String tiempoJugado = datos[1];

                Label runLabel = new Label("Run " + (i + 1) + ": " + tiempoJugado + " - " + LanguageManager.get("watch")
                        + ": " + vecesReloj, labelStyle);
                table.add(runLabel).pad(5).row();
            }
        }
    }

    /**
     * Actualiza la pantalla
     *
     * @see Screen#render(float)
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        background.draw(game.batch);
        game.batch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
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
        stage.dispose();
        skin.dispose();
    }
}
