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
import io.FaiscaJsr.DungeonsGame.Tools.GamePreferences;

public class RecordsScreen implements Screen {
    private final Main game;
    private Stage stage;
    private Skin skin;
    private Table table;
    private ScrollPane scrollPane;
    private BitmapFont font;
    private Label.LabelStyle labelStyle;
    private Sprite background;

    public RecordsScreen(Main game) {
        this.game = game;
    }

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

        Label title = new Label("Records", labelStyle);
        title.setFontScale(2);
        title.setAlignment(Align.center);

        table = new Table();
        cargarRecords();

        TextButton btnVolver = new TextButton("Volver", estiloBoton);
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
        // mainTable.debug();
    }

    private void cargarRecords() {
        table.clear();
        String registros = GamePreferences.getPreferences().getString(GamePreferences.RUNS, "");

        if (registros.isEmpty()) {
            table.add(new Label("No hay records aún.", labelStyle)).pad(10).row();
        } else {
            String[] runs = registros.split(";");

            for (int i = 0; i < runs.length; i++) {
                String[] datos = runs[i].split(",");
                int vecesReloj = Integer.parseInt(datos[0]);
                float tiempoJugado = Float.parseFloat(datos[1]);

                Label runLabel = new Label("Run " + (i + 1) + ": " + tiempoJugado + "s - Relojes: " + vecesReloj,
                        labelStyle);
                table.add(runLabel).pad(5).row();
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        background.draw(game.batch);
        game.batch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
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
        stage.dispose();
        skin.dispose();
    }
}
