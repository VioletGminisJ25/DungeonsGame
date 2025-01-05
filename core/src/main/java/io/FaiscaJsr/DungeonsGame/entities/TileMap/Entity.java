package io.FaiscaJsr.DungeonsGame.entities.TileMap;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {

	protected Vector2 position;
	private Rectangle hitbox;
	public abstract Sprite getSprite();
	public Entity(float x, float y, int width, int height) {
		position = new Vector2(x,y);
		hitbox = new Rectangle(x,y,width,height);
		hitbox.getPosition(position);
	}
	public abstract void update(float dt);

	public abstract void dispose();

	public boolean collidesWith(Entity other) {
		return hitbox.overlaps(other.hitbox);
	}
}