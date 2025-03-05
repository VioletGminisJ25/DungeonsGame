package io.FaiscaJsr.DungeonsGame.Managers;

import io.FaiscaJsr.DungeonsGame.Screens.PlayScreen;

/**
 * Clase que maneja el tiempo.
 */
public class TimeManager {
    public static float timeLeft = 5 * 60; // Tiempo en segundos
    private PlayScreen screen;

    /**
     * Constructor de tiempo.
     *
     * @param screen Pantalla del juego.
     */
    public TimeManager(PlayScreen screen) {
        this.screen = screen;
    }

    /**
     * Método que actualiza el tiempo.
     * Si el tiempo se acaba, se finaliza el juego.
     *
     * @param delta Tiempo transcurrido desde la última actualización.
     */
    public void update(float delta) {
        if (timeLeft > 0) {
            timeLeft -= delta;
            if (timeLeft < 0) {
                timeLeft = 0;
                screen.GameOverScreen();
            }
        }
    }

    /**
     * Método que devuelve el tiempo restante.
     *
     * @return tiempo restante.
     */
    @Override
    public String toString() {
        int minutes = (int) (timeLeft / 60);
        int seconds = (int) (timeLeft % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }
}
