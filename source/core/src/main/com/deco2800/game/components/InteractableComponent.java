package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.player.PlayerObjectInteractions;
import com.deco2800.game.components.player.KeyboardPlayerInputComponent;
import com.deco2800.game.entities.Entity;
//import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.BodyUserData;
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
    //private static final Logger logger = LoggerFactory.getLogger(PlayerObjectInteractions.class);
    private final Entity player;
    private final short targetLayer;
    private String collisionEvent;
    private String interactionEvent;
    private HitboxComponent hitboxComponent;
    private TextureRenderComponent textureComponent;
    private KeyboardPlayerInputComponent inputComponent;
    private ColliderComponent colliderComponent;
    private boolean isTouching = false;

    /**
     * Create a component which listens for collisions with the player on its
     * target later.
     * @param player The player entity
     * @param targetLayer The physics layer of the player's collider
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
     * @param targetLayer The physics layer of the player's collider
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
     * @param player the player entity
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

        // Listen for when the E key is pressed
        player.getEvents().addListener("interaction", this::Interaction);

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

        if (((BodyUserData) other.getBody().getUserData()).entity != player) {
            // Didn't collide with the player entity
            // TODO I don't even think the NPCs CAN collide with this, but just in case...
            System.out.println("Collision! But not with player");
            return;
        }

        this.isTouching = true;

        // TODO Doesn't do anything yet - animations?
        entity.getEvents().trigger("interactionStart");
    }

    private void onCollisionEnd(Fixture me, Fixture other) {
        this.isTouching = false;

        // TODO Doesn't do anything yet - animations?
        entity.getEvents().trigger("interactionEnd");
    }

    /*
    private void onInteraction(Fixture me, Fixture other) {
        if (!isTouching) {
            System.out.print("You are not touching an interactable object");
            return;
        } System.out.println("Imagine something cools it happening");
        // TODO stuff that happens when interacted with
        System.out.println("Interacted with object!");
    }
    */

    /**
     * Function that runs when the player presses the E key
     */
    public void Interaction() {
        if (isTouching) {
            // The player pressed the E key AND we're still touching the object
            System.out.println("INTERACTION - For realsies");

        }
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
