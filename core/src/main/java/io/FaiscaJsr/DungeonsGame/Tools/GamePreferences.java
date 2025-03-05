package io.FaiscaJsr.DungeonsGame.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Clase que maneja las preferencias del juego, como volumen, idioma, vibración
 * y puntuaciones altas.
 */
public class GamePreferences {
    private static final String PREFS_NAME = "dungeonGamePrefs"; // Nombre del archivo de preferencias
    private static final String MUSIC_VOLUME = "musicVolume";
    private static final String SOUND_VOLUME = "soundVolume";
    private static final String VIBRATION = "vibration";
    private static final String LANGUAGE = "language";
    public static final String RUNS = "runs";


    private static Preferences prefs;

    /**
     * Obtiene las preferencias del juego, asegurándose de que estén cargadas.
     *
     * @return Objeto de preferencias.
     */
    public static Preferences getPreferences() {
        if (prefs == null) {
            prefs = Gdx.app.getPreferences(PREFS_NAME);
        }
        return prefs;
    }

    /**
     * Establece el volumen de la música.
     *
     * @param volume Valor del volumen entre 0.0 y 1.0.
     */
    public static void setMusicVolume(float volume) {
        getPreferences().putFloat(MUSIC_VOLUME, volume).flush();
    }

    /**
     * Obtiene el volumen de la música.
     *
     * @return Valor del volumen (por defecto 1.0 si no ha sido configurado).
     */
    public static float getMusicVolume() {
        return getPreferences().getFloat(MUSIC_VOLUME, 1.0f);
    }

    /**
     * Establece el volumen de los efectos de sonido.
     *
     * @param volume Valor del volumen entre 0.0 y 1.0.
     */
    public static void setSoundVolume(float volume) {
        getPreferences().putFloat(SOUND_VOLUME, volume).flush();
    }

    /**
     * Obtiene el volumen de los efectos de sonido.
     *
     * @return Valor del volumen (por defecto 1.0 si no ha sido configurado).
     */
    public static float getSoundVolume() {
        return getPreferences().getFloat(SOUND_VOLUME, 1.0f);
    }

    /**
     * Activa o desactiva la vibración en el juego.
     *
     * @param enabled {@code true} para activar la vibración, {@code false} para
     *                desactivarla.
     */
    public static void setVibration(boolean enabled) {
        getPreferences().putBoolean(VIBRATION, enabled).flush();
    }

    /**
     * Verifica si la vibración está activada.
     *
     * @return {@code true} si la vibración está activada, {@code false} si está
     *         desactivada.
     */
    public static boolean isVibrationEnabled() {
        return getPreferences().getBoolean(VIBRATION, true);
    }

    /**
     * Establece el idioma del juego.
     *
     * @param language Código de idioma (ejemplo: "es" para español, "en" para
     *                 inglés).
     */
    public static void setLanguage(String language) {
        getPreferences().putString(LANGUAGE, language).flush();
    }

    /**
     * Obtiene el idioma configurado en el juego.
     *
     * @return Código de idioma actual (por defecto "es").
     */
    public static String getLanguage() {
        return getPreferences().getString(LANGUAGE, "es");
    }

    public static void guardarRun(int vecesReloj, String tiempoJugado) {

        // Obtener los registros anteriores
        String registrosPrevios = getPreferences().getString(RUNS, "");

        // Crear nueva entrada en formato "vecesReloj,tiempoJugado"
        String nuevaRun = vecesReloj + "," + tiempoJugado;

        // Concatenar con los registros anteriores si existen
        if (!registrosPrevios.isEmpty()) {
            registrosPrevios += ";" + nuevaRun;
        } else {
            registrosPrevios = nuevaRun;
        }

        // Guardar en Preferences
        getPreferences().putString(RUNS, registrosPrevios);
        getPreferences().flush();
    }

}
