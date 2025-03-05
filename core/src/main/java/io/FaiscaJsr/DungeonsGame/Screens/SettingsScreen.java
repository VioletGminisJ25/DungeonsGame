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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.FaiscaJsr.DungeonsGame.Main;
import io.FaiscaJsr.DungeonsGame.Managers.LanguageManager;
import io.FaiscaJsr.DungeonsGame.Tools.GamePreferences;

/**
 * Pantalla de configuración
 */
public class SettingsScreen implements Screen {

    private Viewport viewport;
    private Stage stage;
    private Main game;
    private Sprite background;
    private BitmapFont font;
    private float musicVolume = 0.5f, soundVolume = 0.5f;
    private boolean isVibrationOn, isMusicOn, isSoundOn;
    private float lastMusicVolume, lastSoundVolume;

    /**
     * Constructor
     *
     * @param game Instancia de Main
     */
    public SettingsScreen(Main game) {
        this.game = game;
        musicVolume = GamePreferences.getMusicVolume();
        soundVolume = GamePreferences.getSoundVolume();
        isVibrationOn = GamePreferences.isVibrationEnabled();
        isMusicOn = GamePreferences.getMusicVolume() > 0;
        isSoundOn = GamePreferences.getSoundVolume() > 0;
        lastMusicVolume = GamePreferences.getMusicVolume();
        lastSoundVolume = GamePreferences.getSoundVolume();

        viewport = new StretchViewport(1920, 1080, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);
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

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.up = new TextureRegionDrawable(new Texture("MainMenu/button2.png"));
        buttonStyle.down = new TextureRegionDrawable(new Texture("MainMenu/button2.png"));

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = new TextureRegionDrawable(new Texture("MainMenu/slider_bg.png"));
        sliderStyle.knob = new TextureRegionDrawable(new Texture("MainMenu/slider_knob.png"));

        Table table = new Table();
        table.setFillParent(true);
        table.center().setY(-150); // Esto centra la tabla en la pantalla

        Table leftColumn = new Table();
        Table rightColumn = new Table();

        Label volumeLabel = new Label(LanguageManager.get("music"), new Label.LabelStyle(font, Color.WHITE));
        Slider volumeSlider = new Slider(0, 1, 0.01f, false, sliderStyle);
        volumeSlider.setValue(GamePreferences.getMusicVolume());
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float value = volumeSlider.getValue();
                musicVolume = value;
                GamePreferences.setMusicVolume(value);
                game.updateMusicVolume();
            }
        });

        Label vibrationLabel = new Label(LanguageManager.get("vibration"), new Label.LabelStyle(font, Color.WHITE));
        TextButton vibrationButton = new TextButton("On", buttonStyle);
        vibrationButton.setText(GamePreferences.isVibrationEnabled() ? "On" : "Off");
        vibrationButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isVibrationOn = !isVibrationOn;
                GamePreferences.setVibration(isVibrationOn);
                if (isVibrationOn) {
                    vibrationButton.setText("On");
                } else {
                    vibrationButton.setText("Off");
                }
            }
        });

        Label languageLabel = new Label(LanguageManager.get("language"), new Label.LabelStyle(font, Color.WHITE));
        TextButton languageButton = new TextButton(GamePreferences.getLanguage().equals("en") ? "English" : "Español",
                buttonStyle);
        languageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String nuevoIdioma = GamePreferences.getLanguage().equals("es") ? "en" : "es";
                GamePreferences.setLanguage(nuevoIdioma);
                LanguageManager.setLanguage(nuevoIdioma);
                LanguageManager.loadLanguage();

                // Recargar la pantalla con el nuevo idioma
                game.setScreen(new SettingsScreen(game));
                dispose();
            }
        });
        Label SfxLabel = new Label(LanguageManager.get("sfx"), new Label.LabelStyle(font, Color.WHITE));
        Slider sfxSlider = new Slider(0, 1, 0.01f, false, sliderStyle);
        sfxSlider.setValue(GamePreferences.getSoundVolume());
        sfxSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float value = sfxSlider.getValue();
                soundVolume = value;
                GamePreferences.setSoundVolume(value);
            }
        });

        TextButton backButton = new TextButton(LanguageManager.get("back"), buttonStyle);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });

        leftColumn.add(volumeLabel).pad(40).left().center();
        leftColumn.row();
        leftColumn.add(SfxLabel).pad(40).left().center();
        leftColumn.row();
        leftColumn.add(languageLabel).pad(40).right().center();
        leftColumn.row();
        leftColumn.add(vibrationLabel).pad(40).left().center();

        rightColumn.add(volumeSlider).width(400).pad(20).left().center();
        rightColumn.row();
        rightColumn.add(sfxSlider).width(400).pad(20).left().center();
        rightColumn.row();
        rightColumn.add(languageButton).pad(20).right().center();
        rightColumn.row();
        rightColumn.add(vibrationButton).center();

        // Añadir las columnas centradas con un pequeño espacio entre ellas
        table.add(leftColumn).center();
        table.add(rightColumn).center();
        table.row();

        // Centrar el botón "Back" en su propia fila
        TextButton creditsButton = new TextButton(LanguageManager.get("credits"), buttonStyle);
        creditsButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new CreditsScreen(game));
                dispose();
            }
        });

        TextButton helpButton = new TextButton(LanguageManager.get("help"), buttonStyle);
        helpButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new HelpScreen(game));
                dispose();
            }
        });

        table.row(); // Nueva fila

        table.add(creditsButton).colspan(1).padTop(10).center(); // Botón Créditos
        table.add(helpButton).colspan(2).padTop(10).center(); // Botón Ayuda
        table.row(); // Nueva fila
        table.add(backButton).colspan(2).padTop(20).center();

        stage.addActor(table);
        // table.debug();

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
     * Actualiza la pantalla
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
        viewport.update(width, height, true);
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
