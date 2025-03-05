package io.FaiscaJsr.DungeonsGame.entities.Enemies;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import io.FaiscaJsr.DungeonsGame.Managers.ManagerAudio;
import io.FaiscaJsr.DungeonsGame.Screens.PlayScreen;
import io.FaiscaJsr.DungeonsGame.entities.Player;

/**
 * Clase base para todos los enemigos del juego.
 */
public abstract class Enemy extends Sprite implements Disposable {

	private int currentHealth;
	private int maxHealth;
	private float damage;
	private float speed;
	public Body body;
	private World world;
	public static ArrayList<Enemy> enemiesToHit = new ArrayList<Enemy>();
	private Player player;
	private boolean destroyed;
	public boolean ishitPlayer;
	public boolean enemyDead;
	private float cooldownDamage = 0f;
	private boolean ishit = false;
	private int hitanimationCont = 0;
	public static List<Enemy> enemiesToRemove = new ArrayList<>();
	private PlayScreen screen;

	/**
	 * Indica si el enemigo esta destruido
	 *
	 * @return true si el enemigo esta destruido, false en caso contrario
	 */
	public boolean isDestroyed() {
		return destroyed;
	}

	/**
	 * Establece el estado de destruido del enemigo
	 *
	 * @param destroyed true si el enemigo esta destruido, false en caso contrario
	 */
	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}

	/**
	 * Get para saber si el enemigo esta golpeado
	 *
	 * @return true si el enemigo esta golpeado, false en caso contrario
	 */
	public boolean ishit() {
		return ishit;
	}

	/**
	 * Set de golpeado
	 *
	 * @param hit true si el enemigo esta golpeado, false en caso contrario
	 */
	public void setHit(boolean hit) {
		ishit = hit;
	}

	/**
	 * Get para saber el timpo de espera entre golpes
	 *
	 * @return el timpo de espera entre golpes
	 */
	public float getCooldownDamage() {
		return cooldownDamage;
	}

	/**
	 * Set para el timpo de espera entre golpes
	 *
	 * @param cooldownDamage el timpo de espera entre golpes
	 */
	public void setCooldownDamage(float cooldownDamage) {
		this.cooldownDamage = cooldownDamage;
	}

	/**
	 * Get para saber la salud actual del enemigo
	 *
	 * @return la salud actual del enemigo
	 */
	public int getCurrentHealth() {
		return currentHealth;
	}

	/**
	 * Set para la salud actual del enemigo
	 *
	 * @param currentHealth la salud actual del enemigo
	 */
	public void setCurrentHealth(int currentHealth) {
		this.currentHealth = currentHealth;
	}

	/**
	 * Get para saber la salud maximo del enemigo
	 *
	 * @return la salud maximo del enemigo
	 */
	public int getMaxHealth() {
		return maxHealth;
	}

	/**
	 * Set para la salud maximo del enemigo
	 *
	 * @param maxHealth la salud maximo del enemigo
	 */
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	/**
	 * Get para saber el daño que recibe el enemigo
	 *
	 * @return el daño que recibe el enemigo
	 */
	public float getDamage() {
		return damage;
	}

	/**
	 * Set para el daño que recibe el enemigo
	 *
	 * @param damage el daño que recibe el enemigo
	 */
	public void setDamage(float damage) {
		this.damage = damage;
	}

	/**
	 * Get para saber la velocidad del enemigo
	 *
	 * @return la velocidad del enemigo
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * Set para la velocicdad del enemigo
	 *
	 * @param speed la velocidad del enemigo
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	/**
	 * Constructor del Enemigo
	 *
	 * @param player    Player que controla al Enemigo
	 * @param world     Mundo del juego
	 * @param texture   Textura del Enemigo
	 * @param x         Posición en el eje X
	 * @param y         Posición en el eje Y
	 * @param maxHealth Valor máximo de vida
	 * @param damage    Daño que recibe el Enemigo
	 * @param speed     Velocidad del Enemigo
	 * @param screen    Pantalla del juego
	 */
	protected Enemy(Player player, World world, Texture texture, float x, float y, int maxHealth, float damage,
			float speed, PlayScreen screen) {
		super(texture);
		this.currentHealth = maxHealth;
		this.maxHealth = maxHealth;
		this.damage = damage;
		this.speed = speed;
		this.world = world;
		this.player = player;
		this.screen = screen;
		destroyed = false;
		enemyDead = false;
		ishit = false;
		ishitPlayer = false;
	}

	/**
	 * Método que actualiza el Enemigo
	 *
	 * @param dt Tiempo transcurrido desde la última actualización
	 */
	public void update(float dt) {
	}

	/**
	 * Método que dibuja el Enemigo
	 * Se llama automáticamente en la actualización del juego
	 *
	 * @param batch SpriteBatch que se utiliza para dibujar el Enemigo
	 */
	public void draw(SpriteBatch batch) {
		super.draw(batch);

	}

	/**
	 * Método que crea el cuerpo del Enemigo
	 *
	 * @param x Posición en el eje X
	 * @param y Posición en el eje Y
	 */
	public abstract void createBody(float x, float y);

	/**
	 * Método que recibe daño al Enemigo
	 *
	 * @param damage Daño que recibe el Enemigo
	 * @param delta  Tiempo transcurrido desde la última actualización
	 */
	public void hit(int damage, float delta) {
		Sound sound = ManagerAudio.getSound("fsx/slimeKing/SlimeKing_Hit.wav");
		screen.game.playSound(sound);
		currentHealth = currentHealth - damage;
		ishit = true;
	}

	/**
	 * Libera los recursos utilizados por el Enemigo
	 */
	@Override
	public void dispose() {
		this.getTexture().dispose();
	}
}
