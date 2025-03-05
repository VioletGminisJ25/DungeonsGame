package io.FaiscaJsr.DungeonsGame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;

import io.FaiscaJsr.DungeonsGame.Managers.AssetsManager;

/**
 * Clase que representa el joystick virtual
 */
public class VirtualJoystick {
    private Stage stage;
    private Texture baseTexture;
    private Table table;
    private Texture knobTexture;
    private Touchpad touchpad;
    private Texture upTexture;
    private Texture downTexture;
    private boolean jumpPressed;
    public Image jumpImage;
    private Player player;

    /**
     * Set para el jugador
     *
     * @param player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Constructor del joystick virtual
     *
     * @param x          Posición en el eje X
     * @param y          Posición en el eje Y
     * @param baseRadius Radio del joystick base
     * @param knobRadius Radio del joystick knob
     */
    public VirtualJoystick(float x, float y, float baseRadius, float knobRadius) {
        stage = new Stage();
        table = new Table();
        table.setFillParent(true);
        table.bottom();
        table.pad(15);
        Skin skin = new Skin();
        skin.add("Joystick_base", AssetsManager.getTexture("img/joystick_base.png"));
        skin.add("Joystick_knob", AssetsManager.getTexture("img/joystick_knob.png"));
        Touchpad.TouchpadStyle touchpadStyle = new TouchpadStyle();
        touchpadStyle.background = skin.getDrawable("Joystick_base");
        touchpadStyle.knob = skin.getDrawable("Joystick_knob");
        touchpad = new Touchpad(10, touchpadStyle);
        touchpad.setSize(70, 70);
        table.add(touchpad).pad(70).size(370, 370);
        upTexture = new Texture("ui/ButtonAtack_34x34.png");
        downTexture = new Texture("ui/ButtonAtack_pressed_34x34.png");
        jumpImage = new Image(upTexture);
        jumpImage.setSize(100, 100);
        jumpImage.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                jumpPressed = true;
                jumpImage.setDrawable(new Image(downTexture).getDrawable());
                if (player != null && !player.isIsattack()) {
                    player.attack();
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                jumpPressed = false;
                Gdx.app.postRunnable(() -> {
                    jumpImage.setDrawable(new Image(upTexture).getDrawable());
                });
            }
        });
        table.add().expandX().fillX();
        table.add(jumpImage).size(touchpad.getWidth() + 128, touchpad.getHeight() + 128).pad(100);
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Método que devulelve la dirección del joystick
     *
     * @return Vector2 que representa el movimiento del joystick
     */
    public Vector2 getDirection() {
        float knobPercentX = touchpad.getKnobPercentX();
        float knobPercentY = touchpad.getKnobPercentY();
        knobPercentY *= 1;
        return new Vector2(knobPercentX, knobPercentY);
    }

    /**
     * Método que renderiza el joystick
     */
    public void render() {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * Método que redimensiona el joystick
     *
     * @param width  Ancho del joystick
     * @param height Alto del joystick
     */
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Método que libera los recursos utilizados por el joystick
     */
    public void dispose() {
    }
}
