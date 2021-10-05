package com.deco2800.game.entities.components.interactions.Actions;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.maingame.MainGameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MumActions extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(MumActions.class);

    @Override
    public void create() {
        super.create();
        animator.startAnimation("standing_south");
    }

    @Override
    public void onCollisionStart(Entity target) {
        if (target.getComponent(PlayerActions.class) != null) {
            triggerLoseCondition();
        }
    }

    private void triggerLoseCondition() {
        logger.info("MUM started collision with PLAYER, triggering lose condition");
        ((MainGameScreen) ServiceLocator.getGame().getScreen())
                .getMainGameEntity().getEvents().trigger("loss_caught");
    }
}
