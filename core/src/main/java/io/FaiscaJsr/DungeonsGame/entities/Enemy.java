package io.FaiscaJsr.DungeonsGame.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

public abstract class Enemy extends Sprite implements Disposable {

    private int currentHealth;
    private int maxHealth;
    private float damage;
    private float speed;
    public Body body;
    private World world;
    public static ArrayList<Enemy> enemiesToHit = new ArrayList<Enemy>();
    private Player player;
    protected boolean destroyed;
    public boolean enemyDead;
    private float cooldownDamage = 0f;

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
    }

    @Override
    public void dispose() {
        this.getTexture().dispose();
    }

    public static List<Enemy> enemiesToRemove = new ArrayList<>();

    public void update(float delta) {

        // // if (!enemiesToHit.isEmpty()) {

        // List<Enemy> toRemove = new ArrayList<>();

        // for (Enemy enemy : new ArrayList<>(enemiesToHit)) {
        // if (player.isattack) {
        // if (enemy.getCurrentHealth() > 0) {
        // enemy.hit(20, delta);
        // } else {
        // enemy.enemyDead = true;
        // toRemove.add(enemy);
        // }
        // }
        // }

        // // Remover enemigos después del loop para evitar modificar la lista mientras
        // // iteramos
        // for (Enemy enemy : toRemove) {
        // enemiesToHit.remove(enemy);
        // if (enemy.body != null && !world.isLocked()) { // Evita error si el mundo
        // está en uso
        // world.destroyBody(enemy.body);
        // }
        // }
    }

    public void draw(SpriteBatch batch) {
        if (!destroyed) {
            super.draw(batch);
        }
    }

    public abstract void createBody(float x, float y);

    public void hit(int damage, float delta) {

        System.out.println("Entro");
        cooldownDamage += delta;

        currentHealth = currentHealth - damage;
        System.out.println(currentHealth);
       

    }
}
