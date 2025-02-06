package io.FaiscaJsr.DungeonsGame.Entities.Bosses;

import java.util.Random;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import io.FaiscaJsr.DungeonsGame.Entities.Enemy;
import io.FaiscaJsr.DungeonsGame.Entities.Player;
import io.FaiscaJsr.DungeonsGame.Screens.PlayScreen;
import io.FaiscaJsr.DungeonsGame.Tools.SlimeData;

public class SlimeKing extends Enemy {
	private World world;
	private Random random;

	private State currentState;
	private State previousState;
	private float stateTimer;
	private boolean runningRight;
	private boolean isAttacking;
	private boolean isDead;
	private float timeSeconds = 0f;
	private float period = 0.5f;
	PlayScreen screen;
	public boolean nohit = false;

	enum State {
		wander, attack, dead, idle
	}

	private Animation<TextureRegion> wander;
	private Animation<TextureRegion> dead;
	private float timeAux = 0;

	public SlimeKing(Player player, PlayScreen screen, World world, float x, float y, int maxHealth, float damage, float speed) {
		super(player, world, new Texture("Bosses/SlimeKing/SlimeKing_Walk_0.png"), x, y, maxHealth, damage, speed);
		setBounds(x, y, 340 / PlayScreen.PPM, 340 / PlayScreen.PPM);
		this.screen = screen;
		this.world = world;
		createBody(x, y);
		random = new Random();
		isAttacking = false;
		isDead = false;
		animaciones();
	}

	private void animaciones() {
		TextureAtlas atlas = new TextureAtlas("Bosses/SlimeKing/atlas/SlimeKing.atlas");

		Array<TextureRegion> deadRegions = new Array<TextureRegion>();
		for (int i = 0; i < 4; i++) {
			deadRegions.add(new TextureRegion(atlas.findRegion("death"), 0, i * 320, 320, 320));
		}
		dead = new Animation<TextureRegion>(0.15f, deadRegions);

		Array<TextureRegion> wanderRegions = new Array<TextureRegion>();
		for (int i = 0; i < 4; i++) {
			wanderRegions.add(new TextureRegion(atlas.findRegion("wander"), 0, i * 320, 320, 320));
		}
		wander = new Animation<TextureRegion>(0.15f, wanderRegions);
	}

	private TextureRegion getFrame(float delta) {
		currentState = getState();
		TextureRegion region;
		switch (currentState) {
			case wander:
				region = wander.getKeyFrame(stateTimer, true);
				break;
			case dead:
				region = dead.getKeyFrame(stateTimer);
                
                if (dead.isAnimationFinished(stateTimer)) {
                    enemyDead = true;
                }
				break;

			default:
				region = wander.getKeyFrame(stateTimer, true);
				break;
		}
        if(!enemyDead){

            if ((body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
                region.flip(true, false);
                runningRight = false;
            } else if ((body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
                region.flip(true, false);
                runningRight = true;
            }
        }
		stateTimer = currentState == previousState ? stateTimer + delta : 0;
		previousState = currentState;
		return region;
	}

	private State getState() {
		State returnState;
		if (body.getLinearVelocity().y != 0 && body.getLinearVelocity().x != 0) {
			returnState = State.wander;
		} else {
			returnState = State.idle;
		}
		if (isAttacking) {
			returnState = State.attack;
		}
		if (isDead) {
			returnState = State.dead;
		}
		return returnState;
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2 + 2);
		changeState(dt);
		setRegion(getFrame(dt));
        if(getCurrentHealth() <= 0){
            isDead = true;
        }
	}

	public void changeState(float dt) {
		if (screen.player.body.getPosition().dst(body.getPosition()) > 100) {
			body.setLinearVelocity(
					new Vector2(screen.player.body.getPosition().sub(body.getPosition()).nor().scl(5000).scl(dt)));
		} else if (screen.player.body.getPosition().dst(body.getPosition()) <= 100
				&& screen.player.body.getPosition().dst(body.getPosition()) >= 40) {
			body.setLinearVelocity(
					new Vector2(screen.player.body.getPosition().sub(body.getPosition()).nor().scl(2000).scl(dt)));
		} else {
			body.setLinearVelocity(new Vector2(0, 0));
		}
	}

	@Override
	public void createBody(float x, float y) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x, y);
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.linearDamping = 70f;
		body = world.createBody(bodyDef);

		FixtureDef fixtureDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(16);
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef);
		shape.dispose();
		PolygonShape edgeShape = new PolygonShape();
		edgeShape.setAsBox(100 / PlayScreen.PPM, 100 / PlayScreen.PPM);
		// edgeShape.set(new Vector2[] {
		// new Vector2(-100 / PlayScreen.PPM, 100 / PlayScreen.PPM),
		// new Vector2(100 / PlayScreen.PPM, 100 / PlayScreen.PPM),
		// new Vector2(-100 / PlayScreen.PPM, -100 / PlayScreen.PPM),
		// new Vector2(100 / PlayScreen.PPM, -100 / PlayScreen.PPM)
		// });
		fixtureDef.shape = edgeShape;
		fixtureDef.isSensor = true;
		fixtureDef.filter.categoryBits = PlayScreen.ENEMY_BIT_MASK;
		fixtureDef.filter.maskBits = PlayScreen.PLAYER_BIT_MASK | PlayScreen.GOAL_BIT_MASK | PlayScreen.WALL_BIT_MASK;
		body.createFixture(fixtureDef).setUserData(new SlimeData(this, "damage"));
		edgeShape.dispose();
	}

}
