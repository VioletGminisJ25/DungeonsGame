package io.FaiscaJsr.DungeonsGame.entities.Enemies;

import java.util.Random;

import com.badlogic.gdx.audio.Sound;
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

import io.FaiscaJsr.DungeonsGame.Managers.ManagerAudio;
import io.FaiscaJsr.DungeonsGame.Screens.PlayScreen;
import io.FaiscaJsr.DungeonsGame.entities.Player;
import io.FaiscaJsr.DungeonsGame.entities.Items.Heart;
import io.FaiscaJsr.DungeonsGame.entities.Items.Time;

/**
 * Clase que representa al Slimes.
 */
public class Slimes extends Enemy {
    private World world;
    private Random random;
    private State currentState;
    private State previousState;
    private PlayScreen screen;
    private Animation<TextureRegion> move;
    private Animation<TextureRegion> attack;
    private Animation<TextureRegion> dead;
    private Animation<TextureRegion> idle;
    private Animation<TextureRegion> hit;
    private Animation<TextureRegion> jump;
    private Sound sound = ManagerAudio.getSound("fsx/slimes/slime_jump_1.wav");
    private float stateTimer;
    private float stepTimer = 0f;
    private float stepInterval = 1.65f;
    private boolean runningRight;
    private boolean isAttacking;
    private boolean isDead;
    private boolean moving;
    public boolean nohit = false;
    private int color;
    private int deathcont = 0;
    private int attackcont = 0;
    private int cantDrop = 0;

    enum State {
        move, attack, dead, idle, hit
    }

    /**
     * Constructor del Slimes.
     *
     * @param player    Player que controla al Slimes.
     * @param screen    Pantalla del juego.
     * @param world     Mundo del juego.
     * @param x         Posición en el eje X.
     * @param y         Posición en el eje Y.
     * @param maxHealth Valor máximo de vida.
     * @param damage    Daño que recibe el Slimes.
     * @param speed     Velocidad del Slimes.
     * @param color     Color del Slimes.
     */
    public Slimes(Player player, PlayScreen screen, World world, float x, float y, int maxHealth,
            float damage, float speed, int color) {
        super(player, world, new Texture("slimes/atlas/Slimes.png"), x, y, maxHealth, damage, speed, screen);
        setBounds(x, y, 240 / PlayScreen.PPM, 216 / PlayScreen.PPM);
        this.screen = screen;
        this.world = world;
        this.color = color * 72;
        this.moving = false;
        createBody(x, y);
        runningRight = false;
        random = new Random();
        isAttacking = false;
        isDead = false;
        animaciones();

    }

    /**
     * Métodos para crear las animaciones del Slimes.
     * El metodo consta de 4 animaciones: dead, idle, hit y attack.
     * Cada animación tiene una lista de regiones de texturas que se utilizarán para
     * crear la animación.
     */
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
        attack = new Animation<TextureRegion>(0.05f, attackRegions);
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

    /**
     * Método que actualiza los sonidos del Slimes cuando este está en
     * movimiento.
     *
     * @param dt Tiempo transcurrido desde la última actualización.
     */
    private void updateSounds(float dt) {
        if (!body.getLinearVelocity().isZero()) {
            stepTimer += dt;
            if (stepTimer >= stepInterval) {
                stepTimer = 0;
                screen.game.playSound(sound);
            }
        } else {
            stepTimer = 0;
        }
    }

    /**
     * Método que devuelve la textura de la animación del Slimes segun el estado
     * actual.
     *
     * @param delta Tiempo transcurrido desde la última actualización.
     * @return Textura de la animación del Slimes.
     */
    private TextureRegion getFrame(float delta) {
        currentState = getState();
        TextureRegion region;

        switch (currentState) {
            case idle:
                region = idle.getKeyFrame(stateTimer, true);
                break;
            case dead:
                if (deathcont == 0) {

                    Sound sound = ManagerAudio.getSound("fsx/slimes/death_slime_1.wav");
                    screen.game.playSound(sound);
                    deathcont++;
                }
                region = dead.getKeyFrame(stateTimer);
                setDestroyed(true);
                break;
            case hit:
                region = hit.getKeyFrame(stateTimer, false); // false = no loop

                break;
            case attack:
                if (attackcont == 0) {
                    Sound sound = ManagerAudio.getSound("fsx/slimes/slime_swallow_1.wav");
                    screen.game.playSound(sound);

                    attackcont++;
                }
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
     * Método que devuelve el estado actual del Slimes.
     *
     * @return estado actual del Slimes.
     */
    private State getState() {

        if (isDead) {
            return State.dead;
        } else if (ishitPlayer) {
            return State.attack;
        } else if (ishit()) {
            return State.hit;
        } else if (moving) {
            return State.move;
        } else {
            return State.idle;
        }
    }

    /**
     * Método que actualiza el Slimes.
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
                if (cantDrop == 0) {
                    dropItem(); // Llamamos al método para dropear el objeto
                    cantDrop++;
                }

                enemyDead = true;
            }
        }
        if (hit.isAnimationFinished(stateTimer)) {
            setHit(false);
        }
        if (attack.isAnimationFinished(stateTimer)) {
            ishitPlayer = false;
        }
    }

    /**
     * Método que cambia el estado del Slimes en función de la distancia entre el
     * Slimes y el jugador.
     * El Slimes se mueve a una velocidad máxima de 3000 unidades por segundo
     * cuando está cerca del jugador.
     * El Slimes se mueve a una velocidad máxima de 1000 unidades por segundo
     * cuando está lejos del jugador.
     *
     * @param dt Tiempo transcurrido desde la última actualización.
     */
    public void changeState(float dt) {
        if (screen.player.body.getPosition().dst(body.getPosition()) >= 100 && screen.player.body.getPosition()
                .dst(body.getPosition()) <= 150) {
            body.setLinearVelocity(
                    new Vector2(screen.player.body.getPosition().sub(body.getPosition()).nor().scl(5000).scl(dt)));
            moving = true;
        } else if (screen.player.body.getPosition().dst(body.getPosition()) <= 100) {
            body.setLinearVelocity(
                    new Vector2(screen.player.body.getPosition().sub(body.getPosition()).nor().scl(3000).scl(dt)));
            moving = true;
        } else {
            moving = false;
        }
    }

    /**
     * Método que crea el cuerpo del Slimes.
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
        shape.setRadius(7);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = PlayScreen.ENEMY_BIT_MASK;
        fixtureDef.filter.maskBits = PlayScreen.ATTACK_BIT_MASK | PlayScreen.PLAYER_BIT_MASK | PlayScreen.GOAL_BIT_MASK
                | PlayScreen.WALL_BIT_MASK | PlayScreen.ENEMY_BIT_MASK | PlayScreen.REVERSE_GOAL_BIT_MASK;
        body.createFixture(fixtureDef);
        shape.dispose();
        fixtureDef = new FixtureDef();
        PolygonShape edgeShape = new PolygonShape();
        edgeShape.setAsBox(60 / PlayScreen.PPM, 60 / PlayScreen.PPM);
        fixtureDef.shape = edgeShape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = PlayScreen.ENEMY_BIT_MASK;
        fixtureDef.filter.maskBits = PlayScreen.ATTACK_BIT_MASK | PlayScreen.PLAYER_BIT_MASK | PlayScreen.GOAL_BIT_MASK
                | PlayScreen.WALL_BIT_MASK | PlayScreen.ENEMY_BIT_MASK | PlayScreen.REVERSE_GOAL_BIT_MASK;
        body.createFixture(fixtureDef).setUserData(this);
        edgeShape.dispose();

    }

    /**
     * Método que solta un objeto al Slimes.
     * El Slimes soltará un Heart con una probabilidad de 20% y un Time con una
     * probabilidad de 10%.
     */
    private void dropItem() {
        Vector2 dropPosition = new Vector2(this.getX(), this.getY());

        if (random.nextInt(100) < 20) { // 10% de probabilidad de soltar un Heart
            Heart heart = new Heart(dropPosition.x, dropPosition.y);
            Heart.hearts.add(heart);
        } else if (random.nextInt(100) < 10) { // 10% de probabilidad de soltar un Time
            Time time = new Time(dropPosition.x, dropPosition.y);
            Time.time.add(time);
        }
    }

}
