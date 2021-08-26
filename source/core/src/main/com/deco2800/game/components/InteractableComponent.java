package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;

/**
 * When this entity collides with the player's hitbox, triggers an event, and
 * triggers another event when interacted with by the player.
 *
 * <p>Requires HitboxComponent (or ColliderComponent with isSensor = True) on
 * this entity.
 */
public class InteractableComponent extends Component {
    private short targetLayer;
    private String collisionEvent;
    private String interactionEvent;
    private HitboxComponent hitboxComponent;
    private boolean isTouching = false;

    /**
     * Create a component which listens for collisions with the player on its
     * target layer, and triggers an event on collision.
     * @param targetLayer The physics layer of the target's collider
     * @param collisionEvent The event to trigger once a collision occurs
     */
    public InteractableComponent (short targetLayer, String collisionEvent) {
        this.targetLayer = targetLayer;
        this.collisionEvent = collisionEvent;
    }

    /**
     * Create a component which listens for collisions with the player on its
     * target layer, triggers an event on collision, and triggers an even once
     * interacted with by the player.
     * @param targetLayer The physics layer of the target's collider
     * @param collisionEvent The event to trigger once a collision occurs
     * @param interactionEvent The event to trigger when the player interacts
     *                         with the object
     */
    public InteractableComponent (short targetLayer, String collisionEvent,
                                  String interactionEvent) {
        this.targetLayer = targetLayer;
        this.collisionEvent = collisionEvent;
        this.interactionEvent = interactionEvent;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart",
                this::onCollisionStart);
        entity.getEvents().addListener("collisionEnd",
                this::onCollisionEnd);
        // TODO Collision start will currently trigger with both the player AND
        //  NPCs. We want it to trigger on just the player, NOT the NPCS.
        entity.getEvents().addListener("interaction",
                this::onInteraction);
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

        // Doesn't do anything yet
        entity.getEvents().trigger("interactionStart");
    }

    private void onCollisionEnd(Fixture me, Fixture other) {
        this.isTouching = false;
        // TODO Undo sprite changes

        // Doesn't do anything yet
        entity.getEvents().trigger("interactionEnd");
    }

    private void onInteraction(Fixture me, Fixture other) {
        if (!isTouching) {
            return;
        }
        // TODO stuff that happens when interacted with

    }
}
