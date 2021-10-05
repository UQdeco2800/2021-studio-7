package com.deco2800.game.entities.components.interactions;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;

public interface Interactable {
    /**
     * Performs a check on whether the collision is valid for parsing.
     * @param me should be owned by parent entity
     * @param other owned by target entity
     * @return target entity if valid, otherwise null
     */
    Entity preCollisionCheck(Fixture me, Fixture other);

    /**
     * Responsible for cleaner onCollisionStart method signature and dependencies
     * @param me should be owned by parent entity
     * @param other owned by target entity
     */
    void onPreCollisionStart(Fixture me, Fixture other);

    /**
     * Responsible for cleaner onCollisionEnd method signature and dependencies
     * @param me should be owned by parent entity
     * @param other owned by target entity
     */
    void onPreCollisionEnd(Fixture me, Fixture other);

    /**
     * Called when a valid collision start is detected from one of the parent entity's
     * fixtures and one of the target entity's fixtures
     * @param target entity that has started colliding with the parent entity
     */
    void onCollisionStart(Entity target);

    /**
     * Called when a valid collision end is detected from one of the parent entity's
     * fixtures and one of the target entity's fixtures
     * @param target entity that has ended colliding with the parent entity
     */
    void onCollisionEnd(Entity target);

    /**
     * Called when an interaction should happen between the parent entity and the
     * target entity
     * @param target entity that has requested interaction with the parent entity
     */
    void onInteraction(Entity target);

    /**
     * Called when the object is requested to toggle its highlight. May be extended for
     * multiplayer by checking that the list of colliding player entities is empty.
     * @param shouldHighlight whether the render component should change to a
     *                        highlighted variation or not
     */
    void toggleHighlight(boolean shouldHighlight);
}
