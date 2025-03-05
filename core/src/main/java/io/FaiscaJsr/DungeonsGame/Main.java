package io.FaiscaJsr.DungeonsGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.FaiscaJsr.DungeonsGame.Managers.AssetsManager;
import io.FaiscaJsr.DungeonsGame.Managers.LanguageManager;
import io.FaiscaJsr.DungeonsGame.Managers.ManagerAudio;
import io.FaiscaJsr.DungeonsGame.Screens.MainMenuScreen;
import io.FaiscaJsr.DungeonsGame.Tools.GamePreferences;

/**
 * Clase principal del juego.
 */
public class Main extends Game {
    public SpriteBatch batch;
    public Music currentMusic;
    private Music currentSound;

    /**
     * Metodo que inicia el juego.
     *
     * @see Game#create()
     */
    @Override
    public void create() {
        ManagerAudio.load();
        AssetsManager.load();
        ManagerAudio.finishLoading();
        AssetsManager.finishLoading();
        LanguageManager.loadLanguage();
        batch = new SpriteBatch();
        setScreen(new MainMenuScreen(this));
    }

    /**
     * Método que inicia la música.
     *
     * @param musicPath Ruta del archivo de la música.
     * @param loop      Si se debe repetir la música o no.
     */
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

    /**
     * Método que inicia el sonido.
     *
     * @param newMusic Ruta del archivo de la música.
     * @param loop     Si se debe repetir la música o no.
     */
    public void playMusic(Music newMusic, boolean loop) {

        newMusic.setLooping(loop);

        if (GamePreferences.getSoundVolume() > 0) {
            newMusic.setVolume(GamePreferences.getSoundVolume());
            newMusic.play();
        }
    }

    /**
     * Método que para la musica actual.
     */
    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic = null;
        }
    }

    /**
     * Metodo que actualiza el volumen de la música.
     */
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

    /**
     * Metodo que actualiza el volumen del sonido.
     */
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

    /**
     * Metodo que inicia el sonido.
     *
     * @param sound el sonido a reproducir.
     */
    public void playSound(Sound sound) {
        if (GamePreferences.getSoundVolume() > 0) {
            sound.play(GamePreferences.getSoundVolume());
        }
    }

    /**
     * Método que renderiza el juego.
     *
     * @see Game#render()
     */
    @Override
    public void render() {
        super.render();
    }

    /**
     * Metodo que libera los recursos
     *
     * @see Game#dispose()
     */
    @Override
    public void dispose() {
        AssetsManager.dipose();
        ManagerAudio.dispose();
        batch.dispose();
    }

}
