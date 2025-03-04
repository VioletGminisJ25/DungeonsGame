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

import io.FaiscaJsr.DungeonsGame.Managers.AssetsManager;
import io.FaiscaJsr.DungeonsGame.Screens.PlayScreen;

public class Heart extends Sprite {
	private Animation<TextureRegion> animation;
	public static ArrayList<Heart> hearts = new ArrayList<Heart>();
	private float stateTimer;
	private Body body;

	public Heart(float x, float y) {
		super();
		stateTimer = 0;

		Array<TextureRegion> heartRegions = new Array<TextureRegion>();

		for (int i = 0; i < 4; i++) {
			heartRegions.add(new TextureRegion(AssetsManager.getTexture("ui/hearts.png"), i * 32, 0, 32, 32));
		}
		animation = new Animation<TextureRegion>(0.1f, heartRegions);
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
		if (hearts != null || hearts.size() == 0) {
			for (Heart heart : hearts) {
				heart.setRegion(heart.getFrame(delta));
				heart.draw(batch);
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

		Heart.hearts.remove(this);
	}

}
