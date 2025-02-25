package io.FaiscaJsr.DungeonsGame.Managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class AssetsManager {
    public static final AssetManager assetManager = new AssetManager();


    public static void load(){
        assetManager.load("img/joystick_base.png",Texture.class);
        assetManager.load("img/joystick_knob.png",Texture.class);
        assetManager.load("tileset_complet.png",Texture.class);
        assetManager.load("MainMenu/sound/menuMusic.mp3",Music.class);
    }

    public static void finishLoading(){
        assetManager.finishLoading();
    }

    public static Texture getTexture(String path){
        System.out.println("Cargando: "+ assetManager.get(path, Texture.class).getClass());
        return assetManager.get(path,Texture.class);
    }

    public static BitmapFont getFont(String path){
        return assetManager.get(path,BitmapFont.class);
    }

    public static Music getMusic(String fileName) {
        if (assetManager.isLoaded(fileName)) {
            return assetManager.get(fileName, Music.class);
        } else {
            System.err.println("Música no encontrada: " + fileName);
            return null;
        }
    }
    public static void dipose(){
        assetManager.dispose();
    }
}
