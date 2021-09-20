package com.deco2800.game.entities.components.interactions;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.generic.Component;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.rendering.components.AnimationRenderComponent;

/**
 * When this entity touches a valid hitbox, enact the unique interaction with them.
 *
 * <p>Requires HitboxComponent on this entity.
 *
 */
public class InteractionComponent extends Component implements Interactable {

    protected short targetLayer;
    protected AnimationRenderComponent animator;
    protected HitboxComponent hitbox;

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("collision_start", this::onCollisionStart);
        entity.getEvents().addListener("collision_end", this::onCollisionEnd);
        entity.getEvents().addListener("interaction", this::onInteraction);

        targetLayer = PhysicsLayer.PLAYER;

        animator = entity.getComponent(AnimationRenderComponent.class);
        if (animator != null) {
            entity.getEvents().addListener("update_animation", animator::startAnimation);
        }

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

        return ((BodyUserData) other.getBody().getUserData()).entity;
    }

    @Override
    public void onCollisionStart(Fixture me, Fixture other) {}

    @Override
    public void onCollisionEnd(Fixture me, Fixture other) {}

    @Override
    public void onInteraction(Entity target) {}
}
