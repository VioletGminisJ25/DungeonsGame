package io.FaiscaJsr.DungeonsGame.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import io.FaiscaJsr.DungeonsGame.entities.Goal;

public class WorldContactListener implements ContactListener{

	@Override
	public void beginContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		if(fixtureA.getUserData() == "player" || fixtureB.getUserData() == "player"){
            Fixture fixture = fixtureA.getUserData() == "player" ? fixtureA : fixtureB;
            Fixture otherFixture = fixture == fixtureA ? fixtureB : fixtureA;

            if(otherFixture.getUserData() != null && Goal.class.isAssignableFrom(otherFixture.getUserData().getClass())){
                ((Goal) otherFixture.getUserData()).reachGoal();
            }
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
