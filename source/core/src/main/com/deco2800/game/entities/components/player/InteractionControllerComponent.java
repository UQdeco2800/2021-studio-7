package com.deco2800.game.entities.components.player;

import com.badlogic.gdx.math.Vector2;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.physics.PhysicsLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class InteractionControllerComponent extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(InteractionControllerComponent.class);
    // Highlighting entities
    private static final long MIN_TIME_BETWEEN_HIGHLIGHTS = 50L;
    private long timeSinceLastHighlight = 0L;
    private static final String TOGGLE_HIGHLIGHT = "toggle_highlight";
    // Interacting with entities
    private static final long MIN_TIME_BETWEEN_INTERACTIONS = 100L;
    private long timeSinceLastInteraction = 0L;
    private boolean isInteracting = false;
    private final List<Entity> interactables = new ArrayList<>();
    private Entity highlightedInteractable = null;

    @Override
    public void create() {
        super.create();
        targetLayer = PhysicsLayer.OBSTACLE;
        entity.getEvents().addListener("toggle_interacting", this::toggleInteracting);
    }

    @Override
    public void onCollisionStart(Entity target) {
        if (target.getEvents().hasListener(TOGGLE_HIGHLIGHT) &&
                !interactables.contains(target)) {
            logger.debug("Added interactable to list");
            interactables.add(target);
        }
    }

    @Override
    public void onCollisionEnd(Entity target) {
        boolean removed = interactables.remove(target);
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
     * @param isInteracting true if interaction key is pressed down, otherwise false
     */
    private void toggleInteracting(boolean isInteracting) {
        this.isInteracting = isInteracting;
    }

    /**
     * Ran periodically, looks for the closest interactable entity. If the previous highlighted entity
     * is not the closest, stop highlighting it. If the closest entity is not currently highlighted,
     * start highlighting it.
     */
    protected void updateInteractableHighlights() {
        Vector2 playerPos = entity.getPosition();
        Entity closestInteractable = null;

        for (Entity interactable : interactables) {
            if (closestInteractable == null ||
                    entity.getPosition().dst(playerPos) < closestInteractable.getPosition().dst(playerPos)) {
                // Closest hasn't been set, or this entity is closer than the previous closest
                closestInteractable = interactable;
            }
        }

        if (highlightedInteractable != null && !highlightedInteractable.equals(closestInteractable)) {
            // Previously highlighted interactable is not eligible for highlighting, schedule for unhighlight
            highlightedInteractable.getEvents().trigger(TOGGLE_HIGHLIGHT, false);
        }

        if (closestInteractable != null && !closestInteractable.equals(highlightedInteractable)) {
            // New interactable is eligible for highlighting, schedule for highlight
            closestInteractable.getEvents().trigger(TOGGLE_HIGHLIGHT, true);
        }

        highlightedInteractable = closestInteractable;
    }

    /**
     * Triggers the interaction event on the currently highlighted interactable entity.
     */
    protected void triggerInteraction() {
        if (highlightedInteractable != null) {
            highlightedInteractable.getEvents().trigger("interaction", entity);
        }
    }

    public List<Entity> getInteractables() {
        return interactables;
    }

    public Entity getHighlightedInteractable() {
        return highlightedInteractable;
    }

    public boolean isInteracting() {
        return isInteracting;
    }
}
