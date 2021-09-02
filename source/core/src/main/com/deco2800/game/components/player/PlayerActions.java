package com.deco2800.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.components.Component;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Action component for interacting with the player. Player events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class PlayerActions extends Component {
  private static final Vector2 MAX_SPEED = new Vector2(3f, 3f); // Metres per second

  private PhysicsComponent physicsComponent;
  private CombatStatsComponent combatStatsComponent;

  private Vector2 walkDirection = Vector2.Zero.cpy();
  private boolean moving = false;
  private boolean running = false;

  @Override
  public void create() {
    physicsComponent = entity.getComponent(PhysicsComponent.class);
    combatStatsComponent = entity.getComponent(CombatStatsComponent.class);
    entity.getEvents().addListener("walk", this::walk);
    entity.getEvents().addListener("walkStop", this::stopWalking);
    entity.getEvents().addListener("attack", this::attack);
    entity.getEvents().addListener("run", this::run);
    entity.getEvents().addListener("stopRun", this::stopRunning);
  }

  @Override
  public void update() {
    if (moving) {
      updateSpeed();
    }
    // update the stamina value of player
    updateStamina();

  }

  private void updateSpeed() {
    // increase speed when running, only when there is stamina left
    if (running && combatStatsComponent.getStamina() > 0) {
      MAX_SPEED.set(10f, 10f); //TODO adjust running speed
    } else {
      MAX_SPEED.set(3f, 3f);
    }
    Body body = physicsComponent.getBody();
    Vector2 velocity = body.getLinearVelocity();
    Vector2 desiredVelocity = walkDirection.cpy().scl(MAX_SPEED);
    // impulse = (desiredVel - currentVel) * mass
    Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
    body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
  }

  private void updateStamina() {
    // when player is moving and is running, decrease stamina
    if (running && moving) {
      entity.getEvents().trigger("changeStamina", -1);
    } else { // player is not running (released SHIFT or not moving), regenerate stamina
      entity.getEvents().trigger("changeStamina", 1);
    }
  }

  /**
   * Moves the player towards a given direction.
   *
   * @param direction direction to move in
   */
  void walk(Vector2 direction) {
    this.walkDirection = direction;
    moving = true;
  }

  /**
   * Stops the player from walking.
   */
  void stopWalking() {
    this.walkDirection = Vector2.Zero.cpy();
    updateSpeed();
    moving = false;
  }


  /**
   * Makes the player attack.
   */
  void attack() {
    Sound attackSound = ServiceLocator.getResourceService().getAsset("sounds/Impact4.ogg", Sound.class);
    attackSound.play();
  }

  /**
   * Indicates player is running
   */
  void run() {
    running = true;
  }

  /**
   * Indicates player is not running
   */
  void stopRunning() {
    running = false;
  }
}
