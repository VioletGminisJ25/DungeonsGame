package io.FaiscaJsr.DungeonsGame.MapGenerator.Room;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import io.FaiscaJsr.DungeonsGame.Managers.RoomManager;
import io.FaiscaJsr.DungeonsGame.MapGenerator.TileMap.Corner;
import io.FaiscaJsr.DungeonsGame.MapGenerator.TileMap.Floor;
import io.FaiscaJsr.DungeonsGame.MapGenerator.TileMap.Goal;
import io.FaiscaJsr.DungeonsGame.MapGenerator.TileMap.ReverseGoal;
import io.FaiscaJsr.DungeonsGame.MapGenerator.TileMap.Tile;
import io.FaiscaJsr.DungeonsGame.MapGenerator.TileMap.Wall;
import io.FaiscaJsr.DungeonsGame.Screens.PlayScreen;
import io.FaiscaJsr.DungeonsGame.entities.Player;
import io.FaiscaJsr.DungeonsGame.entities.Enemies.Enemy;

/**
 * Clase que representa una habitación.
 */
public class Room implements Disposable {

    private int ID;
    public boolean playerSpawn;
    public Vector2 playerCoordinatesSpawn;
    private int HEIGHT;
    private int WIDTH;
    private Vector2 initialPosition;
    public GridSpace[][] grid;
    public Rectangle hitbox;
    public Vector2 center;
    public ArrayList<Floor> floors;
    public ArrayList<Wall> walls;
    public ArrayList<Corner> corners;
    public Goal goal;
    public ReverseGoal reverseGoal;
    private static Random rnd = new Random();
    private Player player;
    public ArrayList<Enemy> enemies;
    public PlayScreen playScreen;
    public RoomManager roomManager;
    public boolean enemiesSpawned;

    /**
     * Método que devuelve la altura de la habitación.
     *
     * @return altura de la habitación.
     */
    public int getHeight() {
        return HEIGHT;
    }

    /**
     * Método que devuelve el ancho de la habitación.
     *
     * @return ancho de la habitación.
     */
    public int getWidth() {
        return WIDTH;
    }

    /**
     * Método que devuelve la posición inicial de la habitación.
     *
     * @return posición inicial de la habitación.
     */
    public Vector2 getInitialPosition() {
        return initialPosition;
    }

    /**
     * Enumeración que representa los espacios de la habitación.
     */
    public enum GridSpace {
        floor, wall, goal, reverseGoal
    }

    /**
     * Constructor de la habitación.
     * Crea las habitaciones y los tiles de la habitación.
     *
     * @param id         Identificador de la habitación.
     * @param position   Posición inicial de la habitación.
     * @param width      Ancho de la habitación.
     * @param height     Altura de la habitación.
     * @param player     Jugador.
     * @param playScreen Pantalla del juego.
     */
    public Room(int id, Vector2 position, int width, int height, Player player, PlayScreen playScreen) {
        this.ID = id;
        this.initialPosition = position;
        this.WIDTH = width;
        this.HEIGHT = height;
        this.player = player;
        this.playScreen = playScreen;
        hitbox = new Rectangle(initialPosition.x - 128, initialPosition.y + 128, WIDTH * 32 + 128, HEIGHT * 32 + 128);
        this.center = new Vector2(Math.round((initialPosition.x + ((width / 2) * Tile.DIM))),
                Math.round((initialPosition.y + (height * Tile.DIM / 2))));

        floors = new ArrayList<Floor>();
        walls = new ArrayList<Wall>();
        corners = new ArrayList<Corner>();
        enemies = new ArrayList<Enemy>();
        setup();
        load();
        enemiesSpawned = false;
        playerCoordinatesSpawn = new Vector2(
                rnd.nextFloat(this.center.x - this.WIDTH * 32 / 3, this.center.x + this.WIDTH * 32 / 3),
                rnd.nextFloat(this.center.y - this.HEIGHT * 32 / 3, this.center.y + this.HEIGHT * 32 / 3));
        roomManager = new RoomManager(this, playScreen);
    }

    /**
     * Método que define los tiles que van a ser dibujados en la habitación.
     */
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
                    if (j > 0 && i != 0) {
                        if (i + 1 < grid.length) {
                            if (grid[i + 1][j] == GridSpace.wall) {
                                if (count <= 0 || count < 1) {

                                    grid[i + 1][j] = GridSpace.goal;
                                    grid[i + 1][j - grid[i].length + 1] = GridSpace.reverseGoal;
                                    count++;

                                }
                            }
                        }
                    }

                }
            }

        }
    }

    /**
     * Método que carga los tiles de la habitación.
     * Se añaden a arrays de sprites para dibujarlos.
     */
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
                        goal = new Goal(this, initialPosition.x + (i * Tile.DIM / 2),
                                initialPosition.y + (j * Tile.DIM), 0, player);

                        break;
                    case reverseGoal:
                        Floor floor3 = new Floor(initialPosition.x + (i * Tile.DIM), initialPosition.y + (j * Tile.DIM),
                                0);
                        floors.add(floor3);
                        reverseGoal = new ReverseGoal(initialPosition.x + (i * Tile.DIM / 2),
                                initialPosition.y + (j * Tile.DIM), 180, player);
                        break;
                    default:
                        break;
                }

            }
        }

    }

    /**
     * Método que dibuja la habitación.
     * Se dibujan los tiles de la habitación.
     *
     * @param batch SpriteBatch que se utiliza para dibujar la habitación.
     */
    public void draw(SpriteBatch batch) {

        for (Wall wall : walls) {
            wall.getSprite().draw(batch);
        }
        for (Corner corner : corners) {
            corner.getSprite().draw(batch);
        }
        for (Floor floor : floors) {
            batch.draw(floor.getSprite(), floor.position.x, floor.position.y);
        }
        goal.getSprite().draw(batch);
        reverseGoal.getSprite().draw(batch);
        for (Enemy enemy : enemies) {
            enemy.draw(batch);
            enemy.update(Gdx.graphics.getDeltaTime());
        }

    }

    /**
     * Método que verifica si dos habitaciones se cruzan.
     *
     * @param room Habitación que se compara con la actual.
     * @return true si las habitaciones se cruzan, false en caso contrario.
     */
    public boolean intersects(Room room) {
        return (initialPosition.x <= room.initialPosition.x + room.WIDTH
                && initialPosition.x + this.WIDTH >= room.initialPosition.x
                && initialPosition.y <= room.initialPosition.y + room.HEIGHT
                && room.initialPosition.y + room.HEIGHT >= room.initialPosition.y);
    }

    /**
     * Método que verifica si dos habitaciones se cruzan.
     *
     * @param other Habitación que se compara con la actual.
     * @return true si las habitaciones se cruzan, false en caso contrario.
     */
    public boolean collidesWith(Room other) {
        return hitbox.overlaps(other.hitbox);
    }

    /**
     * Metodo que libera los recursos utlizados
     */
    @Override
    public void dispose() {
        for (Wall wall : walls) {
            wall.dispose();
        }
        for (Corner corner : corners) {
            corner.dispose();
        }
        for (Floor floor : floors) {
            floor.dispose();
        }
        goal.dispose();
        reverseGoal.dispose();
        for (Enemy enemy : enemies) {
            enemy.dispose();
        }
    }
}
