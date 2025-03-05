package io.FaiscaJsr.DungeonsGame.MapGenerator.TileMap;

import io.FaiscaJsr.DungeonsGame.Managers.ResourceLoader;

/**
 * Clase que representa el suelo.
 */
public class Floor extends Tile {

    /**
     * Constructor de suelo.
     *
     * @param x               Posición en el eje X.
     * @param y               Posición en el eje Y.
     * @param rotationDegrees Rotación del suelo.
     */
    public Floor(float x, float y, int rotationDegrees) {
        super(x, y, ResourceLoader.floorTile());
        super.getSprite().setRotation(rotationDegrees);
    }

}
