package io.FaiscaJsr.DungeonsGame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.FaiscaJsr.DungeonsGame.entities.TileMap.Tile;

public class ResourceLoader {
	static Texture tiles = new Texture("tileset_complet.png");
	public static Sprite floorTile(){
		int x = 32;
		int y = 32;
		return new Sprite(new TextureRegion(tiles,x,y,Tile.DIM,Tile.DIM));
	}
	public static Sprite wallTile(int rotation){
		int x = 32;
		int y = 0;
		TextureRegion region = new TextureRegion(tiles,x,y,Tile.DIM,Tile.DIM);
		Sprite sprite = new Sprite(region);
		sprite.rotate(rotation);
		return sprite;
	}
	public static Sprite cornerTile(int rotation){
		int x = 0;
		int y = 0;
		TextureRegion region = new TextureRegion(tiles,x,y,Tile.DIM,Tile.DIM);
		Sprite sprite = new Sprite(region);
		sprite.rotate(rotation);
		return sprite;
	}
    public static Sprite goalTile(int rotation) {
        int x = Tile.DIM*3;
        int y = 0;
        TextureRegion region = new TextureRegion(tiles, x, y, Tile.DIM*2, Tile.DIM);
        Sprite sprite = new Sprite(region);
        sprite.rotate(rotation);
        return sprite;
    }
	public static Texture baseJoysitck(){
		return new Texture("img/joystick_base.png");
	}
	public static Texture knobJoystick(){
		return new Texture("img/joystick_knob.png");
	}

}
