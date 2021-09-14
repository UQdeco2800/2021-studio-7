package com.deco2800.game.entities.components.interactions.Actions;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.physics.PhysicsLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class tvActions extends InteractionComponent {

    private static final Logger logger = LoggerFactory.getLogger(tvActions.class);
    // Checks if the player has interacted with the object
    private boolean interactedWith = false;

    @Override
    public void create() {
        super.create();
        targetLayer = PhysicsLayer.PLAYER;
        animator.startAnimation("TV_ONA");
    }

    @Override
    public void onCollisionStart(Fixture me, Fixture other) {
        Entity target = preCollisionCheck(me, other);
        if (target == null) {
            return;
        } else if (target.getComponent(PlayerActions.class) != null) {
            logger.info("TV started collision with PLAYER, tv animation");
            if (interactedWith) {
                animator.startAnimation("TV_offL2");
            } else {
                animator.startAnimation("TV_ONA");
                animator.startAnimation("TV_ONB");
                animator.startAnimation("TV_ONC");
            }
        }
    }

    @Override
    public void onCollisionEnd(Fixture me, Fixture other) {
        Entity target = preCollisionCheck(me, other);
        if (target == null) {
            return;
        } else if (target.getComponent(PlayerActions.class) != null) {
            logger.info("TV ended collision with PLAYER, tv animation");
            if (interactedWith) {
                animator.startAnimation("TV_offL2");
            } else {
                animator.startAnimation("TV_ONA");
                animator.startAnimation("TV_ONB");
                animator.startAnimation("TV_ONC");
            }
        }
    }

    @Override
    public void onInteraction(Entity target) {
        if (target == null) {
            return;
        } else if (target.getComponent(PlayerActions.class) != null) {
            logger.info("TV started collision with SURVEYOR, triggering TV animation");
            animator.startAnimation("TV_offL");
            animator.startAnimation("TV_offL2");
            interactedWith = true;
        }
    }
}
