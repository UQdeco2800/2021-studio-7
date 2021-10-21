package com.deco2800.game.entities.components.object;

import com.deco2800.game.chores.ChoreList;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.generic.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WashingDishesActions extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(WashingDishesActions.class);
    private static final String UPDATE_ANIMATION = "update_animation";
    private int count = 0;
    private long startTime;

    @Override
    public void create() {
        super.create();
        entity.getEvents().trigger(UPDATE_ANIMATION, "dishwasher_notworking");
    }

    @Override
    public void onInteraction(Entity target) {
        if (target.getComponent(PlayerActions.class) != null) {
            triggerDishWasherInteracted();
        }
    }

    @Override
    public void toggleHighlight(boolean shouldHighlight) {
        if (shouldHighlight) {
            logger.debug("DISHWASHER started collision with PLAYER");
        } else {
            logger.debug("DISHWASHER ended collision with PLAYER");
        }
        triggerAnimationChange(count);
    }

    private void triggerDishWasherInteracted() {
        logger.debug("PLAYER interacted with DISHWASHER, triggering dishwasher interacted");
        if (count == 0) {
            count++;
            triggerAnimationChange(count);
            startTime = ServiceLocator.getTimeSource().getTime();
        }
        long currentTime = ServiceLocator.getTimeSource().getTime();
        if (currentTime - startTime >= 500L){
            count++;
            triggerAnimationChange(count);
            startTime = ServiceLocator.getTimeSource().getTime();
        }
    }


    private void triggerAnimationChange(int count){
        switch (count) {
            case 0:
                entity.getEvents().trigger(UPDATE_ANIMATION, "dishwasher_notworking");
                break;
            case 1:
                entity.getEvents().trigger(UPDATE_ANIMATION, "dishwasher_notworking2");
                break;
            case 2:
                entity.getEvents().trigger(UPDATE_ANIMATION, "dishwasher_notworking3");
                break;
            case 3:
                entity.getEvents().trigger(UPDATE_ANIMATION, "dishwasher_notworking4");
                break;
            default:
                entity.getEvents().trigger(UPDATE_ANIMATION, "dishwasher_working");
                entity.getEvents().trigger("chore_complete", ChoreList.DISHWASHER);
                break;
        }
    }
}