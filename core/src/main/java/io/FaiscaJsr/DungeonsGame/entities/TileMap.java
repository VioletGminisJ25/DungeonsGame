package io.FaiscaJsr.DungeonsGame.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import io.FaiscaJsr.DungeonsGame.ResourceLoader;

//TODO: Investigar sobre la generacion del suelo y paredes

public class TileMap {
	private static final int HEIGHT = 5;
	public static int getHeight() {
		return HEIGHT;
	}

	private static final int WIDTH = 10;
	public static int getWidth() {
		return WIDTH;
	}

	private Vector2 initialPosition;

	public enum GridSpace {
		empty, floor, wall
	}

	public GridSpace[][] grid;

	public TileMap(Vector2 position) {
		setup();
		this.initialPosition = position;
	}

	public void setup() {

		grid = new GridSpace[WIDTH][HEIGHT];
		System.out.println();
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (i == 0 || j == 0 || j == grid[i].length - 1 || i == grid.length - 1) {
					grid[i][j] = GridSpace.wall;
				} else {
					grid[i][j] = GridSpace.floor;
				}

				System.out.print(grid[i][j]);

			}
			System.out.println();
		}

	}

	public void createFloors(SpriteBatch batch) {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				switch (grid[i][j]) {
					case floor:
						Floor floor = new Floor(initialPosition.x + (i*32), initialPosition.y + (j*32),0);
						floor.getSprite().draw(batch);
						break;

					case wall:
						// Don't use batch.draw(sprite, ...) Use sprite.draw(batch). This is due to an unfortunate design decision to make Sprite extend from TextureRegion. But if you draw it like a TextureRegion, all its position and rotation properties are ignored.
						// Vamos que si son sprites utilices sprite.draw entiendo
						if (i == 0) {
							if(j==0){
								Corner corner = new Corner(initialPosition.x + (i*32), initialPosition.y + (j*32),90);
								corner.getSprite().draw(batch);
							}if(j==grid[i].length-1){
								Corner corner = new Corner(initialPosition.x + (i*32), initialPosition.y + (j*32),0);
								corner.getSprite().draw(batch);
							}
							if((j!=0)&&(j!=grid[i].length-1)){
								Wall wall = new Wall(initialPosition.x + (i*32), initialPosition.y + (j*32),90);
								wall.getSprite().draw(batch);
							}
							
						}else if(i == grid.length-1){
							if(j==0){
								Corner corner = new Corner(initialPosition.x + (i*32), initialPosition.y + (j*32),180);
								corner.getSprite().draw(batch);
							}if(j==grid[i].length-1){
								Corner corner = new Corner(initialPosition.x + (i*32), initialPosition.y + (j*32),270);
								corner.getSprite().draw(batch);
							}
							if((j!=0)&&(j!=grid[i].length-1)){
								Wall wall = new Wall(initialPosition.x + (i*32), initialPosition.y + (j*32),-90);
								wall.getSprite().draw(batch);
								
							}
						}else{
							if(j!=0){
								Wall wall = new Wall(initialPosition.x + (i*32), initialPosition.y + (j*32),0);
								wall.getSprite().draw(batch);
							}if(j!=grid[i].length-1){
								Wall wall = new Wall(initialPosition.x + (i*32), initialPosition.y + (j*32),180);
								wall.getSprite().draw(batch);
							}
						}
					
						break;

					default:
						break;
				}

			}
		}

	}
}
