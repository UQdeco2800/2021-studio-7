package com.deco2800.game.components;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.deco2800.game.components.player.KeyboardPlayerInputComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.events.listeners.EventListener;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;


/**
 * When this entity collides with the player's hitbox, triggers an event, and
 * triggers another event when interacted with by the player.
 *
 * <p>Requires HitboxComponent (or ColliderComponent with isSensor = True) on
 * this entity.
 */
public class InteractableComponent extends Component {
    private Entity player;
    private short targetLayer;
    private String collisionEvent;
    private String interactionEvent;
    private HitboxComponent hitboxComponent;
    private KeyboardPlayerInputComponent inputComponent;
    private boolean isTouching = false;


    /**
     * Create a component which listens for collisions with the player on its
     * target layer, and triggers an event on collision.
     * UPDATE: 27/08/21 2:17AM collisionEvent string is temporarily going to be
     * used to parse a texture string for demo purposes. - Treff
     * //TODO Implement animation system for texture changes
     * @param player The player entity
     * @param targetLayer The physics layer of the target's collider
     */
    public InteractableComponent (Entity player, short targetLayer) {
        this.player = player;
        this.targetLayer = targetLayer;
    }

    /**
     * Create a component which listens for collisions with the player on its
     * target layer, and triggers an event on collision.
     * UPDATE: 27/08/21 2:17AM collisionEvent string is temporarily going to be
     * used to parse a texture string for demo purposes. - Treff
     * //TODO Implement animation system for texture changes
     * @param player The player entity
     * @param targetLayer The physics layer of the target's collider
     * @param collisionEvent The event to trigger once a collision occurs
     */
    public InteractableComponent (Entity player, short targetLayer, String collisionEvent) {
        this.player = player;
        this.targetLayer = targetLayer;
        this.collisionEvent = collisionEvent;

        // Creates Collider component for door, bed and player
        this.colliderComponent = new ColliderComponent();

        // sets if an object is a sensor to true
        this.colliderComponent.setSensor(true);

        // Sets a box around the
        this.colliderComponent.setAsBox(entity.getScale(), entity.getCenterPosition());
    }

    /**
     * Create a component which listens for collisions with the player on its
     * target layer, triggers an event on collision, and triggers an even once
     * interacted with by the player.
     * @param player The player entity
     * @param targetLayer The physics layer of the target's collider
     * @param collisionEvent The event to trigger once a collision occurs
     * @param interactionEvent The event to trigger when the player interacts
     *                         with the object
     */
    public InteractableComponent (Entity player, short targetLayer, String collisionEvent,
                                  String interactionEvent) {
        this.player = player;
        this.interactionEvent = interactionEvent;
        this.targetLayer = targetLayer;
        this.collisionEvent = collisionEvent;

        // Creates Collider component for door, bed and player
        this.colliderComponent = new ColliderComponent();

        // sets if an object is a sensor to true
        this.colliderComponent.setSensor(true);

        this.colliderComponent.setAsBox(entity.getScale(), entity.getCenterPosition());
    }

    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart",
                this::onCollisionStart);
        entity.getEvents().addListener("collisionEnd",
                this::onCollisionEnd);
        // TODO Collision start will currently trigger with both the player AND
        //  NPCs. We want it to trigger on just the player, NOT the NPCS.

        // Should trigger when the player presses the E key
        try {
            player.getEvents().addListener("interaction", this::onInteraction);
        } catch (Exception e) {
            System.out.println("Exception: %e");
        }

        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }

    private void onCollisionStart(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            // Not triggered by hitbox, ignore

            return;
        }

        if (!PhysicsLayer.contains(targetLayer,
                other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }

        this.isTouching = true;
        // TODO Sprite changes and trigger event
        System.out.println("touching interactable object");

        // Doesn't do anything yet (For animations probably)
        entity.getEvents().trigger("interactionStart");
    }

    private void onCollisionEnd(Fixture me, Fixture other) {
        this.isTouching = false;
        // TODO Undo sprite changes

        // Doesn't do anything yet (For animations probably)
        entity.getEvents().trigger("interactionEnd");
    }

    public void onInteraction(Fixture me, Fixture other) {
        if (!isTouching) {
            return;
        }
        // TODO stuff that happens when interacted with
        System.out.println("Interacted with object!");
    }


    /**
     * Updates the players hitbox position as the player moves around
     */
    private void updatePlayerHitbox(){

        // Find a way to reference the player
        this.colliderComponent.dispose();
        this.colliderComponent.setAsBox(entity.getScale(), entity.getCenterPosition());
    }
}
