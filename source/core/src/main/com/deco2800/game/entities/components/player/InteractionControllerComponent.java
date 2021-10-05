package com.deco2800.game.entities.components.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.InteractionComponent;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.physics.PhysicsLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InteractionControllerComponent extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(InteractionControllerComponent.class);
    // Highlighting entities
    private static final long MIN_TIME_BETWEEN_HIGHLIGHTS = 50L;
    private long timeSinceLastHighlight = 0L;
    // Interacting with entities
    private static final long MIN_TIME_BETWEEN_INTERACTIONS = 100L;
    private long timeSinceLastInteraction = 0L;
    private boolean isInteracting = false;
    private final Array<Entity> interactables = new Array<>();
    private Entity highlightedInteractable = null;

    @Override
    public void create() {
        super.create();
        targetLayer = PhysicsLayer.OBSTACLE;
        entity.getEvents().addListener("key_e", this::keyE);
        entity.getEvents().trigger("update_animation", "standing_south");
    }

    @Override
    public void onCollisionStart(Entity target) {
        if (target.getEvents().hasListener("toggle_highlight") &&
                !interactables.contains(target, true)) {
            logger.debug("Added interactable to list");
            interactables.add(target);
        }
    }

    @Override
    public void onCollisionEnd(Entity target) {
        boolean removed = interactables.removeValue(target, true);
        if (removed) {
            logger.debug("Removed interactable to list");
        }
    }

    @Override
    public void update() {
        long currentTime = ServiceLocator.getTimeSource().getTime();
        if (currentTime - timeSinceLastHighlight >= MIN_TIME_BETWEEN_HIGHLIGHTS) {
            timeSinceLastHighlight = currentTime;
            updateInteractableHighlights();
        }
        if (currentTime - timeSinceLastInteraction >= MIN_TIME_BETWEEN_INTERACTIONS && isInteracting) {
            timeSinceLastInteraction = currentTime;
            triggerInteraction();
        }
    }

    /**
     * Raw input parsing on the E key
     * @param isDown true if key is currently down, otherwise false
     */
    private void keyE(boolean isDown) {
        isInteracting = isDown;
    }

    /**
     * Ran periodically, looks for the closest interactable entity. If the previous highlighted entity
     * is not the closest, stop highlighting it. If the closest entity is not currently highlighted,
     * start highlighting it.
     */
    private void updateInteractableHighlights() {
        Vector2 playerPos = entity.getPosition();
        Entity closestInteractable = null;

        for (Entity interactable : new Array.ArrayIterator<>(interactables)) {
            if (closestInteractable == null ||
                    entity.getPosition().dst(playerPos) < closestInteractable.getPosition().dst(playerPos)) {
                // Closest hasn't been set, or this entity is closer than the previous closest
                closestInteractable = interactable;
            }
        }

        if (highlightedInteractable != null && !highlightedInteractable.equals(closestInteractable)) {
            // Previously highlighted interactable is not eligible for highlighting, schedule for unhighlight
            highlightedInteractable.getEvents().trigger("toggle_highlight", false);
        }

        if (closestInteractable != null && !closestInteractable.equals(highlightedInteractable)) {
            // New interactable is eligible for highlighting, schedule for highlight
            closestInteractable.getEvents().trigger("toggle_highlight", true);
        }

        highlightedInteractable = closestInteractable;
    }

    /**
     * Triggers the interaction event on the currently highlighted interactable entity.
     */
    private void triggerInteraction() {
        if (highlightedInteractable != null) {
            highlightedInteractable.getEvents().trigger("interaction", entity);
        }
    }
}
