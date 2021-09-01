package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.player.PlayerObjectInteractions;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * When this entity collides with the player's hitbox, triggers an event, and triggers another
 * event when interacted with (with the E key) by the player.
 *
 * <p>Requires HitboxComponent on this entity
 */
public class InteractableComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(PlayerObjectInteractions.class);
    private Entity player;
    private short targetLayer;
    private String interactionEvent;
    private String objectType;
    private HitboxComponent hitboxComponent;
    private boolean isTouching = false;

    /**
     * Create a component which listens for collisions with the player on its target layer, and
     * triggers an event on collision.
     *
     * @param player The player entity
     */
    public InteractableComponent (Entity player) {
        this.player = player;
    }

    /**
     * Create a component which listens for collisions with the player, and triggers a specified
     * event on collision. The event must be created for this entity, an event from another
     * entity cannot (currently) be passed.
     *
     * @param player The player entity
     * @param objectType Identifies what sort of object the component is
     *                   called on, and informs interactions.
     */
    public InteractableComponent (Entity player, String objectType) {
        this.player = player;
        this.objectType = objectType;
        this.interactionEvent = interactionEvent;
    }

    @Override
    public void create() {
        this.targetLayer = PhysicsLayer.PLAYER;

        entity.getEvents().trigger("interactionEnd"); // Needed for animation purposes

        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);

        // E key is pressed
        player.getEvents().addListener("interaction", this::onInteraction);

        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }

    private void onCollisionStart(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            // Not triggered by hitbox, ignore
            return;
        }

        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }

        this.isTouching = true;
        entity.getEvents().trigger("interactionStart");
    }

    private void onCollisionEnd(Fixture me, Fixture other) {
        this.isTouching = false;
        entity.getEvents().trigger("interactionEnd");
    }

    /**
     * Function that is called when the player presses the interact key (currently E)
     */
    public void onInteraction() {
        if(isTouching) {
            if(interactionEvent != null) {
                try {
                    entity.getEvents().trigger(interactionEvent);
                } catch (NullPointerException e) {
                    logger.error("No interaction event passed to InteractableComponent");
                }
            }
            objectTypeEvent();
        }
    }


    public void objectTypeEvent(){
        System.out.println("ObjectInteractionCalled");
        switch(this.objectType){
            case "bed":
                System.out.println("For bed!");
                entity.getEvents().trigger("lossTimed");
                break;
            default:
                return;
        }
    }
}
