package io.FaiscaJsr.DungeonsGame.Managers;

import io.FaiscaJsr.DungeonsGame.Screens.PlayScreen;

public class TimeManager {
	public static float timeLeft = 5*60; // Tiempo en segundos

	private PlayScreen screen;

	public TimeManager(PlayScreen screen) {
		 // 5 minutos en segundos
		this.screen = screen;
	}

	public void update(float delta) {
		if (timeLeft > 0) {
			timeLeft -= delta;
			if (timeLeft < 0) {
				timeLeft = 0; // Evita valores negativos
				screen.GameOverScreen();
			}
		}
	}

	@Override
	public String toString() {
		int minutes = (int) (timeLeft / 60);
		int seconds = (int) (timeLeft % 60);
		return String.format("%02d:%02d", minutes, seconds);
	}
}
