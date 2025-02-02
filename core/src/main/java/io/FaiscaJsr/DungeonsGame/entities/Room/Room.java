package io.FaiscaJsr.DungeonsGame.entities.Room;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.FaiscaJsr.DungeonsGame.entities.Goal;
import io.FaiscaJsr.DungeonsGame.entities.Player;
import io.FaiscaJsr.DungeonsGame.entities.TileMap.Corner;
import io.FaiscaJsr.DungeonsGame.entities.TileMap.Floor;
import io.FaiscaJsr.DungeonsGame.entities.TileMap.Tile;
import io.FaiscaJsr.DungeonsGame.entities.TileMap.Wall;

public class Room {

    private int ID;

    public boolean playerSpawn;

    private int HEIGHT;

    public int getHeight() {
        return HEIGHT;

    }

    private int WIDTH;

    public int getWidth() {
        return WIDTH;
    }

    private Vector2 initialPosition;

    public Vector2 getInitialPosition() {
        return initialPosition;
    }

    public enum GridSpace {
        floor, wall, goal
    }

    public GridSpace[][] grid;

    public Rectangle hitbox;
    public Vector2 center;
    public ArrayList<Floor> floors;
    public ArrayList<Wall> walls;
    public ArrayList<Corner> corners;
    public Goal goal;
    private Random rnd = new Random();
    private Player player;

    public Room(int id, Vector2 position, int width, int height,Player player) {
        this.ID = id;
        this.initialPosition = position;
        this.WIDTH = width;
        this.HEIGHT = height;
        this.player = player;
        hitbox = new Rectangle(initialPosition.x - 128, initialPosition.y + 128, WIDTH * 32 + 128, HEIGHT * 32 + 128);
        this.center = new Vector2(Math.round((initialPosition.x + ((width / 2) * Tile.DIM))),
                Math.round((initialPosition.y + (height * Tile.DIM / 2))));
        System.out.println("Initial position: " + initialPosition);
        System.out.println("Width: " + WIDTH);
        System.out.println("Height: " + height);
        System.out.println("Center X:" + (initialPosition.x + (width / 2)));
        System.out.println("Center Y:" + (initialPosition.y + (height / 2)));
        System.out.println("");
        floors = new ArrayList<Floor>();
        walls = new ArrayList<Wall>();
        corners = new ArrayList<Corner>();
        // System.out.println("Room: " + hitbox);
    }

    public void setup() {

        grid = new GridSpace[WIDTH][HEIGHT];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (i == 0 || j == 0 || j == grid[i].length - 1 || i == grid.length - 1) {
                    grid[i][j] = GridSpace.wall;
                } else {
                    grid[i][j] = GridSpace.floor;
                }

            }
        }
        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == GridSpace.wall) {
                    if (j >0 &&i!=0) {
                        if (i + 1 < grid.length) {
                            if (grid[i + 1][j] == GridSpace.wall) {
                                if (count <= 0 || count < 1) {

                                    grid[i][j] = GridSpace.goal;
                                    grid[i + 1][j] = GridSpace.goal;
                                    count++;

                                }
                            }
                        }
                    }

                }
            }

        }
    }

    public void load() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                switch (grid[i][j]) {
                    case floor:
                        Floor floor = new Floor(initialPosition.x + (i * Tile.DIM), initialPosition.y + (j * Tile.DIM),
                                0);
                        floors.add(floor);
                        // floor.getSprite().draw(batch);
                        break;

                    case wall:
                        // Don't use batch.draw(sprite, ...) Use sprite.draw(batch). This is due to an
                        // unfortunate design decision to make Sprite extend from TextureRegion. But if
                        // you draw it like a TextureRegion, all its position and rotation properties
                        // are ignored.
                        // Vamos que si son sprites utilices sprite.draw entiendo
                        if (i == 0) {
                            if (j == 0) {
                                Corner corner = new Corner(initialPosition.x + (i * Tile.DIM),
                                        initialPosition.y + (j * Tile.DIM), 90);
                                // corner.getSprite().draw(batch);
                                corners.add(corner);
                            }
                            if (j == grid[i].length - 1) {
                                Corner corner = new Corner(initialPosition.x + (i * Tile.DIM),
                                        initialPosition.y + (j * Tile.DIM), 0);
                                // corner.getSprite().draw(batch);
                                corners.add(corner);
                            }
                            if ((j != 0) && (j != grid[i].length - 1)) {
                                Wall wall = new Wall(initialPosition.x + (i * Tile.DIM),
                                        initialPosition.y + (j * Tile.DIM), 90);
                                // wall.getSprite().draw(batch);
                                walls.add(wall);
                            }

                        } else if (i == grid.length - 1) {
                            if (j == 0) {
                                Corner corner = new Corner(initialPosition.x + (i * Tile.DIM),
                                        initialPosition.y + (j * Tile.DIM), 180);
                                // corner.getSprite().draw(batch);
                                corners.add(corner);
                            }
                            if (j == grid[i].length - 1) {
                                Corner corner = new Corner(initialPosition.x + (i * Tile.DIM),
                                        initialPosition.y + (j * Tile.DIM), 270);
                                // corner.getSprite().draw(batch);
                                corners.add(corner);
                            }
                            if ((j != 0) && (j != grid[i].length - 1)) {
                                Wall wall = new Wall(initialPosition.x + (i * Tile.DIM),
                                        initialPosition.y + (j * Tile.DIM), -90);
                                // wall.getSprite().draw(batch);
                                walls.add(wall);

                            }
                        } else {
                            if (j != 0) {
                                Wall wall = new Wall(initialPosition.x + (i * Tile.DIM),
                                        initialPosition.y + (j * Tile.DIM), 0);
                                // wall.getSprite().draw(batch);
                                walls.add(wall);
                            }
                            if (j != grid[i].length - 1) {
                                Wall wall = new Wall(initialPosition.x + (i * Tile.DIM),
                                        initialPosition.y + (j * Tile.DIM), 180);
                                // wall.getSprite().draw(batch);
                                walls.add(wall);
                            }
                        }

                        break;
                    case goal:
                        Floor floor2 = new Floor(initialPosition.x + (i * Tile.DIM), initialPosition.y + (j * Tile.DIM),
                                0);
                        floors.add(floor2);
                        Goal goal = new Goal(initialPosition.x + (i * Tile.DIM/2),
                                initialPosition.y + (j * Tile.DIM), 0,player);
                        this.goal = goal;
                        break;

                    default:
                        break;
                }

            }
        }

    }

    public void draw(SpriteBatch batch) {

        for (Wall wall : walls) {
            wall.getSprite().draw(batch);
        }
        for (Corner corner : corners) {
            corner.getSprite().draw(batch);
        }
        for (Floor floor : floors) {
            // System.out.println("floor");
            batch.draw(floor.getSprite(), floor.position.x, floor.position.y);
        }
        goal.getSprite().draw(batch);
    }

    public boolean intersects(Room room) {
        return (initialPosition.x <= room.initialPosition.x + room.WIDTH
                && initialPosition.x + this.WIDTH >= room.initialPosition.x
                && initialPosition.y <= room.initialPosition.y + room.HEIGHT
                && room.initialPosition.y + room.HEIGHT >= room.initialPosition.y);
    }

    public boolean collidesWith(Room other) {
        return hitbox.overlaps(other.hitbox);
    }
}
