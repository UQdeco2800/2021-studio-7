package com.deco2800.game.physics;

import com.badlogic.gdx.physics.box2d.*;
import com.deco2800.game.components.player.PlayerObjectInteractions;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.ColliderComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Box2D collision events fire globally on the physics world, not per-object. The contact listener
 * receives these events, finds the entities involved in the collision, and triggers events on them.
 *
 * <p>On contact start: evt = "collisionStart", params = ({@link Fixture} thisFixture, {@link
 * Fixture} otherFixture)
 *
 * <p>On contact end: evt = "collisionEnd", params = ({@link Fixture} thisFixture, {@link Fixture}
 * otherFixture)
 */
public class PhysicsContactListener implements ContactListener {
  private static final Logger logger = LoggerFactory.getLogger(PhysicsContactListener.class);
  private Fixture targetFixture;
  private Entity targetEntity;

  public void setTargetFixture(ColliderComponent component){
    this.targetFixture = component.getFixture();
    setTargetEntity(this.targetFixture);
  }

  private void setTargetEntity(Fixture fixture){
    BodyUserData userData = (BodyUserData) fixture.getBody().getUserData();
    if (userData != null && userData.entity != null){
      this.targetEntity = userData.entity;
    }
  }

  private void targetCollisionCommunication(Fixture fixture, boolean touching){
    BodyUserData userData =
            (BodyUserData) fixture.getBody().getUserData();
    Entity objectTouching = userData.entity;
    if (touching){
      targetEntity.getComponent(PlayerObjectInteractions.class).addObject(objectTouching);
    } else {
      targetEntity.getComponent(PlayerObjectInteractions.class).removeObject(objectTouching);
    }
  }

  @Override
  public void beginContact(Contact contact) {
    triggerEventOn(contact.getFixtureA(), "collisionStart", contact.getFixtureB());
    triggerEventOn(contact.getFixtureB(), "collisionStart", contact.getFixtureA());

    if (contact.getFixtureA() == targetFixture){
      targetCollisionCommunication(contact.getFixtureB(), true);
    } else if (contact.getFixtureB() == targetFixture){
      targetCollisionCommunication(contact.getFixtureA(), true);
    }
  }

  @Override
  public void endContact(Contact contact) {
    triggerEventOn(contact.getFixtureA(), "collisionEnd", contact.getFixtureB());
    triggerEventOn(contact.getFixtureB(), "collisionEnd", contact.getFixtureA());

    if (contact.getFixtureA() == targetFixture){
      targetCollisionCommunication(contact.getFixtureB(), false);
    } else if (contact.getFixtureB() == targetFixture){
      targetCollisionCommunication(contact.getFixtureA(), false);
    }
  }

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {
  }

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) {
  }

  private void triggerEventOn(Fixture fixture, String evt, Fixture otherFixture) {
    BodyUserData userData = (BodyUserData) fixture.getBody().getUserData();
    if (userData != null && userData.entity != null) {
      logger.debug("{} on entity {}", evt, userData.entity);
      userData.entity.getEvents().trigger(evt, fixture, otherFixture);
    }
  }
}
