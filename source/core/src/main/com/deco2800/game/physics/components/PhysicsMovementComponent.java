package com.deco2800.game.physics.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.ai.movement.MovementController;
import com.deco2800.game.components.Component;
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

  @Override
  public void create() {
    physicsComponent = entity.getComponent(PhysicsComponent.class);
  }

  @Override
  public void update() {
    if (movementEnabled && targetPosition != null) {
      Body body = physicsComponent.getBody();
      updateDirection(body);
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

  /** @return Target position in the world */
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
  /*
    Function used to update the entities animations based upon the direction of movement.
    Character will display the animation that is within 45 degrees of the nearest compass direction.
     For example, if the entites vector is (-0.1,-0.9) than it will display a down walking animation.
   */
  public void movementEvents(){
    Vector2 entityDirection= getDirection();
    float x = entityDirection.x;
    float y = entityDirection.y;

    if (x<0 && y==0) {
      entity.getEvents().trigger("walkLeft");
      System.out.println(" Left");
    } else if (x>0 && y==0){
      entity.getEvents().trigger("walkRight");
      System.out.println("Right");
    } else if (x==0 && y>0){
      entity.getEvents().trigger("walkUp");
      System.out.println("Up");
    } else if (x == 0 && y<0){
      entity.getEvents().trigger("walkDown");
      System.out.println("Down");
    } else if (x<0 && y>0){
      entity.getEvents().trigger("walkUpLeft");
      System.out.println("Up Left");
    } else if (x>0 && y>0){
      entity.getEvents().trigger("walkUpRight");
      System.out.println("Up Right");
    } else if (x<0 && y<0){
      entity.getEvents().trigger("walkDownLeft");
      System.out.println("Down Left");
    } else if (x>0 && y<0){
      entity.getEvents().trigger("walkDownRight");
      System.out.println("Down Right");
    }
  }
}
