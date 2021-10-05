package com.deco2800.game.entities.components.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.CombatStatsComponent;
import com.deco2800.game.entities.components.interactions.InteractionComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.generic.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Action component for interacting with the player. Player events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class PlayerActions extends InteractionComponent {
  private static final Vector2 MAX_SPEED = new Vector2(3f, 3f); // Metres per second
  private static final Logger logger = LoggerFactory.getLogger(PlayerActions.class);

  private final Array<Entity> collidingInteractables = new Array<>();
  private Entity highlightedInteractable = null;
  private long timeSinceLastHighlight = 0L;
  private PhysicsComponent physicsComponent;
  private CombatStatsComponent combatStatsComponent;

  private Vector2 walkDirection = Vector2.Zero.cpy();
  private boolean moving = false;
  private boolean running = false;

  @Override
  public void create() {
    super.create();
    physicsComponent = entity.getComponent(PhysicsComponent.class);
    combatStatsComponent = entity.getComponent(CombatStatsComponent.class);
    entity.getEvents().addListener("walk", this::walk);
    entity.getEvents().addListener("stop_walking", this::stopWalking);
    entity.getEvents().addListener("run", this::run);
    entity.getEvents().addListener("stop_running", this::stopRunning);
    entity.getEvents().trigger("update_animation", "standing_south");
    entity.getEvents().addListener("drink_energy_drink", this::drinkEnergyDrink);
  }

  @Override
  public void onCollisionStart(Entity target) {
    if (target.getComponent(InteractionComponent.class) != null &&
            !collidingInteractables.contains(target, true)) {
      logger.info("Added interactable to list");
      collidingInteractables.add(target);
    }
  }

  @Override
  public void onCollisionEnd(Entity target) {
    boolean removed = collidingInteractables.removeValue(target, true);
    if (removed) {
      logger.info("Removed interactable to list");
    }
  }

  @Override
  public void update() {
    // Check if 100 milliseconds have passed since last checking highlights
    long currentTime = ServiceLocator.getTimeSource().getTime();
    if (currentTime - timeSinceLastHighlight >= 100L) {
      timeSinceLastHighlight = currentTime;
      updateEntityHighlights();
    }

    if (moving) {
      updateSpeed();
      entity.getEvents().trigger("change_score", -1);
    }
    // update the stamina value of player
    updateStamina();

  }

  private void drinkEnergyDrink(){
    int stamina = entity.getComponent(CombatStatsComponent.class).getStamina();
    entity.getComponent(CombatStatsComponent.class).setStamina(stamina + 100);
  }

  private void updateEntityHighlights() {
    Vector2 playerPos = entity.getPosition();
    Entity closestInteractable = null;

    for (Entity interactable : new Array.ArrayIterator<>(collidingInteractables)) {
      if (closestInteractable == null ||
              entity.getPosition().dst(playerPos) < closestInteractable.getPosition().dst(playerPos)) {
        // Closest hasn't been set, or this entity is closer than the previous closest
        closestInteractable = interactable;
      }
    }

    if (highlightedInteractable != null && !highlightedInteractable.equals(closestInteractable)) {
      // Previously highlighted interactable is not eligible for highlighting
      ServiceLocator.getPhysicsService().getPhysics().scheduleEntityForUnhighlight(highlightedInteractable);
      if (closestInteractable != null) {
        // New interactable is eligible for highlighting
        ServiceLocator.getPhysicsService().getPhysics().scheduleEntityForHighlight(closestInteractable);
      }
      highlightedInteractable = closestInteractable;
    }
  }

  private void updateSpeed() {
    // increase speed when running, only when there is stamina left
    if (running && combatStatsComponent.getStamina() > 0) {
      MAX_SPEED.set(6f, 6f); //TODO adjust running speed
    } else {
      MAX_SPEED.set(3f, 3f);
    } Body body = physicsComponent.getBody();
    Vector2 velocity = body.getLinearVelocity();
    Vector2 desiredVelocity = walkDirection.cpy().scl(MAX_SPEED);
    // impulse = (desiredVel - currentVel) * mass
    Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
    body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
  }

  private void updateStamina() {
    // when player is moving and is running, decrease stamina
    if (running && moving) {
      entity.getEvents().trigger("change_stamina", -2);
    } else { // player is not running (released SHIFT or not moving), regenerate stamina
      entity.getEvents().trigger("change_stamina", 1);
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
