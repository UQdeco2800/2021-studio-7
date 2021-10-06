package com.deco2800.game.entities.components.interactions.Actions;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.maingame.MainGameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BedActions extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(BedActions.class);

    @Override
    public void create() {
        super.create();
        animator.startAnimation("bed");
    }

    @Override
    public void onInteraction(Entity target) {
        if (target.getComponent(PlayerActions.class) != null) {
            triggerWinCondition();
        }
    }

    @Override
    public void toggleHighlight(boolean shouldHighlight) {
        if (shouldHighlight) {
            logger.debug("BED started collision with PLAYER, highlighting bed");
            animator.startAnimation("bedhighlight1");
        } else {
            logger.debug("BED ended collision with PLAYER, un-highlighting bed");
            animator.startAnimation("bed");
        }
    }

    private void triggerWinCondition() {
        logger.debug("PLAYER interacted with BED, triggering win");
        ((MainGameScreen) ServiceLocator.getGame().getScreen())
                .getMainGameEntity().getEvents().trigger("win_default");
    }
}