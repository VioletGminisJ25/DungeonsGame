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
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import io.FaiscaJsr.DungeonsGame.Main;
import io.FaiscaJsr.DungeonsGame.Managers.LanguageManager;

/**
 * Pantalla de ayuda
 *
 */
public class HelpScreen implements Screen {
    private Main game;
    private Stage stage;
    private Skin skin;
    private BitmapFont font;
    private BitmapFont font2;
    private StretchViewport viewport;
    private Sprite background;

    /**
     * Constructor
     *
     * @param game juego
     */
    public HelpScreen(Main game) {
        this.game = game;
        background = new Sprite(new Texture("GameOver/GameOverBackground.png"));
        background.setSize(1920, 1080);
        viewport = new StretchViewport(1920, 1080, new OrthographicCamera());
        stage = new Stage(viewport);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("GameOver/font2.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.genMipMaps = true;
        parameter.color = Color.WHITE;
        parameter.size = (int) Math.ceil(60);
        parameter.magFilter = TextureFilter.Linear;
        parameter.minFilter = TextureFilter.MipMapLinearNearest;
        font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose();

        Gdx.input.setInputProcessor(stage);

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = this.font;

        Label titleLabel = new Label(LanguageManager.get("helpTitle"), labelStyle);
        titleLabel.setAlignment(Align.center);

        Table scrollTable = new Table();
        Label contentLabel = new Label(LanguageManager.get("helpText"), labelStyle);

        contentLabel.setWrap(true);
        contentLabel.setAlignment(Align.center);

        scrollTable.add(contentLabel).width(Gdx.graphics.getWidth() * 0.8f).pad(20);

        ScrollPane scrollPane = new ScrollPane(scrollTable, new ScrollPaneStyle());
        scrollPane.setScrollingDisabled(true, false);

        TextButton.TextButtonStyle estiloBoton = new TextButton.TextButtonStyle();
        estiloBoton.font = this.font;
        estiloBoton.up = new TextureRegionDrawable(new Texture("GameOver/RestartButton.png"));
        estiloBoton.down = new TextureRegionDrawable(new Texture("GameOver/RestartButton.png"));

        TextButton backButton = new TextButton(LanguageManager.get("back"), estiloBoton);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
                dispose();
            }
        });

        rootTable.add(titleLabel).padBottom(20).row();
        rootTable.add(scrollPane).width(Gdx.graphics.getWidth() * 0.85f).height(Gdx.graphics.getHeight() * 0.6f)
                .padBottom(30).row();
        rootTable.add(backButton).height(50);

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
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        background.draw(game.batch);
        game.batch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * Cambia el tamaño de la ventana
     *
     * @see Screen#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
    }
}
