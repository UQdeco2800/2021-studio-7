package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;

import java.awt.*;

/**
 * When this entity collides with the player's hitbox, triggers an event, and
 * triggers another event when interacted with by the player.
 *
 * <p>Requires HitboxComponent (or ColliderComponent with isSensor = True) on
 * this entity.
 */
public class InteractableComponent extends Component {
    private short targetLayer;
    Rectangle playerHitBox;
    Rectangle bedHitBox;
    Rectangle doorHitBox;

    /**
     * Create a component which listens for collisions with the player on its
     * target layer, and triggers an event on collision.
     * @param targetLayer The physics layer of the target's collider
     * @param collisionEvent The event to trigger once a collision occurs
     */
    public InteractableComponent (short targetLayer, String collisionEvent) {
        this.targetLayer = targetLayer;
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
    }

    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart",
                this::onCollisionStart);
        // TODO Collision start will currently trigger with both the player AND
        //  NPCs. We want it to trigger on just the NPCS
    }

    private void onCollisionStart(Fixture me, Fixture other) {

    }
}
