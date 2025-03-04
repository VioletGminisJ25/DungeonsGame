package io.FaiscaJsr.DungeonsGame.Managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Administra la carga y reproducción de archivos de audio en el juego.
 * Permite cargar música y efectos de sonido de manera asíncrona,
 * evitando bloqueos en el rendimiento.
 */
public class ManagerAudio {
	/** Gestor de activos para manejar la carga de archivos de audio. */
	private static final AssetManager manager = new AssetManager();

	/**
	 * Carga los archivos de audio necesarios para el juego.
	 * Se verifica si los archivos ya están cargados antes de añadirlos a la cola.
	 */
	public static void load() {
		loadMusic("fsx/slimeKing/walk_slime.wav");
		loadSound("fsx/slimeKing/SlimeKing_Death.wav");
		loadSound("fsx/slimeKing/SlimeKing_Hit.wav");
		loadSound("fsx/slimes/death_slime_1.wav");
		loadSound("fsx/slimes/slime_jump_1.wav");
		loadSound("fsx/slimes/slime_swallow_1.wav");
		loadMusic("MainMenu/sound/menuMusic.wav");
	}

	/**
	 * Finaliza la carga de todos los archivos de audio.
	 * Este método bloquea la ejecución hasta que todos los recursos estén
	 * disponibles.
	 */
	public static void finishLoading() {
		manager.finishLoading();
	}

	/**
	 * Obtiene un archivo de música cargado.
	 *
	 * @param fileName Ruta del archivo de música.
	 * @return Objeto {@link Music} si está disponible, o `null` si no se encuentra.
	 */
	public static Music getMusic(String fileName) {
		if (manager.isLoaded(fileName)) {
			return manager.get(fileName, Music.class);
		} else {
			System.err.println("Música no encontrada: " + fileName);
			return null;
		}
	}

	/**
	 * Obtiene un efecto de sonido cargado.
	 *
	 * @param fileName Ruta del archivo de sonido.
	 * @return Objeto {@link Sound} si está disponible, o `null` si no se encuentra.
	 */
	public static Sound getSound(String fileName) {
		if (manager.isLoaded(fileName)) {
			return manager.get(fileName, Sound.class);
		} else {
			System.err.println("Sonido no encontrado: " + fileName);
			return null;
		}
	}

	/**
	 * Libera un archivo de audio específico para optimizar el uso de memoria.
	 * 
	 * @param fileName Ruta del archivo a liberar.
	 */
	public static void unload(String fileName) {
		if (manager.isLoaded(fileName)) {
			manager.unload(fileName);
		}
	}

	/**
	 * Libera todos los archivos de audio cargados.
	 * Se debe llamar cuando ya no se necesiten los sonidos en memoria.
	 */
	public static void dispose() {
		manager.dispose();
	}

	// ---------------------- Métodos privados ----------------------

	/**
	 * Carga un archivo de música si no ha sido cargado previamente.
	 * 
	 * @param fileName Ruta del archivo de música.
	 */
	private static void loadMusic(String fileName) {
		if (!manager.isLoaded(fileName)) {
			manager.load(fileName, Music.class);
		}
	}

	/**
	 * Carga un archivo de sonido si no ha sido cargado previamente.
	 * 
	 * @param fileName Ruta del archivo de sonido.
	 */
	private static void loadSound(String fileName) {
		if (!manager.isLoaded(fileName)) {
			manager.load(fileName, Sound.class);
		}
	}
}