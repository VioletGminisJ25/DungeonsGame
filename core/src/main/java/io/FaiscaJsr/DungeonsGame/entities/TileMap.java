package io.FaiscaJsr.DungeonsGame.entities;

//TODO: Investigar sobre la generacion del suelo y paredes

public class TileMap {
	private static final int HEIGHT = 30;
	private static final int WIDTH =30;
	public enum GridSpace{empty,ground,wall}
	public GridSpace[][] grid;
	public TileMap() {
		setup();
	}

	public void setup(){

		grid = new GridSpace[WIDTH][HEIGHT];
		
		for(int i = 0;i<grid.length;i++){
			for(int j = 0;j<grid[i].length;j++){
				grid[i][j] = GridSpace.empty;
			}
		}
	}
}
