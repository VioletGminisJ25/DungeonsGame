package io.FaiscaJsr.DungeonsGame.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import io.FaiscaJsr.DungeonsGame.MapGenerator.TileMap.Goal;
import io.FaiscaJsr.DungeonsGame.MapGenerator.TileMap.ReverseGoal;
import io.FaiscaJsr.DungeonsGame.Screens.PlayScreen;
import io.FaiscaJsr.DungeonsGame.entities.Player;
import io.FaiscaJsr.DungeonsGame.entities.Enemies.Enemy;
import io.FaiscaJsr.DungeonsGame.entities.Items.Heart;
import io.FaiscaJsr.DungeonsGame.entities.Items.Time;

/**
 * Clase World Contac Listener
 * Se encarga de estar a la escucha de todos los eventos
 * de contacto entre dos bodies del world
 */
public class WorldContactListener implements ContactListener {

    /**
     * Constructor
     * Metodo que recibe el contacto inicial
     *
     * @param contact Contacto
     * @see WorldContactListener#beginContact(Contact) beginContact
     */
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        int sbit = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        switch (sbit) {
            case PlayScreen.PLAYER_BIT_MASK | PlayScreen.GOAL_BIT_MASK:
                if (fixtureB.getUserData() != null) {
                    if (fixtureA.getFilterData().categoryBits == PlayScreen.PLAYER_BIT_MASK) {
                        ((Goal) fixtureB.getUserData()).reachGoal();
                    } else {
                        ((Goal) fixtureA.getUserData()).reachGoal();
                    }
                }
                break;
            case PlayScreen.ATTACK_BIT_MASK | PlayScreen.ENEMY_BIT_MASK:
                Enemy enemy = null;

                if (fixtureA.getFilterData().categoryBits == PlayScreen.ENEMY_BIT_MASK) {
                    enemy = (Enemy) fixtureA.getUserData();
                } else {
                    enemy = (Enemy) fixtureB.getUserData();
                }

                if (enemy != null && !Enemy.enemiesToHit.contains(enemy)) {
                    Enemy.enemiesToHit.add(enemy);

                }

                break;
            case PlayScreen.PLAYER_BIT_MASK | PlayScreen.REVERSE_GOAL_BIT_MASK:
                if (fixtureB.getUserData() != null) {
                    if (fixtureA.getFilterData().categoryBits == PlayScreen.PLAYER_BIT_MASK) {
                        ((ReverseGoal) fixtureB.getUserData()).reachGoal();
                    } else {
                        ((ReverseGoal) fixtureA.getUserData()).reachGoal();
                    }
                }
                break;

            case PlayScreen.PLAYER_BIT_MASK | PlayScreen.ENEMY_BIT_MASK:
                if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
                    if (fixtureB.getFilterData().categoryBits == PlayScreen.ENEMY_BIT_MASK) {
                        ((Player) fixtureA.getUserData()).hit(5);
                        ((Enemy) fixtureB.getUserData()).ishitPlayer = true;
                    } else {
                        ((Player) fixtureB.getUserData()).hit(5);
                        ((Enemy) fixtureA.getUserData()).ishitPlayer = true;

                    }
                }
                break;
            case PlayScreen.PLAYER_BIT_MASK | PlayScreen.ITEM_BIT_MASK:
                if (fixtureB.getUserData() != null) {
                    if (fixtureA.getFilterData().categoryBits == PlayScreen.PLAYER_BIT_MASK) {
                        if (fixtureB.getUserData().getClass() == Heart.class) {
                            ((Player) fixtureA.getUserData()).pickupHeart();
                            ((Heart) fixtureB.getUserData()).destroy();
                        } else if (fixtureB.getUserData().getClass() == Time.class) {
                            ((Player) fixtureA.getUserData()).pickupTime();
                            ((Time) fixtureB.getUserData()).destroy();
                        }
                    } else {
                        if (fixtureA.getUserData().getClass() == Heart.class) {

                            ((Player) fixtureB.getUserData()).pickupHeart();
                            ((Heart) fixtureA.getUserData()).destroy();
                        } else if (fixtureB.getUserData().getClass() == Time.class) {
                            ((Player) fixtureA.getUserData()).pickupTime();
                            ((Time) fixtureB.getUserData()).destroy();
                        }
                    }
                }

            default:
                break;
        }
    }

    /**
     * Metodo que recibe el contacto final
     *
     * @param contact Contacto
     * @see WorldContactListener#endContact(Contact) endContact
     */
    @Override
    public void endContact(Contact contact) {
    }

    /**
     * Metodo que recibe el contacto antes de resolver
     *
     * @param contact     Contacto
     * @param oldManifold Manifold anterior
     * @see WorldContactListener#preSolve(Contact, Manifold) preSolve
     */
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    /**
     * Metodo que recibe el contacto despues de resolver
     *
     * @param contact Contacto
     * @param impulse Impulso
     */
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}
