package io.FaiscaJsr.DungeonsGame.Tools;

import io.FaiscaJsr.DungeonsGame.entities.Player;

public class PlayerData {
    private  String mode;
    private int damage;
    private Player player;
    private boolean isAttack;
	public boolean isAttack() {
        return isAttack;
    }
    public void setAttack(boolean isAttack) {
        this.isAttack = isAttack;
    }
    public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public int getDamage() {
		return damage;
	}
	public void setDamage(int damage) {
		this.damage = damage;
	}
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
    public PlayerData(String mode, int damage, Player player, boolean isAttack) {
        this.mode = mode;
        this.damage = damage;
        this.player = player;
        this.isAttack = isAttack;
    }

}
