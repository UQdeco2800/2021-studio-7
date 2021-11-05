package com.deco2800.game.entities.components.object;

import com.deco2800.game.ai.components.AITaskComponent;
import com.deco2800.game.ai.tasks.SlipTask;
import com.deco2800.game.chores.ChoreList;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.generic.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PuddleActions extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(BananaPeelActions.class);

    @Override
    public void create() {
        super.create();
        entity.getEvents().trigger("update_animation", "puddle");
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

    @Override
    public void onInteraction(Entity target) {
        if (target.getComponent(PlayerActions.class) != null) {
            logger.debug("PLAYER interacted with PUDDLE, queues PUDDLE for removal");
            entity.getEvents().trigger("chore_complete", ChoreList.PUDDLE);
            ServiceLocator.getEntityService().scheduleEntityForRemoval(entity);
        }
    }

    private void toggleSlipPlayer(Entity target, boolean shouldSlip) {
        if (shouldSlip) {
            logger.debug("PUDDLE started collision with PLAYER, executing task and removing object");
            try {
                SlipTask slipTask = (SlipTask)
                    target.getComponent(AITaskComponent.class).getPriorityTask(SlipTask.class);
                slipTask.changePriority(1);
            } catch (NullPointerException e) {
                logger.error("SlipTask does not exist in player AIComponent");
            }
        } else {
            logger.debug("PUDDLE ended collision with PLAYER");
        }
    }
}
