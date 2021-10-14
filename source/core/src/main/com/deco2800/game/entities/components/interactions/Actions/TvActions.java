package com.deco2800.game.entities.components.interactions.Actions;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TvActions extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(TvActions.class);

    private boolean hasInteracted = false;

    @Override
    public void create() {
        super.create();
        animator.startAnimation("TV_on1");
    }

    @Override
    public void onInteraction(Entity target) {
        if (target.getComponent(PlayerActions.class) != null) {
            logger.debug("PLAYER interacted with TV, triggering TV animation");
            animator.startAnimation("TV_on1");
            animator.startAnimation("TV_off2");
            hasInteracted = true;
            // Tell the chore controller that this chore is complete
            entity.getEvents().trigger("chore_complete", entity);
        }
    }

    @Override
    public void toggleHighlight(boolean shouldHighlight) {
        if (shouldHighlight) {
            logger.debug("TV started collision with PLAYER, tv animation");
            if (hasInteracted) {
                animator.startAnimation("TV_off2");
            } else {
                animator.startAnimation("TV_onh1");
            }
        } else {
            logger.debug("TV ended collision with PLAYER, tv animation");
            if (hasInteracted) {
                animator.startAnimation("TV_off2");
            } else {
                animator.startAnimation("TV_on1");
            }
        }
    }
}
