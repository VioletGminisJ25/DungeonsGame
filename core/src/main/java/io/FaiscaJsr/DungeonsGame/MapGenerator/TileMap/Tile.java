package io.FaiscaJsr.DungeonsGame.MapGenerator.TileMap;

import com.badlogic.gdx.graphics.g2d.Sprite;

import io.FaiscaJsr.DungeonsGame.entities.Entity;

/**
 * Clase base para todos los tiles del juego.
 */
public abstract class Tile extends Entity {
    public static final int DIM = 32;
    private Sprite sprite;

    /**
     * Método que establece el sprite del tile.
     *
     * @param sprite
     */
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    /**
     * Método que establece la posición del sprite del tile.
     *
     * @param x      Posición en el eje X.
     * @param y      Posición en el eje Y.
     * @param sprite Sprite del tile.
     */
    public Tile(float x, float y, Sprite sprite) {
        super(x, y, DIM, DIM);
        this.sprite = sprite;
        this.sprite.setPosition(x, y);
    }

    /**
     * Método que actualiza el tile.
     *
     * @param dt Tiempo transcurrido desde la última actualización.
     */
    @Override
    public void dispose() {
        sprite.getTexture().dispose();
    }

    /**
     * Método que devuelve el sprite del tile.
     *
     * @return Sprite del tile.
     */
    @Override
    public Sprite getSprite() {
        return sprite;
    }

    /**
     * Método que actualiza el tile.
     *
     * @param dt Tiempo transcurrido desde la última actualización.
     */
    @Override
    public void update(float dt) {

    }

}
