package io.FaiscaJsr.DungeonsGame.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Clase base para todos los objetos del juego.
 */
public abstract class Entity {
    public Vector2 position;
    private Rectangle hitbox;

    public abstract Sprite getSprite();

    /**
     * Constructor del Entity
     *
     * @param x      Posición en el eje X
     * @param y      Posición en el eje Y
     * @param width  Ancho del Entity
     * @param height Alto del Entity
     */
    public Entity(float x, float y, int width, int height) {
        position = new Vector2(x, y);
        hitbox = new Rectangle(x, y, width, height);
        hitbox.getPosition(position);
    }

    /**
     * Método que actualiza el Entity
     *
     * @param dt Tiempo transcurrido desde la última actualización
     */
    public abstract void update(float dt);

    /**
     * Método que libera los recursos utilizados por el Entity
     */
    public abstract void dispose();

    /**
     * Método que verifica si dos Entities se cruzan
     *
     * @param other Entity que se compara con el Entity actual
     * @return true si los Entity se cruzan, false en caso contrario
     */
    public boolean collidesWith(Entity other) {
        return hitbox.overlaps(other.hitbox);
    }
}
