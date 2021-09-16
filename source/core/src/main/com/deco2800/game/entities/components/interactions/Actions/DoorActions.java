package com.deco2800.game.entities.components.interactions.Actions;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.physics.PhysicsLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoorActions extends InteractionComponent {

    private static final Logger logger = LoggerFactory.getLogger(BedActions.class);

    @Override
    public void create() {
        super.create();
        targetLayer = PhysicsLayer.PLAYER;
        animator.startAnimation("door_close_left");
    }

    @Override
    public void onCollisionStart(Fixture me, Fixture other) {
        Entity target = preCollisionCheck(me, other);
        if (target == null) {
            return;
        } else if (target.getComponent(PlayerActions.class) != null) {
            logger.info("DOOR started collision with PLAYER, highlighting door");
            animator.startAnimation("Door_left_highlighted");
        }
    }

    @Override
    public void onCollisionEnd(Fixture me, Fixture other) {
        Entity target = preCollisionCheck(me, other);
        if (target == null) {
            return;
        } else if (target.getComponent(PlayerActions.class) != null) {
            logger.info("DOOR ended collision with PLAYER, un-highlighting door");
            animator.startAnimation("door_close_left");
        }
    }

    @Override
    public void onInteraction(Entity target) {
        if (target == null) {
            return;
        } else if (target.getComponent(PlayerActions.class) != null) {
            logger.info("DOOR started collision with SURVEYOR, triggering door animation");
            animator.startAnimation("Door_left_highlihted_d");
            animator.startAnimation("Door_open_left");
        }
    }
}
