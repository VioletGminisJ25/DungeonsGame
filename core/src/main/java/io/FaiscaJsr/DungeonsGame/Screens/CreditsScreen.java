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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.FaiscaJsr.DungeonsGame.Main;
import io.FaiscaJsr.DungeonsGame.Managers.LanguageManager;

/**
 * Pantalla de creditos
 */
public class CreditsScreen implements Screen {
    private Main game;
    private Stage stage;
    private BitmapFont font;
    private ScrollPane scrollPane;
    private float scrollSpeed = 40f; // Velocidad de desplazamiento
    private Sprite background;

    /**
     * Constructor de la clase
     *
     * @param game
     */
    public CreditsScreen(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("GameOver/font2.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.genMipMaps = true;
        parameter.color = Color.WHITE;
        parameter.size = (int) Math.ceil(60);
        parameter.magFilter = TextureFilter.Linear;
        parameter.minFilter = TextureFilter.MipMapLinearNearest;
        font = generator.generateFont(parameter);
        generator.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = this.font;

        Label titleLabel = new Label(LanguageManager.get("creditsTitle"), labelStyle);

        Table scrollTable = new Table();
        Label creditsLabel = new Label(LanguageManager.get("creditsText"),
                labelStyle);
        creditsLabel.setWrap(true);
        creditsLabel.setAlignment(1);

        scrollTable.add(creditsLabel).width(Gdx.graphics.getWidth() * 0.8f).pad(20);

        scrollPane = new ScrollPane(scrollTable, new ScrollPaneStyle());
        scrollPane.setScrollingDisabled(true, false);

        TextButton.TextButtonStyle estiloBotonExit = new TextButton.TextButtonStyle();
        estiloBotonExit.font = this.font;
        estiloBotonExit.up = new TextureRegionDrawable(new Texture("GameOver/ExitButton.png"));
        estiloBotonExit.down = new TextureRegionDrawable(new Texture("GameOver/ExitButton.png"));

        TextButton backButton = new TextButton(LanguageManager.get("back"), estiloBotonExit);
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
        background = new Sprite(new Texture("GameOver/GameOverBackground.png"));
        background.setSize(1920, 1080);
    }

    /**
     * Método que muestra la pantalla
     *
     * @see Screen#show()
     */
    @Override
    public void show() {
    }

    /**
     * Método que dibuja la pantalla
     *
     * @param delta tiempo transcurrido desde la última actualización
     * @see Screen#render(float)
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        background.draw(game.batch);
        game.batch.end();
        float scrollY = scrollPane.getScrollY() + scrollSpeed * delta;
        scrollPane.setScrollY(scrollY);
        stage.act(delta);
        stage.draw();
    }

    /**
     * Método que redimensiona la pantalla
     *
     * @param width  ancho de la pantalla
     * @param height alto de la pantalla
     * @see Screen#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Método que pausa la pantalla
     *
     * @see Screen#pause()
     */
    @Override
    public void pause() {
    }

    /**
     * Método que reanuda la pantalla
     *
     * @see Screen#resume()
     */
    @Override
    public void resume() {
    }

    /**
     * Método que oculta la pantalla
     *
     * @see Screen#hide()
     */
    @Override
    public void hide() {
    }

    /**
     * Método que libera memoria
     *
     * @see Screen#dispose()
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}
