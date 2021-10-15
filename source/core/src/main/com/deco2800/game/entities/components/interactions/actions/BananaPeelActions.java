package com.deco2800.game.entities.components.interactions.actions;

import com.deco2800.game.ai.components.AITaskComponent;
import com.deco2800.game.ai.tasks.SlipTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BananaPeelActions extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(BananaPeelActions.class);

    @Override
    public void create(){
        super.create();
        entity.getEvents().trigger("update_animation", "banana_peel");
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
            logger.debug("PEEL started collision with PLAYER, executing task and removing object");
            try {
                SlipTask slipTask = (SlipTask)
                        target.getComponent(AITaskComponent.class).getPriorityTask(SlipTask.class);
                slipTask.changePriority(1);
            } catch (NullPointerException e) {
                logger.debug("SlipTask does not exist in player AIComponent");
            }
        } else {
            logger.debug("PEEL ended collision with PLAYER");
        }
    }
}
