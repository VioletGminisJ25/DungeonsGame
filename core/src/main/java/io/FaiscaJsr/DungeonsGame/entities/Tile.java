package io.FaiscaJsr.DungeonsGame.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class Tile extends Entity{
	public static final int DIM = 16;
	private Sprite sprite;

	// public enum TileType {
	// 	wall, floor
	// }

	public Tile(float x, float y, Sprite sprite) {
		super(x, y, DIM, DIM);
		this.sprite = sprite;
	}

	@Override
	public void dispose() {
		sprite.getTexture().dispose();
	}

	@Override
	public Sprite getSprite() {
		return sprite;
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
	}
	
}
