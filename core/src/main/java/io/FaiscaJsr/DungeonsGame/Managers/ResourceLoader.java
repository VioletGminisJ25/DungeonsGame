package io.FaiscaJsr.DungeonsGame.Managers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.FaiscaJsr.DungeonsGame.MapGenerator.TileMap.Tile;

/**
 * Clase que carga lo tiles de la aplicación.
 */
public class ResourceLoader {
	static Texture tiles = AssetsManager.getTexture("tileset_complet.png");
    private static  Sprite floorSprite;
    private static Sprite wallSprite;
    private static Sprite cornerSprite;

    public static void load(){
        floorSprite = new Sprite(new TextureRegion(tiles,32,32,Tile.DIM,Tile.DIM));
        wallSprite = new Sprite(new TextureRegion(tiles,32,0,Tile.DIM,Tile.DIM));
        cornerSprite = new Sprite(new TextureRegion(tiles,0,0,Tile.DIM,Tile.DIM));
    }

    /**
     * Método que carga el sprite de la pared.
     * @return Sprite de la pared.
     */
	public static Sprite floorTile(){
		// int x = 32;
		// int y = 32;
		// return new Sprite(new TextureRegion(tiles,x,y,Tile.DIM,Tile.DIM));
        return floorSprite;
	}

    /**
     * Método que carga el sprite de la pared.
     * @param rotation Rotación del sprite.
     * @return Sprite de la pared.
     */
	public static Sprite wallTile(int rotation){
		// int x = 32;
		// int y = 0;
		// TextureRegion region = new TextureRegion(tiles,x,y,Tile.DIM,Tile.DIM);
		// Sprite sprite = new Sprite(region);
        Sprite sprite = wallSprite;
		sprite.rotate(rotation);
		return sprite;
	}

    /**
     * Método que carga el sprite de la pared.
     * @param rotation Rotación del sprite.
     * @return Sprite de la pared.
     */
	public static Sprite cornerTile(int rotation){
		// int x = 0;
		// int y = 0;
		// TextureRegion region = new TextureRegion(tiles,x,y,Tile.DIM,Tile.DIM);
		// Sprite sprite = new Sprite(region);
		Sprite sprite = cornerSprite;
		sprite.rotate(rotation);
		return sprite;
	}

    /**
     * Método que carga el sprite de la meta.
     * @param rotation Rotación del sprite.
     * @return Sprite de la meta.
     */
    public static Sprite goalTile(int rotation) {
        int x = Tile.DIM*3;
        int y = 0;
        TextureRegion region = new TextureRegion(tiles, x, y, Tile.DIM*2, Tile.DIM);
        Sprite sprite = new Sprite(region);
        sprite.rotate(rotation);
        return sprite;
    }

}
