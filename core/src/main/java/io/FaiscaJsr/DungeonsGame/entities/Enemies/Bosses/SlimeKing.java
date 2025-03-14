package io.FaiscaJsr.DungeonsGame.entities.Enemies.Bosses;

import java.util.Random;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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

import io.FaiscaJsr.DungeonsGame.Managers.AssetsManager;
import io.FaiscaJsr.DungeonsGame.Managers.ManagerAudio;
import io.FaiscaJsr.DungeonsGame.Screens.PlayScreen;
import io.FaiscaJsr.DungeonsGame.entities.Player;
import io.FaiscaJsr.DungeonsGame.entities.Enemies.Enemy;

/**
 * Clase que representa al SlimeKing.
 *
 */
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
    private boolean moving;
    private PlayScreen screen;
    private Player player;
    public boolean nohit = false;
    private static Music sound = ManagerAudio.getMusic("fsx/slimeKing/walk_slime.wav");
    private float stepTimer = 0f;
    private float stepInterval = 0.3f;
    private Animation<TextureRegion> wander;
    private Animation<TextureRegion> dead;
    private Animation<TextureRegion> hit;
    private float timeAux = 0;
    private int contSoundDeath = 0;

    enum State {
        wander, attack, dead, idle, hit
    }

    /**
     * Constructor del SlimeKing.
     *
     * @param player    Player que controla al SlimeKing.
     * @param screen    Pantalla del juego.
     * @param world     Mundo del juego.
     * @param x         Posición en el eje X.
     * @param y         Posición en el eje Y.
     * @param maxHealth Valor máximo de vida.
     * @param damage    Daño que recibe el SlimeKing.
     * @param speed     Velocidad del SlimeKing.
     */
    public SlimeKing(Player player, PlayScreen screen, World world, float x, float y, int maxHealth, float damage,
            float speed) {
        super(player, world, AssetsManager.getTexture("Bosses/SlimeKing/SlimeKing_Walk_0.png"), x, y, maxHealth, damage, speed,
                screen);
        setBounds(x, y, 340 / PlayScreen.PPM, 340 / PlayScreen.PPM);
        this.screen = screen;
        this.world = world;
        this.moving = false;
        this.player = player;
        createBody(x, y);
        runningRight = true;
        random = new Random();
        isAttacking = false;
        isDead = false;
        animaciones();
    }

    /**
     * Métodos para crear las animaciones del SlimeKing.
     * El metodo consta de 4 animaciones: dead, wander, hit y idle.
     * Cada animación tiene una lista de regiones de texturas que se utilizarán para
     * crear la animación.
     */
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

        Array<TextureRegion> hitRegions = new Array<TextureRegion>();
        for (int i = 0; i < 2; i++) {
            hitRegions.add(new TextureRegion(atlas.findRegion("hit"), 0, i * 320, 320, 320));
        }
        hit = new Animation<TextureRegion>(0.15f, hitRegions);
    }

    /**
     * Método que devuelve la textura de la animación del SlimeKing segun el estado
     * actual.
     *
     * @param delta Tiempo transcurrido desde la última actualización.
     * @return Textura de la animación del SlimeKing.
     */
    private TextureRegion getFrame(float delta) {
        currentState = getState();
        TextureRegion region;
        switch (currentState) {
            case idle:
            case wander:
                region = wander.getKeyFrame(stateTimer, true);
                break;
            case dead:
                if (contSoundDeath == 0) {
                    Sound deathSound = ManagerAudio.getSound("fsx/slimeKing/SlimeKing_Death.wav");
                    screen.game.playSound(deathSound);
                    contSoundDeath++;
                }
                region = dead.getKeyFrame(stateTimer, false);
                setDestroyed(true);

                break;
            case hit:
                region = hit.getKeyFrame(stateTimer, false);
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

    /**
     * Método que actualiza los sonidos del SlimeKing cuando este está en
     * movimiento.
     *
     * @param dt
     */
    private void updateSounds(float dt) {
        if (!body.getLinearVelocity().isZero()) {
            stepTimer += dt;
            if (stepTimer >= stepInterval) {
                stepTimer = 0;
                if (!sound.isPlaying()) {

                    screen.game.playMusic(sound, false);
                }
            }
        } else {
            stepTimer = 0;
        }
    }

    /**
     * Método que devuelve el estado actual del SlimeKing.
     *
     * @return estado actual del SlimeKing.
     */
    private State getState() {
        if (isDead) {
            return State.dead;
        }
        if (ishit()) {
            return State.hit;
        }
        if (isAttacking) {
            return State.attack;
        }
        if (moving) {

            return State.wander;
        }
        return State.idle;

    }

    /**
     * Método que actualiza el SlimeKing.
     *
     * @param dt Tiempo transcurrido desde la última actualización.
     */
    @Override
    public void update(float dt) {
        super.update(dt);
        if (body != null) {
            setPosition(body.getPosition().x - getWidth() / 2 + 3, body.getPosition().y - getHeight() / 2);
            changeState(dt);
            updateSounds(dt);
        }
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
            if (ishit()) {
                setHit(false);
            }
        }

    }

    /**
     * Método que cambia el estado del SlimeKing en función de la distancia entre el
     * SlimeKing y el jugador.
     * El SlimeKing se mueve a una velocidad máxima de 3000 unidades por segundo
     * cuando está cerca del jugador.
     * El SlimeKing se mueve a una velocidad máxima de 1000 unidades por segundo
     * cuando está lejos del jugador.
     *
     * @param dt Tiempo transcurrido desde la última actualización.
     */
    public void changeState(float dt) {
        if (screen.player.body.getPosition().dst(body.getPosition()) > 100
                && screen.player.body.getPosition().dst(body.getPosition()) <= 150) {
            body.setLinearVelocity(
                    new Vector2(screen.player.body.getPosition().sub(body.getPosition()).nor().scl(3000).scl(dt)));
            moving = true;
        } else if (screen.player.body.getPosition().dst(body.getPosition()) <= 100) {
            body.setLinearVelocity(
                    new Vector2(screen.player.body.getPosition().sub(body.getPosition()).nor().scl(1000).scl(dt)));
            moving = true;
        } else {
            moving = false;
        }
    }

    /**
     * Método que crea el cuerpo del SlimeKing.
     *
     * @param x Posición en el eje X.
     * @param y Posición en el eje Y.
     */
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
        fixtureDef.shape = edgeShape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = PlayScreen.ENEMY_BIT_MASK;
        fixtureDef.filter.maskBits = PlayScreen.ATTACK_BIT_MASK | PlayScreen.PLAYER_BIT_MASK | PlayScreen.GOAL_BIT_MASK
                | PlayScreen.WALL_BIT_MASK | PlayScreen.ENEMY_BIT_MASK | PlayScreen.REVERSE_GOAL_BIT_MASK;
        body.createFixture(fixtureDef).setUserData(this);
        edgeShape.dispose();
    }

}
