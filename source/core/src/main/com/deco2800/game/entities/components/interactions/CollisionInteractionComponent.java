package com.deco2800.game.entities.components.interactions;


import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.generic.Component;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollisionInteractionComponent extends Component implements Interactable {
    private static final Logger logger = LoggerFactory.getLogger(CollisionInteractionComponent.class);

    protected short targetLayer;
    protected HitboxComponent hitbox;

    public void create(){
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
    public void onCollisionStart(Fixture me, Fixture other) {
        Entity target = preCollisionCheck(me, other);
        if (target == null) {
            return;
        } else if (target.getComponent(PlayerActions.class) != null) {
            logger.info("onCollisionStart called on CollisionInteraction between Player and {}", entity);
            onInteraction(target);
        }
    }

    @Override
    public void onCollisionEnd(Fixture me, Fixture other) {
        Entity target = preCollisionCheck(me, other);
        if (target == null) {
            return;
        } else if (target.getComponent(PlayerActions.class) != null) {
            logger.info("onCollisionEnd called on CollisionInteraction between Player and {}", entity);
        }
    }

    @Override
    public void onInteraction(Entity target) {}
}
