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
    private boolean hasInteracted = false;

    @Override
    public void create() {
        super.create();
        targetLayer = PhysicsLayer.PLAYER;
        animator.startAnimation("TV_on1");
    }

    @Override
    public void onCollisionStart(Fixture me, Fixture other) {
        Entity target = preCollisionCheck(me, other);
        if (target == null) {
            return;
        } else if (target.getComponent(PlayerActions.class) != null) {
            logger.info("TV started collision with PLAYER, tv animation");
            if (hasInteracted) {
                animator.startAnimation("TV_off2");
            } else {
                animator.startAnimation("TV_onh1");
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
            if (hasInteracted) {
                animator.startAnimation("TV_off2");
            } else {
                animator.startAnimation("TV_on1");
            }
        }
    }

    @Override
    public void onInteraction(Entity target) {
        if (target == null) {
            return;
        } else if (target.getComponent(PlayerActions.class) != null) {
            logger.info("TV started collision with SURVEYOR, triggering TV animation");
            if (hasInteracted) {
                hasInteracted = false;
                animator.startAnimation("TV_on1");
            } else {
                hasInteracted = true;
                animator.startAnimation("TV_off1");
                animator.startAnimation("TV_off2");
            }
        }
    }
}
