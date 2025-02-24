package io.FaiscaJsr.DungeonsGame.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import io.FaiscaJsr.DungeonsGame.Main;
import io.FaiscaJsr.DungeonsGame.Screens.PlayScreen;

public class Player extends Sprite implements Disposable {
    public enum State {
        run, attack, idle, hit, death
    };

    private int currentHealth;
    private int maxHealth;
    private float damage;
    private float speed;
    public State currentState;
    public State previousState;
    public Body body;
    public World world;
    private TextureRegion idleRegion;
    private Animation<TextureRegion> run;
    private Animation<TextureRegion> idle;
    private Animation<TextureRegion> attack;
    private Animation<TextureRegion> hit;
    public Animation<TextureRegion> death;
    public boolean runningRight;
    public float stateTimer;
    public VirtualJoystick virtualJoystick;
    public boolean isattack = false;
    private Body bodyAttack;
    public static int currentRoom = 0;
    private boolean isHit;
    private boolean isDead;
    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean isDead) {
        this.isDead = isDead;
    }

    private Main game;

    public boolean isIsattack() {
        return isattack;
    }

    public Player(World world, PlayScreen screen, VirtualJoystick virtualJoystick) {
        super();
        this.world = world;
        isHit = false;
        maxHealth = 100;
        currentHealth = maxHealth;
        isDead = false;
        game = screen.game;

        idleRegion = new TextureRegion(screen.getTextureAtlas().findRegion("idle"), 0, 0, 100, 100);
        setRegion(idleRegion);
        setBounds(0, 0, 126 * 1.1f, 39 * 1.1f);
        definePlayer();
        setCenter(126 * 1.1f / 2, 39 * 1.1f / 2);

        currentState = State.idle;
        previousState = State.idle;
        runningRight = true;
        stateTimer = 0;
        this.virtualJoystick = virtualJoystick;

        animationes(screen);
    }

    private void animationes(PlayScreen screen) {
        Array<TextureRegion> idleRegions = new Array<TextureRegion>();
        for (int i = 0; i < 5; i++) {
            idleRegions.add(new TextureRegion(screen.getTextureAtlas().findRegion("idle"), 0, i * 39, 126, 39));
        }
        idle = new Animation<TextureRegion>(0.1f, idleRegions);

        Array<TextureRegion> runRegions = new Array<TextureRegion>();
        for (int i = 0; i < 8; i++) {
            runRegions.add(new TextureRegion(screen.getTextureAtlas().findRegion("run"), 0, i * 39, 126, 39));
        }
        run = new Animation<TextureRegion>(0.1f, runRegions);

        Array<TextureRegion> attackRegions = new Array<TextureRegion>();
        for (int i = 0; i < 8; i++) {
            attackRegions.add(new TextureRegion(screen.getTextureAtlas().findRegion("attack"), 0, i * 39, 126, 39));
        }
        attack = new Animation<TextureRegion>(0.07f, attackRegions);

        Array<TextureRegion> hitRegions = new Array<TextureRegion>();
        for (int i = 0; i < 2; i++) {
            hitRegions.add(new TextureRegion(screen.getTextureAtlas().findRegion("hit"), 0, i * 39, 126, 39));
        }
        hit = new Animation<TextureRegion>(0.1f, hitRegions);
        Array<TextureRegion> deathRegions = new Array<TextureRegion>();
        for (int i = 0; i < 5; i++) {
            deathRegions.add(new TextureRegion(screen.getTextureAtlas().findRegion("death"), 0, i * 39, 126, 39));
        }
        death = new Animation<TextureRegion>(0.1f, deathRegions);
    }

    private TextureRegion getFrame(float delta) {
        currentState = getState();
        TextureRegion region;
        switch (currentState) {
            case idle:
                region = idle.getKeyFrame(stateTimer, true);
                break;
            case run:
                region = run.getKeyFrame(stateTimer, true);
                break;
            case attack:
                region = attack.getKeyFrame(stateTimer);
                body.setLinearVelocity(0, 0);
                break;
            case hit:
                region = hit.getKeyFrame(stateTimer, false);
                break;
            case death:
                region = death.getKeyFrame(stateTimer, false);

                break;
            default:
                region = idle.getKeyFrame(stateTimer, true);
                break;
        }
        if ((body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }
        stateTimer = currentState == previousState ? stateTimer + delta : 0;
        previousState = currentState;
        return region;
    }

    private State getState() {
        if (isDead) {
            return State.death;
        } else if (isHit) {
            return State.hit;
        } else if (isattack) {
            return State.attack;
        } else if (!body.getLinearVelocity().isZero()) {
            return State.run;
        } else {
            return State.idle;
        }
    }

    private void definePlayer() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(0, 0);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // bodyDef.linearDamping = 5f;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape edgeShape = new PolygonShape();
        edgeShape.set(new Vector2[] {
                new Vector2(-40 / PlayScreen.PPM, 60 / PlayScreen.PPM),
                new Vector2(40 / PlayScreen.PPM, 60 / PlayScreen.PPM),
                new Vector2(-40 / PlayScreen.PPM, -20 / PlayScreen.PPM),
                new Vector2(40 / PlayScreen.PPM, -20 / PlayScreen.PPM)
        });
        fixtureDef.shape = edgeShape;
        fixtureDef.filter.categoryBits = PlayScreen.PLAYER_BIT_MASK;

        fixtureDef.filter.maskBits = PlayScreen.WALL_BIT_MASK |
                PlayScreen.GOAL_BIT_MASK | PlayScreen.ENEMY_BIT_MASK | PlayScreen.ITEM_BIT_MASK
                | PlayScreen.REVERSE_GOAL_BIT_MASK;

        body.createFixture(fixtureDef).setUserData(this);
        edgeShape.dispose();
    }

    public void HandleInput(float delta) {
        if (!isDead) {
            if (!isattack) {
                body.setLinearVelocity(virtualJoystick.getDirection().scl(6500f).scl(delta));
            }
        } else {
            body.setLinearVelocity(0, 0);
        }
        // body.setLinearVelocity(virtualJoystick.getDirection().scl(5000f).scl(delta));

    }

    public void update(float delta) {
		setPosition(body.getPosition().x - getWidth() / 2 + 3, body.getPosition().y - getHeight());
		setRegion(getFrame(delta));
		if (isattack && attack.isAnimationFinished(stateTimer)) {
			isattack = false;
			if (bodyAttack != null) {
				world.destroyBody(bodyAttack);
				bodyAttack = null;
			}
		}
		if (hit.isAnimationFinished(stateTimer)) {
			isHit = false;
		}
		if (currentHealth <= 0) {
			isDead=true;
		}


	}

    @Override
    public float getHeight() {
        return super.getHeight() / 3;
    }

    public void attack() {
        if (!isattack) {
            isattack = true;
            createAttackBody();
        }
    }

    private void createAttackBody() {
        if (bodyAttack != null) {
            world.destroyBody(bodyAttack);
        }

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(body.getPosition().x, body.getPosition().y);
        bodyDef.linearDamping = 0f;
        bodyAttack = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 10000f;
        PolygonShape attackShape = new PolygonShape();

        if (!runningRight) {
            attackShape.set(new Vector2[] {
                    new Vector2(-31 / PlayScreen.PPM, 50 / PlayScreen.PPM),
                    new Vector2(-270 / PlayScreen.PPM, 50 / PlayScreen.PPM),
                    new Vector2(-31 / PlayScreen.PPM, -20 / PlayScreen.PPM),
                    new Vector2(-270 / PlayScreen.PPM, -20 / PlayScreen.PPM)
            });
        } else {
            attackShape.set(new Vector2[] {
                    new Vector2(31 / PlayScreen.PPM, 50 / PlayScreen.PPM),
                    new Vector2(270 / PlayScreen.PPM, 50 / PlayScreen.PPM),
                    new Vector2(31 / PlayScreen.PPM, -20 / PlayScreen.PPM),
                    new Vector2(270 / PlayScreen.PPM, -20 / PlayScreen.PPM)
            });
        }

        fixtureDef.shape = attackShape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = PlayScreen.ATTACK_BIT_MASK; // ?? attack
        fixtureDef.filter.maskBits = PlayScreen.ENEMY_BIT_MASK |
                PlayScreen.ITEM_BIT_MASK;

        Fixture attackFixture = bodyAttack.createFixture(fixtureDef);
        attackFixture.setUserData(this);

        attackShape.dispose();
    }

    @Override
    public void dispose() {
        world.destroyBody(body);
        if(bodyAttack!=null){
            world.destroyBody(bodyAttack);
        }
    }

    public void hit(int damage) {
        currentHealth = currentHealth - damage;
        isHit = true;
    }

}
