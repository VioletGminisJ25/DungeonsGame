package io.FaiscaJsr.DungeonsGame.Tools;

import java.nio.channels.Pipe.SourceChannel;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import io.FaiscaJsr.DungeonsGame.Entities.Enemy;
import io.FaiscaJsr.DungeonsGame.Entities.Goal;
import io.FaiscaJsr.DungeonsGame.Entities.Player;
import io.FaiscaJsr.DungeonsGame.Screens.PlayScreen;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        int sbit = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;
        // System.out.println("A:"+fixtureA.getFilterData().categoryBits);
        // System.out.println("B:"+fixtureB.getFilterData().categoryBits);
        switch (sbit) {
            case PlayScreen.PLAYER_BIT_MASK | PlayScreen.GOAL_BIT_MASK:
                if (fixtureA.getFilterData().categoryBits == PlayScreen.PLAYER_BIT_MASK) {
                    if (fixtureB.getUserData() != null) {
                        ((Goal) fixtureB.getUserData()).reachGoal();
                    }
                }
                break;
            case PlayScreen.PLAYER_BIT_MASK | PlayScreen.ENEMY_BIT_MASK:
                Enemy enemy = null;

                // if (fixtureA.getFilterData().categoryBits == PlayScreen.ENEMY_BIT_MASK
                //         && fixtureA.getUserData() instanceof Enemy) {
                //     enemy = (Enemy) fixtureA.getUserData();
                // } else
                if (fixtureA.getFilterData().categoryBits == PlayScreen.ENEMY_BIT_MASK
                        && fixtureA.getUserData() instanceof Enemy) {
                    enemy = (Enemy) fixtureA.getUserData();
                }

                // Asegurarse de que enemy es un enemigo válido y que no está en la lista
                if (enemy != null && !Enemy.enemiesToHit.contains(enemy)) {
                    Enemy.enemiesToHit.add(enemy);
                    // System.out.println("Enemy added: " + enemy);
                }

                break;

            default:
                // System.out.println("DEFUAULT");
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

                Enemy enemy = null;

                if (fixtureA.getFilterData().categoryBits == PlayScreen.ENEMY_BIT_MASK
                        && fixtureA.getUserData() instanceof Enemy) {
                    enemy = (Enemy) fixtureA.getUserData();
                } else if (fixtureB.getFilterData().categoryBits == PlayScreen.ENEMY_BIT_MASK
                        && fixtureB.getUserData() instanceof Enemy) {
                    enemy = (Enemy) fixtureB.getUserData();
                }

                if (enemy != null) {
                    Enemy.enemiesToHit.remove(enemy);
                    // System.out.println("Enemy removed: " + enemy);
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
