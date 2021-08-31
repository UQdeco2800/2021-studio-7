package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.player.PlayerObjectInteractions;
import com.deco2800.game.components.player.KeyboardPlayerInputComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * When this entity collides with the player's hitbox, triggers an event, and
 * triggers another event when interacted with by the player.
 *
 * <p>Requires HitboxComponent (or ColliderComponent with isSensor = True) on
 * this entity.
 */
public class InteractableComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(PlayerObjectInteractions.class);
    private Entity player;
    private short targetLayer;
    private String collisionEvent;
    private String interactionEvent;
    private HitboxComponent hitboxComponent;
    private TextureRenderComponent textureComponent;
    private KeyboardPlayerInputComponent inputComponent;
    private ColliderComponent colliderComponent;
    private boolean isTouching = false;

    public InteractableComponent(short targetLayer){
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
    }

    @Override
    public void create() {
        // TODO currently for animation purposes
        entity.getEvents().trigger("interactionEnd");

        entity.getEvents().addListener("collisionStart",
                this::onCollisionStart);
        entity.getEvents().addListener("collisionEnd",
                this::onCollisionEnd);
        // TODO Collision start will currently trigger with both the player AND
        //  NPCs. We want it to trigger on just the player, NOT the NPCS.
        // Should trigger when the player presses the E key
        player.getEvents().addListener("interaction", this::onInteraction);

        hitboxComponent = entity.getComponent(HitboxComponent.class);
        textureComponent = entity.getComponent(TextureRenderComponent.class);
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
        //System.out.println("touching interactable object");

        // Doesn't do anything yet (For animations probably)
        entity.getEvents().trigger("interactionStart");
    }

    private void onCollisionEnd(Fixture me, Fixture other) {
        this.isTouching = false;
        // TODO Undo sprite changes

        // Doesn't do anything yet (For animations probably)
        entity.getEvents().trigger("interactionEnd");
    }

    /**
     * Function that is called when the player presses the interact key (currently E)
     */
    public void onInteraction() {
        if(isTouching) {
            // TODO Stuff that happens once interacted with
            System.out.println("Successful interaction");
        }
    }
}
