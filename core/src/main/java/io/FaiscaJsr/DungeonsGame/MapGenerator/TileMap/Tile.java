package io.FaiscaJsr.DungeonsGame.MapGenerator.TileMap;

import com.badlogic.gdx.graphics.g2d.Sprite;

import io.FaiscaJsr.DungeonsGame.entities.Entity;

public abstract class Tile extends Entity{
	public static final int DIM = 32;
	// public static final int DIM = 16;
	private Sprite sprite;




    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Tile(float x, float y, Sprite sprite) {
		super(x, y, DIM, DIM);
		this.sprite = sprite;
		this.sprite.setPosition(x, y);
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
		
	}

}
