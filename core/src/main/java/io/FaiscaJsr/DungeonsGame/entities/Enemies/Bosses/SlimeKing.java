package io.FaiscaJsr.DungeonsGame.entities.Enemies.Bosses;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
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
import io.FaiscaJsr.DungeonsGame.entities.Player;
import io.FaiscaJsr.DungeonsGame.entities.Enemies.Enemy;

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
        wander, attack, dead, idle, hit
    }

    private Animation<TextureRegion> wander;
    private Animation<TextureRegion> dead;
    private Animation<TextureRegion> hit;
    private float timeAux = 0;

    public SlimeKing(Player player, PlayScreen screen, World world, float x, float y, int maxHealth, float damage,
            float speed) {
        super(player, world, new Texture("Bosses/SlimeKing/SlimeKing_Walk_0.png"), x, y, maxHealth, damage, speed);
        setBounds(x, y, 340 / PlayScreen.PPM, 340 / PlayScreen.PPM);
        this.screen = screen;
        this.world = world;
        createBody(x, y);
        runningRight = true;
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
            wanderRegions.add(new TextureRegion(atlas.findRegion("wander"),  0,i * 320, 320, 320));
        }
        wander = new Animation<TextureRegion>(0.15f, wanderRegions);

        Array<TextureRegion> hitRegions = new Array<TextureRegion>();
        for (int i = 0; i < 2; i++) {
            hitRegions.add(new TextureRegion(atlas.findRegion("hit"),  0,i * 320, 320, 320));
        }
        hit = new Animation<TextureRegion>(0.15f, hitRegions);
    }

    private TextureRegion getFrame(float delta) {
        currentState = getState();
        TextureRegion region;
        switch (currentState) {
            case idle:
            case wander:
                region = wander.getKeyFrame(stateTimer, true);
                break;
            case dead:
                region = dead.getKeyFrame(stateTimer, false);
                setDestroyed(true);

                break;
            case hit:
                region = hit.getKeyFrame(stateTimer, false);
                body.setLinearVelocity(0, 0);
                break;

            default:
                region = wander.getKeyFrame(stateTimer, true);
                break;
        }
        if (!enemyDead) {
            float velocityX = body.getLinearVelocity().x;

            boolean movingLeft = velocityX < 0;
            boolean movingRight = velocityX > 0;

            if (movingLeft) {
                if (!region.isFlipX()) {
                    region.flip(true, false);
                }
            } else {
                if (movingRight && region.isFlipX()) {
                    region.flip(true, false);
                }
            }
        }
        stateTimer = currentState == previousState ? stateTimer + delta : 0;
        previousState = currentState;
        return region;
    }

    private State getState() {
        if (isDead) {
            return State.dead;
        }
        if (ishit()) {
            System.out.println("ENTRO");
            return State.hit;
        }
        if (isAttacking) {
            return State.attack;
        }
        if (body.getLinearVelocity().y != 0 && body.getLinearVelocity().x != 0) {
            return State.wander;
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
        if (isDead) {

            if (dead.isAnimationFinished(stateTimer)) {
                enemyDead = true;
            }
        }
        if (hit.isAnimationFinished(stateTimer)) {
            if(ishit()){
                setHit(false);
            }
        }

    }

    public void changeState(float dt) {
        if (screen.player.body.getPosition().dst(body.getPosition()) > 100 &&screen.player.body.getPosition().dst(body.getPosition()) <= 150) {
            body.setLinearVelocity(
                    new Vector2(screen.player.body.getPosition().sub(body.getPosition()).nor().scl(3000).scl(dt)));
        } else if (screen.player.body.getPosition().dst(body.getPosition()) <= 100) {
            body.setLinearVelocity(
                    new Vector2(screen.player.body.getPosition().sub(body.getPosition()).nor().scl(1000).scl(dt)));
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
        shape.setRadius(20);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = PlayScreen.ENEMY_BIT_MASK;
        fixtureDef.filter.maskBits = PlayScreen.ATTACK_BIT_MASK | PlayScreen.PLAYER_BIT_MASK | PlayScreen.GOAL_BIT_MASK
                | PlayScreen.WALL_BIT_MASK | PlayScreen.ENEMY_BIT_MASK | PlayScreen.REVERSE_GOAL_BIT_MASK;
        body.createFixture(fixtureDef);
        shape.dispose();
        fixtureDef = new FixtureDef();
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
        fixtureDef.filter.maskBits = PlayScreen.ATTACK_BIT_MASK | PlayScreen.PLAYER_BIT_MASK | PlayScreen.GOAL_BIT_MASK | PlayScreen.WALL_BIT_MASK | PlayScreen.ENEMY_BIT_MASK | PlayScreen.REVERSE_GOAL_BIT_MASK;
        body.createFixture(fixtureDef).setUserData(this);
        edgeShape.dispose();
    }

}
