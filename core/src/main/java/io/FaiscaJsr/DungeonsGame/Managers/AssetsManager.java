package io.FaiscaJsr.DungeonsGame.Managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Clase que maneja los recursos del juego.
 * Carga los recursos del juego y los almacena en un AssetManager.
 * Permite acceder a los recursos del juego mediante el método getTexture.
 */
public class AssetsManager {

    public static final AssetManager assetManager = new AssetManager();

    /**
     * Método que carga los recursos del juego.
     */
    public static void load() {
        assetManager.load("img/joystick_base.png", Texture.class);
        assetManager.load("img/joystick_knob.png", Texture.class);
        assetManager.load("tileset_complet.png", Texture.class);
        assetManager.load("ui/hearts.png", Texture.class);
        assetManager.load("ui/time.png", Texture.class);
        assetManager.load("ui/time_1.png", Texture.class);
        assetManager.load("slimes/atlas/Slimes.png", Texture.class);
        assetManager.load("Bosses/SlimeKing/SlimeKing_Walk_0.png",Texture.class);
    }

    /**
     * Método que finaliza la carga de los recursos del juego.
     */
    public static void finishLoading() {
        assetManager.finishLoading();
    }

    /**
     * Método que devuelve una textura del juego.
     *
     * @param path Ruta del recurso.
     * @return Recurso del juego.
     */
    public static Texture getTexture(String path) {
        return assetManager.get(path, Texture.class);
    }

    /**
     * Método que devuelve un fuente del juego.
     *
     * @param path Ruta del recurso.
     * @return Recurso del juego.
     */
    public static BitmapFont getFont(String path) {
        return assetManager.get(path, BitmapFont.class);
    }

    /**
     * Método que libera los recursos del juego.
     */
    public static void dipose() {
        assetManager.dispose();
    }
}
