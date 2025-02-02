package io.FaiscaJsr.DungeonsGame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;

import io.FaiscaJsr.DungeonsGame.ResourceLoader;

public class VirtualJoystick {
	private Stage stage;
	private Texture baseTexture;
	private Table table;
	private Texture knobTexture;
	private Vector2 basePosition;
	private Vector2 knobPosition;
	private float baseRadius;
	private float knobRadius;
	private boolean touched;
	private int touchPointer;
	private Touchpad touchpad;
	private boolean isJoystickVisible = false;

	public VirtualJoystick(float x, float y, float baseRadius, float knobRadius) {
		stage = new Stage();
		table = new Table();
		table.bottom().left();
		table.pad(15);

		Skin skin = new Skin();
		skin.add("Joystick_base", ResourceLoader.baseJoysitck());
		skin.add("Joystick_knob", ResourceLoader.knobJoystick());
		Touchpad.TouchpadStyle touchpadStyle = new TouchpadStyle();
		touchpadStyle.background = skin.getDrawable("Joystick_base");
		touchpadStyle.knob = skin.getDrawable("Joystick_knob");

		touchpad = new Touchpad(10, touchpadStyle);
		touchpad.setSize(40, 40);
		table.add(touchpad);
		stage.addActor(table);
		Gdx.input.setInputProcessor(stage);



	}

	public Vector2 getDirection() {
		float knobPercentX = touchpad.getKnobPercentX();
		float knobPercentY = touchpad.getKnobPercentY();
		knobPercentY *= 1;
		return new Vector2(knobPercentX, knobPercentY);
	}


	public void render() {
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}

	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	public void dispose() {
		baseTexture.dispose();
		knobTexture.dispose();
	}
}
