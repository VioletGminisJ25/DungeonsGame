package io.FaiscaJsr.DungeonsGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.FaiscaJsr.DungeonsGame.Managers.AssetsManager;
import io.FaiscaJsr.DungeonsGame.Managers.ManagerAudio;
import io.FaiscaJsr.DungeonsGame.Screens.MainMenuScreen;
import io.FaiscaJsr.DungeonsGame.Tools.GamePreferences;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends Game {
	public SpriteBatch batch;
	private Music currentMusic;
	private Music currentSound;

	@Override
	public void create() {
		ManagerAudio.load();
		AssetsManager.load();
		ManagerAudio.finishLoading();
		AssetsManager.finishLoading();
		batch = new SpriteBatch();
		setScreen(new MainMenuScreen(this));
	}

	public void playMusic(String musicPath, boolean loop) {
		Music newMusic = ManagerAudio.getMusic(musicPath);

		// Si es la misma música, solo actualizar volumen
		if (currentMusic == newMusic && currentMusic.isPlaying()) {
			updateMusicVolume();
			return;
		}

		// Detener la música anterior si hay alguna
		if (currentMusic != null) {
			currentMusic.stop();
		}

		// Configurar nueva música
		currentMusic = newMusic;
		currentMusic.setLooping(loop);
		updateMusicVolume(); // Aplica el volumen correcto

		if (GamePreferences.getMusicVolume() > 0) {
			currentMusic.play();
		}
	}
	public void playMusic(Music newMusic, boolean loop) {
		if (currentMusic != null) {
			currentMusic.stop();
		}

		// Configurar nueva música
		currentMusic = newMusic;
		currentMusic.setLooping(loop);
		updateSoundVolume();

		if (GamePreferences.getSoundVolume() > 0) {
			currentMusic.play();
		}
	}

	public void stopMusic() {
		if (currentMusic != null) {
			currentMusic.stop();
			currentMusic = null;
		}
	}

	public void updateMusicVolume() {
		if (currentMusic != null) {
			float volume = GamePreferences.getMusicVolume();
			currentMusic.setVolume(volume);

			// Si el volumen es 0, pausar la música; si es mayor, reanudarla
			if (volume == 0) {
				currentMusic.pause();
			} else if (!currentMusic.isPlaying()) {
				currentMusic.play();
			}
		}
	}
	 public void updateSoundVolume() {
		if (currentSound != null) {
			float volume = GamePreferences.getSoundVolume();
			currentSound.setVolume(volume);

			// Si el volumen es 0, pausar la música; si es mayor, reanudarla
			if (volume == 0) {
				currentSound.pause();
			} else if (!currentMusic.isPlaying()) {
				currentSound.play();
			}
		}
	}
	 public void playSound(Sound sound) {
		if (GamePreferences.getSoundVolume() > 0) {
			sound.play(GamePreferences.getSoundVolume());
		}
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		AssetsManager.dipose();
		ManagerAudio.dispose();
		batch.dispose();
	}

}
