package io.FaiscaJsr.DungeonsGame.MapGenerator;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.FaiscaJsr.DungeonsGame.MapGenerator.Room.Room;
import io.FaiscaJsr.DungeonsGame.MapGenerator.TileMap.Floor;
import io.FaiscaJsr.DungeonsGame.MapGenerator.TileMap.Tile;
import io.FaiscaJsr.DungeonsGame.Screens.PlayScreen;
import io.FaiscaJsr.DungeonsGame.entities.Player;

/**
 * Clase que genera el mapa siguiendo el algoritmo Binary Space Partitioning
 */
public class BspTree {
    public Rectangle container;
    public Rectangle room;
    public BspTree left;
    public BspTree right;
    private static Random rnd = new Random();
    private ArrayList<Floor> floors;
    public static ArrayList<Room> rooms = new ArrayList<Room>();
    private final static int MIN_ROOM_SIZE = 7;
    private static final int MAX_HABITACIONES = 10;
    private Player player;
    private PlayScreen playScreen;
    private int numHabitaciones = 0;

    /**
     * Constructor de la clase
     *
     * @param a          Rectángulo que contiene el mapa
     * @param player     jugador
     * @param playScreen pantalla de juego
     */
    public BspTree(Rectangle a, Player player, PlayScreen playScreen) {
        container = a;
        floors = new ArrayList<Floor>();
        this.player = player;
        this.playScreen = playScreen;
    }

    /**
     * Método que divide el mapa en dos partes
     *
     * @param numberOfOperations número de operaciones
     * @param container          rectángulo que contiene el mapa
     * @return nodo que contiene el mapa dividido
     */
    public BspTree Split(int numberOfOperations, Rectangle container) {
        BspTree node = new BspTree(container, player, playScreen);

        if (numberOfOperations == 0) {
            return node;
        }

        Rectangle[] splitedContainer = SplitContainer(container);
        // Console.WriteLine(numberOfOperations + "");
        node.left = Split(numberOfOperations - 1, splitedContainer[0]);

        // Debug.Log(numberOfOperations);
        node.right = Split(numberOfOperations - 1, splitedContainer[1]);

        return node;
    }

    /**
     * Método que divide el rectángulo en dos partes
     *
     * @param container rectángulo que contiene el mapa
     * @return dos partes del rectángulo
     */
    private Rectangle[] SplitContainer(Rectangle container) {
        Rectangle c1, c2;
        boolean horizontal = rnd.nextBoolean();
        if (horizontal) {
            c1 = new Rectangle(container.x, container.y, container.width / 2,
                    container.height);
            c2 = new Rectangle(container.x + (container.width / 2) * Tile.DIM, container.y, container.width / 2,
                    container.height);
        } else {
            c1 = new Rectangle(container.x, container.y, container.width,
                    container.height / 2);
            c2 = new Rectangle(container.x, container.y + (container.height / 2) * Tile.DIM, container.width,
                    container.height / 2);
        }
        return new Rectangle[] { c1, c2 };
    }

    /**
     * Método que carga el mapa
     *
     * @param node nodo que contiene el mapa
     */
    public void load(BspTree node) {

        if (node.left != null) {
            load(node.left);
        }
        if (node.right != null) {
            load(node.right);

        } else {
            if (node.container.width > MIN_ROOM_SIZE && node.container.height > MIN_ROOM_SIZE) {

                Room room = new Room(numHabitaciones, new Vector2(node.container.x, node.container.y),
                        (int) node.container.width, (int) node.container.height, player, playScreen);

                if (numHabitaciones < MAX_HABITACIONES) {
                    rooms.add(room);
                    numHabitaciones++;
                }
            }
        }
    }

    /**
     * Método que dibuja el mapa
     *
     * @param batch sprite batch
     */
    public static void draw(SpriteBatch batch) {
        for (Room room : rooms) {
            room.draw(batch);
        }
    }

    /**
     * Método que libera memoria
     */
    public static void dispose() {
        for (Room room : rooms) {
            room.dispose();
        }
        rooms.clear();
    }
}
