package com.deco2800.game.entities.components.interactions.Actions;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TvActions extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(TvActions.class);

    @Override
    public void create() {
        super.create();
        animator.startAnimation("TV_ONA");
    }

    @Override
    public void onInteraction(Entity target) {
        if (target.getComponent(PlayerActions.class) != null) {
            logger.info("PLAYER interacted with TV, triggering TV animation");
            animator.startAnimation("TV_offL");
            animator.startAnimation("TV_offL2");
        }
    }

    @Override
    public void toggleHighlight(boolean shouldHighlight) {
        if (shouldHighlight) {
            logger.debug("TV started collision with PLAYER, tv animation");
            animator.startAnimation("TV_ONA");
            animator.startAnimation("TV_ONB");
            animator.startAnimation("TV_ONC");
        } else {
            logger.info("TV ended collision with PLAYER, tv animation");
            animator.startAnimation("TV_ONA");
            animator.startAnimation("TV_ONB");
            animator.startAnimation("TV_ONC");
        }
    }
}
