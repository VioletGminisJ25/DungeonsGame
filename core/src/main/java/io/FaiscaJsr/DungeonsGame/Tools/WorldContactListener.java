package io.FaiscaJsr.DungeonsGame.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import io.FaiscaJsr.DungeonsGame.Screens.PlayScreen;
import io.FaiscaJsr.DungeonsGame.entities.Enemy;
import io.FaiscaJsr.DungeonsGame.entities.Goal;

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
            case PlayScreen.ATTCK_BIT_MASK | PlayScreen.ENEMY_BIT_MASK: // Si el player choca con el enemigo deberia
                                                                         // recibir daño
                Enemy enemy = null;

                if (fixtureA.getFilterData().categoryBits == PlayScreen.ENEMY_BIT_MASK) {
                    enemy = (Enemy) fixtureA.getUserData();
                } else {
                    enemy = (Enemy) fixtureB.getUserData();
                }

                // Asegurarse de que enemy es un enemigo válido y que no está en la lista (cual)
                if (enemy != null && !Enemy.enemiesToHit.contains(enemy)) {
                   // System.out.println("Enemy added: " + enemy);
                    Enemy.enemiesToHit.add(enemy);

                    System.out.println(enemy.getCurrentHealth());
                    System.out.println(Enemy.enemiesToHit.size());
                }

                break;

                // Y aqui el caso del ataque no me seas

            default:
                // System.out.println("DEFUAULT");
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}
