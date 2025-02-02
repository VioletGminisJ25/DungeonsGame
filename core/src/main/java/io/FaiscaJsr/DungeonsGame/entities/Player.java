package io.FaiscaJsr.DungeonsGame.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import io.FaiscaJsr.DungeonsGame.Screens.PlayScreen;

public class Player extends Sprite implements Disposable {
    public enum State {
        run, attack, idle
    };

    public State currentState;
    public State previousState;
    public Body body;
    public World world;
    private TextureRegion idleRegion;
    private Animation<TextureRegion> run;
    private Animation<TextureRegion> idle;
    private Animation<TextureRegion> attack;
    public boolean runningRight;
    public float stateTimer;
    public VirtualJoystick virtualJoystick;

    public Player(World world, PlayScreen screen, VirtualJoystick virtualJoystick) {
        super();
        this.world = world;

        idleRegion = new TextureRegion(screen.getTextureAtlas().findRegion("idle"), 0, 0, 100, 100);
        setRegion(idleRegion);
        setBounds(0, 0, 126, 39);
        definePlayer();
        setCenter(getWidth() / 2, getHeight() / 2);

        currentState = State.idle;
        previousState = State.idle;
        runningRight = true;
        stateTimer = 0;
        this.virtualJoystick = virtualJoystick;

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
        attack = new Animation<TextureRegion>(0.1f, attackRegions);
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

    private State getState() {// Contemplar los frames runUP
        if (body.getLinearVelocity().y > 1.5) {
            return State.run;
        }
        if (body.getLinearVelocity().y < -1.5) {
            return State.run;
        }
        if (body.getLinearVelocity().x > 1.5) {
            return State.run;
        }
        if (body.getLinearVelocity().x < -1.5) {
            return State.run;
        }
        if ((body.getLinearVelocity().x < 1.5 && body.getLinearVelocity().x > -1.5)
                && (body.getLinearVelocity().y < 1.5 && body.getLinearVelocity().y > -1.5)) {
            return State.idle;
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
        CircleShape shape = new CircleShape();
        shape.setRadius(5);
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
        shape.dispose();
        PolygonShape edgeShape = new PolygonShape();
        edgeShape.set(new Vector2[] {
                new Vector2(-80 / PlayScreen.PPM, 126 / PlayScreen.PPM),
                new Vector2(80/ PlayScreen.PPM, 126 / PlayScreen.PPM),
                new Vector2(-80 / PlayScreen.PPM, -39 / PlayScreen.PPM),
                new Vector2(80 / PlayScreen.PPM, -39 / PlayScreen.PPM)
        });
        fixtureDef.shape = edgeShape;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData("player");
        edgeShape.dispose();
    }

    public void HandleInput(float delta) {
        body.setLinearVelocity(virtualJoystick.getDirection().scl(6000f).scl(delta));
        // body.setLinearVelocity(virtualJoystick.getDirection().scl(5000f).scl(delta));

    }

    public void update(float delta) {
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight());
        setRegion(getFrame(delta));
    }

    @Override
    public float getHeight() {
        return super.getHeight() / 3;
    }

    @Override
    public void dispose() {
    }

}
