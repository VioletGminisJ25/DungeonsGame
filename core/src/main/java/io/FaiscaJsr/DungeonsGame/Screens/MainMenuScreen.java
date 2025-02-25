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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.FaiscaJsr.DungeonsGame.Main;
import io.FaiscaJsr.DungeonsGame.Managers.AssetsManager;

public class MainMenuScreen implements Screen {

    private Viewport viewport;
    private Stage stage;
    private Main game;
    private Sprite background;
    private BitmapFont font;

    public MainMenuScreen(Main game) {
        super();
        this.game = game;

        game.playMusic("MainMenu/sound/menuMusic.mp3",true);


        viewport = new StretchViewport(1920, 1080, new OrthographicCamera());
        stage = new Stage(viewport, ((Main) game).batch);
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
        Table table = new Table();
        table.setFillParent(true);
        table.center().setY(-100);
        // table.debug();
        table.row();
        TextButton startButton = new TextButton("Start Game", estiloBoton);
        table.add(startButton).pad(20);
        table.row();
        TextButton tutorialButton = new TextButton("Tutorial", estiloBoton);
        table.add(tutorialButton).pad(20);
        table.row();
        TextButton optionsButton = new TextButton("Options", estiloBoton);
        table.add(optionsButton).pad(20);
        table.row();
        TextButton exitButton = new TextButton("Exit", estiloBoton);
        table.add(exitButton).pad(20);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PlayScreen(game));
                dispose();
            }
        });

        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
                dispose();
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                Gdx.app.exit();
                System.exit(0);
            }
        });

        stage.addActor(table);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        background.draw(game.batch);
        game.batch.end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
