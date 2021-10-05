package com.deco2800.game.entities.components.interactions.Actions;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.screens.maingame.MainGameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BedActions extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(BedActions.class);

    @Override
    public void create() {
        super.create();
        targetLayer = PhysicsLayer.PLAYER;
        animator.startAnimation("bed");
    }

    @Override
    public void onCollisionStart(Entity target) {
        if (target.getComponent(PlayerActions.class) != null) {
            toggleHighlight(true);
        }
    }

    @Override
    public void onCollisionEnd(Entity target) {
        if (target.getComponent(PlayerActions.class) != null) {
            toggleHighlight(false);
        }
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
            logger.info("BED started collision with PLAYER, highlighting bed");
            animator.startAnimation("bed_highlight");
        } else {
            logger.info("BED ended collision with PLAYER, un-highlighting bed");
            animator.startAnimation("bed");
        }
    }

    private void triggerWinCondition() {
        logger.info("PLAYER interacted with BED, triggering win");
        ((MainGameScreen) ServiceLocator.getGame().getScreen())
                .getMainGameEntity().getEvents().trigger("win_default");
    }
}