package com.deco2800.game.physics.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.ai.movement.MovementController;
import com.deco2800.game.generic.Component;
import com.deco2800.game.utils.math.Vector2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Movement controller for a physics-based entity. */
public class PhysicsMovementComponent extends Component implements MovementController {
  private static final Logger logger = LoggerFactory.getLogger(PhysicsMovementComponent.class);
  private static final Vector2 maxSpeed = Vector2Utils.ONE;

  private PhysicsComponent physicsComponent;
  private Vector2 targetPosition;
  private boolean movementEnabled = true;
  private int lastDirection = 0;
  private int currentDirection = 0;

  @Override
  public void create() {
    physicsComponent = entity.getComponent(PhysicsComponent.class);
  }

  @Override
  public void update() {
    if (movementEnabled && targetPosition != null) {
      Body body = physicsComponent.getBody();
      updateDirection(body);
      getcurrentDirectionCode();
      movementEvents();
    }
  }

  /**
   * Enable/disable movement for the controller. Disabling will immediately set velocity to 0.
   *
   * @param movementEnabled true to enable movement, false otherwise
   */
  @Override
  public void setMoving(boolean movementEnabled) {
    this.movementEnabled = movementEnabled;
    if (!movementEnabled) {
      Body body = physicsComponent.getBody();
      setToVelocity(body, Vector2.Zero);
    }
  }

  @Override
  public boolean getMoving() {
    return movementEnabled;
  }

  /**
   * @return Target position in the world
   */
  @Override
  public Vector2 getTarget() {
    return targetPosition;
  }

  /**
   * Set a target to move towards. The entity will be steered towards it in a straight line, not
   * using pathfinding or avoiding other entities.
   *
   * @param target target position
   */
  @Override
  public void setTarget(Vector2 target) {
    logger.trace("Setting target to {}", target);
    this.targetPosition = target;
  }

  private void updateDirection(Body body) {
    Vector2 desiredVelocity = getDirection().scl(maxSpeed);
    setToVelocity(body, desiredVelocity);
  }

  private void setToVelocity(Body body, Vector2 desiredVelocity) {
    // impulse force = (desired velocity - current velocity) * mass
    Vector2 velocity = body.getLinearVelocity();
    Vector2 impulse = desiredVelocity.cpy().sub(velocity).scl(body.getMass());
    body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
  }

  private Vector2 getDirection() {
    // Move towards targetPosition based on our current position
    return targetPosition.cpy().sub(entity.getPosition()).nor();
  }

  /**
   * Will asign an integer value to the direction. Directions are broken into compass quadrents.
   * Where:
   * 0 = North
   * 1 = East
   * 2 = South
   * 3 = West
   * 4 = NorthEast
   * 5 = NorthWest
   * 6 = SouthEast
   * 7 = SouthWest
   */
  public void getcurrentDirectionCode() {
    Vector2 entityDirection = getDirection();
    float x = entityDirection.x;
    float y = entityDirection.y;

    if (x < 0.5 && x > -0.5 && y > 0) {         // Walking north
      currentDirection = 0;
    } else if (x > 0 && y < 0.5 && y > -0.5) {  // Walking east
      currentDirection = 1;
    } else if (x < 0.5 && x > -0.5 && y < 0) {  // Walking south
      currentDirection = 2;
    } else if (x < 0 && y < 0.5 && y > -0.5) {  // Walking west
      currentDirection = 3;
    } else if (x > 0.5 && y >0.5) {
      currentDirection = 4;
    } else if (x < -0.5 && y >0.5) {
      currentDirection = 5;
    } else if (x > 0.5 && y < -0.5) {
      currentDirection = 6;
    } else if (x < -0.5 && y < -0.5) {
      currentDirection = 7;
    }

  }

  /**
   * Function used to update the entities animations based upon the direction of movement.
   * Character will display the animation that tis within 45 degrees of the nearest compass direction.
   * For example, if the entites vector is (-0.1,-0.9) than it will display a down walking animation.
   */
  public void movementEvents() {
    Vector2 entityDirection = getDirection();
    float x = entityDirection.x;
    float y = entityDirection.y;

    if (lastDirection != currentDirection) {
      if (x < 0.5 && x > -0.5 && y > 0) {
        entity.getEvents().trigger("update_animation", "walking_north");
        lastDirection = 0;
      } else if (x > 0 && y < 0.5 && y > -0.5) {
        entity.getEvents().trigger("update_animation", "walking_east");
        lastDirection = 1;
      } else if (x < 0.5 && x > -0.5 && y < 0) {
        entity.getEvents().trigger("update_animation", "walking_south");
        lastDirection = 2;
      } else if (x < 0 && y < 0.5 && y > -0.5) {
        entity.getEvents().trigger("update_animation", "walking_west");
        lastDirection = 3;
      } else if (x > 0.5 && y >0.5) {
        entity.getEvents().trigger("update_animation", "walking_northeast");
        lastDirection = 4;
      } else if (x < -0.5 && y >0.5) {
        entity.getEvents().trigger("update_animation", "walking_northwest");
        lastDirection = 5;
      } else if (x > 0.5 && y < -0.5) {
        entity.getEvents().trigger("update_animation", "walking_southeast");
        lastDirection = 6;
      } else if (x < -0.5 && y < -0.5) {
        entity.getEvents().trigger("update_animation", "walking_southwest");
        lastDirection = 7;
      }
    }
  }
}
