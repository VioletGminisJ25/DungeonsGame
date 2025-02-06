package io.FaiscaJsr.DungeonsGame.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import io.FaiscaJsr.DungeonsGame.Entities.Enemy;
import io.FaiscaJsr.DungeonsGame.Entities.Goal;
import io.FaiscaJsr.DungeonsGame.Screens.PlayScreen;

public class WorldContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		int sbit = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;
		switch (sbit) {
			case PlayScreen.PLAYER_BIT_MASK | PlayScreen.GOAL_BIT_MASK:
				if (fixtureA.getFilterData().categoryBits == PlayScreen.PLAYER_BIT_MASK) {
					if(fixtureB.getUserData()!=null){
						((Goal) fixtureB.getUserData()).reachGoal();
					}
				} else {
					if(fixtureB.getUserData()!=null){
						((Goal) fixtureB.getUserData()).reachGoal();
					}
				}
				break;
			case PlayScreen.PLAYER_BIT_MASK | PlayScreen.ENEMY_BIT_MASK:

				if (fixtureA.getFilterData().categoryBits == PlayScreen.PLAYER_BIT_MASK) {
					if (fixtureB.getUserData() != null) {
						if (!Enemy.enemiesToHit.contains(((SlimeData) fixtureB.getUserData()).slime)) {
							Enemy.enemiesToHit.add(((SlimeData) fixtureB.getUserData()).slime);
						}
					}
				} else {
					if (fixtureA.getUserData() != null) {
						if (!Enemy.enemiesToHit.contains(((SlimeData) fixtureA.getUserData()).slime)) {
							Enemy.enemiesToHit.add(((SlimeData) fixtureA.getUserData()).slime);
						}
					}
				}
				break;

			default:
				break;
		}
	}

	@Override
	public void endContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		int sbit = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;
		switch (sbit) {
			case PlayScreen.PLAYER_BIT_MASK | PlayScreen.ENEMY_BIT_MASK:

				if (fixtureA.getFilterData().categoryBits == PlayScreen.PLAYER_BIT_MASK) {
					if (fixtureB.getUserData() != null) {
						if (fixtureB.getUserData() != null) {
							if (Enemy.enemiesToHit.contains(((SlimeData) fixtureB.getUserData()).slime)) {
								Enemy.enemiesToHit.remove(((SlimeData) fixtureB.getUserData()).slime);
							}
						}
					}
				} else {
					if (fixtureA.getUserData() != null) {
						if (Enemy.enemiesToHit.contains(((SlimeData) fixtureA.getUserData()).slime)) {
							Enemy.enemiesToHit.remove(((SlimeData) fixtureA.getUserData()).slime);
						}
					}

				}
				break;

			default:
				break;
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

}
