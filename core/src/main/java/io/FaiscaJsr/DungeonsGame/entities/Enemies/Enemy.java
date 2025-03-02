package io.FaiscaJsr.DungeonsGame.entities.Enemies;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import io.FaiscaJsr.DungeonsGame.entities.Player;

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
	public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public boolean enemyDead;
	private float cooldownDamage = 0f;
	private boolean ishit = false;
	private int hitanimationCont = 0;

	public boolean ishit() {
		return ishit;
	}

	public void setHit(boolean hit) {
		ishit = hit;
	}

	public float getCooldownDamage() {
		return cooldownDamage;
	}

	public void setCooldownDamage(float cooldownDamage) {
		this.cooldownDamage = cooldownDamage;
	}

	public int getCurrentHealth() {
		return currentHealth;
	}

	public void setCurrentHealth(int currentHealth) {
		this.currentHealth = currentHealth;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public float getDamage() {
		return damage;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

    public static List<Enemy> enemiesToRemove = new ArrayList<>();

	protected Enemy(Player player, World world, Texture texture, float x, float y, int maxHealth, float damage,
			float speed) {
		super(texture);
		this.currentHealth = maxHealth;
		this.maxHealth = maxHealth;
		this.damage = damage;
		this.speed = speed;
		this.world = world;
		this.player = player;
		destroyed = false;
		enemyDead = false;
        ishit = false;
        ishitPlayer = false;
	}

	@Override
	public void dispose() {
		this.getTexture().dispose();
	}


	public void update(float dt) {


	}

	public void draw(SpriteBatch batch) {
			super.draw(batch);

	}

	public abstract void createBody(float x, float y);

	public void hit(int damage, float delta) {
		currentHealth = currentHealth - damage;
		ishit = true;
	}
}
