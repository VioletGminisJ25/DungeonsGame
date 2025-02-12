package io.FaiscaJsr.DungeonsGame.entities;

import java.util.Random;

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

import io.FaiscaJsr.DungeonsGame.Screens.PlayScreen;

public class Slimes extends Enemy {//TODO CORREGIR ANIMACION DE HIT RAQUITICA xD
	private World world;
	private Random random;

	private State currentState;
	private State previousState;
	private float stateTimer;
	private boolean runningRight;
	private boolean isAttacking;
	private boolean isDead;
	PlayScreen screen;
	public boolean nohit = false;
	public boolean ishit = false;
    private int color;

	enum State {
		move, attack, dead, idle, hit
	}

	private Animation<TextureRegion> move;
	private Animation<TextureRegion> attack;
	private Animation<TextureRegion> dead;
	private Animation<TextureRegion> idle;
	private Animation<TextureRegion> hit;
	private Animation<TextureRegion> jump; //???


	public Slimes(Player player, PlayScreen screen, World world, float x, float y, int maxHealth,
			float damage, float speed,int color) {
		super(player, world, new Texture("slimes/atlas/Slimes.png"), x, y, maxHealth, damage, speed);
		setBounds(x, y, 240 / PlayScreen.PPM, 216 / PlayScreen.PPM);
		this.screen = screen;
		this.world = world;
        this.color = color*72;
		createBody(x, y);
		random = new Random();
		isAttacking = false;
		isDead = false;
		animaciones();

	}

	private void animaciones() {
		TextureAtlas atlas = new TextureAtlas("slimes/atlas/Slimes.atlas");
		Array<TextureRegion> idleRegions = new Array<TextureRegion>();
		for (int i = 0; i < 2; i++) {
			idleRegions.add(new TextureRegion(atlas.findRegion("idle1"), i * 80, color, 80, 72));
		}
		idle = new Animation<TextureRegion>(0.15f, idleRegions);
		Array<TextureRegion> attackRegions = new Array<TextureRegion>();
		for (int i = 0; i < 14; i++) {
			attackRegions.add(new TextureRegion(atlas.findRegion("attack"), i * 80, color, 80, 72));
		}
		attack = new Animation<TextureRegion>(0.15f, attackRegions);
		Array<TextureRegion> deadRegions = new Array<TextureRegion>();
		for (int i = 0; i < 13; i++) {
			deadRegions.add(new TextureRegion(atlas.findRegion("dead"), i * 80, color, 80, 72));
		}
		dead = new Animation<TextureRegion>(0.15f, deadRegions);
		Array<TextureRegion> hitRegions = new Array<TextureRegion>();
		for (int i = 0; i < 2; i++) {
			hitRegions.add(new TextureRegion(atlas.findRegion("hit"), i * 80, color, 80, 72));
		}
		hit = new Animation<TextureRegion>(0.30f, hitRegions);
		Array<TextureRegion> jumpArray = new Array<TextureRegion>();
		for (int i = 0; i < 11; i++) {
			jumpArray.add(new TextureRegion(atlas.findRegion("jump"), i * 80, color, 80, 72));
		}
		jump = new Animation<TextureRegion>(0.15f, jumpArray);
	}

	private TextureRegion getFrame(float delta) {
		currentState = getState();
		TextureRegion region;
		switch (currentState) {
			case idle:
				region = idle.getKeyFrame(stateTimer, true);
				break;
			case dead:
				region = dead.getKeyFrame(stateTimer);
				if (dead.isAnimationFinished(stateTimer)) {
					enemyDead = true;
				}
				break;
			case hit:
				region = hit.getKeyFrame(stateTimer, false); // false = no loop
				body.setLinearVelocity(0, 0);
				if (hit.isAnimationFinished(stateTimer)) {
					ishit = false; // Solo se desactiva cuando termina la animación
				}
				break;
			case attack:
				region = attack.getKeyFrame(stateTimer, true);
				break;
			case move:
				region = jump.getKeyFrame(stateTimer, true);
				break;

			default:
				region = idle.getKeyFrame(stateTimer, true);
				break;
		}
		if (!enemyDead) {

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

	// public void hit(int damage, float delta) {

	// 	super.setCooldownDamage(super.getCooldownDamage() + delta);
	// 	if (super.getCooldownDamage() > 1f) {
	// 		super.setCooldownDamage(0);
	// 		super.setCurrentHealth(super.getCurrentHealth() - damage);
	// 		ishit = true;
	// 		System.out.println(super.getCurrentHealth());
	// 	}

	// }

	private State getState() {

		if (isDead) {
			return State.dead;
		}
		if (ishit) {
			return State.hit;
		}
		if (isAttacking) {
			return State.attack;
		}
		if (body.getLinearVelocity().y != 0 && body.getLinearVelocity().x != 0) {
			return State.move;
		}

		return State.idle;
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		setPosition(body.getPosition().x - getWidth() / 2 + 3, body.getPosition().y - getHeight() / 2);
		changeState(dt);
		setRegion(getFrame(dt));
		if (getCurrentHealth() <= 0) {
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
					new Vector2(screen.player.body.getPosition().sub(body.getPosition()).nor().scl(3000).scl(dt)));
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
		shape.setRadius(7);
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef);
		shape.dispose();
		PolygonShape edgeShape = new PolygonShape();
		edgeShape.setAsBox(60 / PlayScreen.PPM, 60 / PlayScreen.PPM);
		fixtureDef.shape = edgeShape;
		fixtureDef.filter.categoryBits = PlayScreen.ENEMY_BIT_MASK;
		fixtureDef.filter.maskBits = PlayScreen.ATTCK_BIT_MASK |PlayScreen.PLAYER_BIT_MASK | PlayScreen.GOAL_BIT_MASK | PlayScreen.WALL_BIT_MASK |PlayScreen.ENEMY_BIT_MASK;
		body.createFixture(fixtureDef).setUserData(this);
		edgeShape.dispose();

	}

}
