package io.FaiscaJsr.DungeonsGame.entities.Items;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import io.FaiscaJsr.DungeonsGame.Managers.AssetsManager;
import io.FaiscaJsr.DungeonsGame.Screens.PlayScreen;

public class Time extends Sprite {
	private Animation<TextureRegion> animation;
	public static ArrayList<Time> time = new ArrayList<Time>();
	private float stateTimer;
	private Body body;
	public static int times = 0;

	public Time(float x, float y) {
		super();
		stateTimer = 0;

		Array<TextureRegion> timeRegion = new Array<TextureRegion>();

		for (int i = 0; i < 5; i++) {
			timeRegion.add(new TextureRegion(AssetsManager.getTexture("ui/time.png"), i * 512, 0, 512, 512));
		}
		animation = new Animation<TextureRegion>(0.1f, timeRegion);
		setBounds(x, y, 64 / PlayScreen.PPM, 64 / PlayScreen.PPM);
		defineBody();
		body.setTransform(x, y, 0);
	}

	private TextureRegion getFrame(float delta) {
		TextureRegion region;

		region = animation.getKeyFrame(stateTimer, true);
		stateTimer = stateTimer + delta;
		return region;
	}

	public static void draw(float delta, SpriteBatch batch) {
		if (time != null || time.size() == 0) {
			for (Time time : time) {
				time.setRegion(time.getFrame(delta));
				time.draw(batch);
			}
		}
	}

	private void defineBody() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		// bodyDef.linearDamping = 5f;

		body = PlayScreen.world.createBody(bodyDef);

		FixtureDef fixtureDef = new FixtureDef();
		PolygonShape edgeShape = new PolygonShape();
		edgeShape.set(new Vector2[] {
				new Vector2(0 / PlayScreen.PPM, 50 / PlayScreen.PPM),
				new Vector2(50 / PlayScreen.PPM, 0 / PlayScreen.PPM),
				new Vector2(0 / PlayScreen.PPM, 0 / PlayScreen.PPM),
				new Vector2(50 / PlayScreen.PPM, 50 / PlayScreen.PPM)
		});
		fixtureDef.shape = edgeShape;
		fixtureDef.isSensor = true;
		fixtureDef.filter.categoryBits = PlayScreen.ITEM_BIT_MASK;

		fixtureDef.filter.maskBits = PlayScreen.PLAYER_BIT_MASK;

		Fixture f = body.createFixture(fixtureDef);
		f.setUserData(this);

		edgeShape.dispose();
	}

	public void destroy() {
		// PlayScreen.world.destroyBody(body);
		// body = null;
		Time.time.remove(this);
		PlayScreen.bodiesToRemove.add(body);
		times++;

	}

}
