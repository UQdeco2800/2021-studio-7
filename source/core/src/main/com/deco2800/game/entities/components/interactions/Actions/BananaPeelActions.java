package com.deco2800.game.entities.components.interactions.Actions;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BananaPeelActions extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(BananaPeelActions.class);

    //private Vector2 slipstream = Vector2(3,3);

    @Override
    public void create(){
        super.create();
        animator.startAnimation("banana_peel");
    }

    @Override
    public void onCollisionStart(Entity target) {
        if (target.getComponent(PlayerActions.class) != null) {
            toggleSlipPlayer(target, true);
        }
    }

    @Override
    public void onCollisionEnd(Entity target) {
        if (target.getComponent(PlayerActions.class) != null) {
            toggleSlipPlayer(target, false);
        }
    }

    private void toggleSlipPlayer(Entity target, boolean shouldSlip) {
        if (shouldSlip) {
            logger.info("PEEL started collision with PLAYER, executing task and removing object");
            // Play some sort of slip animation
            // Alter player Vector2
        } else {
            logger.info("PEEL ended collision with PLAYER");
            // Something
        }
    }
}
