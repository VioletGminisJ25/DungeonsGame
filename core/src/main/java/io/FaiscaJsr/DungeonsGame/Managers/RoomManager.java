package io.FaiscaJsr.DungeonsGame.Managers;

import java.util.ArrayList;
import java.util.Random;

import io.FaiscaJsr.DungeonsGame.MapGenerator.Room.Room;
import io.FaiscaJsr.DungeonsGame.Screens.PlayScreen;
import io.FaiscaJsr.DungeonsGame.entities.Player;
import io.FaiscaJsr.DungeonsGame.entities.Enemies.Enemy;
import io.FaiscaJsr.DungeonsGame.entities.Enemies.Slimes;
import io.FaiscaJsr.DungeonsGame.entities.Enemies.Bosses.SlimeKing;

public class RoomManager {
	private int rounds;
	private int enemyCantSpawn;
	private static Random rnd = new Random();
	private PlayScreen playScreen;
	private Room room;
	private Player player;
	private boolean finished;
	private boolean roomInitialized;
	private ArrayList<Enemy> enemiesAlive;

	public RoomManager(Room room, PlayScreen playScreen) {
		super();
		this.playScreen = playScreen;
		this.room = room;
		this.rounds = 1;
		this.enemyCantSpawn = 0;
		this.roomInitialized = false;
		this.enemiesAlive = new ArrayList<Enemy>();
	}

	public void roomInitialized() {
		createEnemies();
		roomInitialized = true;
	}

	public void createEnemies() {
		enemyCantSpawn = rnd.nextInt(1, 15);

		for (int i = 0; i < enemyCantSpawn; i++) {
			Slimes slime = new Slimes(player, playScreen, PlayScreen.world,
					rnd.nextFloat(room.center.x - room.getWidth() * 32 / 3,
							room.center.x + room.getWidth() * 32
									/ 3),
					rnd.nextFloat(room.center.y - room.getHeight() * 32 / 3,
							room.center.y + room.getHeight() * 32 / 3),
					35, 1f, 1f, rnd.nextInt(7));
			room.enemies.add(slime);
			enemiesAlive.add(slime);
		}
	}

	private void enemiesDefeated() {
		int cont = 0;
		for (Enemy enemy : enemiesAlive) {
			if (enemy.isDestroyed()) {
				cont++;
			}
		}
		if (roomInitialized) {
			if (cont == enemiesAlive.size()) {
				enemiesAlive.clear();
				if (rounds >= 3) {
					finished = true;
				} else if (rounds == 2) {
					SlimeKing slimeKing = new SlimeKing(player, playScreen, PlayScreen.world,
							rnd.nextFloat(room.center.x - room.getWidth() * 32 / 3,
									room.center.x + room.getWidth() * 32 / 3),
							rnd.nextFloat(room.center.y - room.getHeight() * 32 / 3,
									room.center.y + room.getHeight() * 32 / 3),
							50, 1f, 1f);
					room.enemies.add(slimeKing);
					enemiesAlive.add(slimeKing);
					createEnemies();
					rounds++;
				} else {
					createEnemies();
					rounds++;
				}
				System.out.println("Round: " + rounds);
			}
		}
	}

	public boolean finishRoom() {
		return finished;
	}

	public void update(float delta) {
		enemiesDefeated();
	}
}
