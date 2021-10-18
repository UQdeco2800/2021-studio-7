
package com.deco2800.game.entities.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.generic.Component;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;

/**
 * When this entity touches a valid hitbox, enact the unique interaction with them.
 *
 * <p>Requires HitboxComponent on this entity.
 *
 */
public class InteractionComponent extends Component implements Interactable {

    protected short targetLayer;
    protected HitboxComponent hitbox;

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("pre_collision_start", this::onPreCollisionStart);
        entity.getEvents().addListener("pre_collision_end", this::onPreCollisionEnd);
        entity.getEvents().addListener("collision_start", this::onCollisionStart);
        entity.getEvents().addListener("collision_end", this::onCollisionEnd);
        entity.getEvents().addListener("interaction", this::onInteraction);
        entity.getEvents().addListener("toggle_highlight", this::toggleHighlight);

        targetLayer = PhysicsLayer.PLAYER;

        hitbox = entity.getComponent(HitboxComponent.class);
    }

    @Override
    public Entity preCollisionCheck(Fixture me, Fixture other) {
        if (hitbox.getFixture() != me) {
            // Not triggered by hitbox, ignore
            return null;
        }

        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return null;
        }

        return (Entity) other.getBody().getUserData();
    }

    @Override
    public void onPreCollisionStart(Fixture me, Fixture other) {
        Entity target = preCollisionCheck(me, other);
        if (target != null) {
            onCollisionStart(target);
        }
    }

    @Override
    public void onPreCollisionEnd(Fixture me, Fixture other) {
        Entity target = preCollisionCheck(me, other);
        if (target != null) {
            onCollisionEnd(target);
        }
    }

    @Override
    public void onCollisionStart(Entity target) {
        // Interactables do nothing on collision start by default
    }

    @Override
    public void onCollisionEnd(Entity target) {
        // Interactables do nothing on collision end by default
    }

    @Override
    public void onInteraction(Entity target) {
        // Interactables do nothing on interaction by default
    }

    @Override
    public void toggleHighlight(boolean shouldHighlight) {
        // Subclasses should override to specify which textures to toggle
    }
}