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

    private static final int NOT_WORK_0 = 0;
    private static final int NOT_WORK_1 = 1;
    private static final int NOT_WORK_2 = 2;
    private static final int NOT_WORK_3 = 3;
    private static final int WORKING = 4;
    private static final int HIGHLIGHT_OFFSET = 5;
    private static final int HIGHLIGHT_0 = 5;
    private static final int HIGHLIGHT_1 = 6;
    private static final int HIGHLIGHT_2 = 7;
    private static final int HIGHLIGHT_3 = 8;

    @Override
    public void create() {
        super.create();
        entity.getEvents().trigger(UPDATE_ANIMATION, "dishwasher_notworking0");
    }

    @Override
    public void onInteraction(Entity target) {
        if (target.getComponent(PlayerActions.class) != null) {
            triggerDishWasherInteracted();
        }
    }

    @Override
    public void toggleHighlight(boolean shouldHighlight) {
        // Only toggle highlight on unactivated dishes
        //if (count != 0) return;

        if (shouldHighlight) {
            logger.debug("DISHWASHER started collision with PLAYER");
            triggerAnimationChange(count + HIGHLIGHT_OFFSET);
        } else {
            logger.debug("DISHWASHER ended collision with PLAYER");
            triggerAnimationChange(count);
        }
    }

    private void triggerDishWasherInteracted() {
        logger.debug("PLAYER interacted with DISHWASHER, triggering dishwasher interacted");
        if (count == NOT_WORK_0) {
            count++;
            startTime = ServiceLocator.getTimeSource().getTime();
        }
        long currentTime = ServiceLocator.getTimeSource().getTime();
        if (currentTime - startTime >= 500L && count < WORKING){
            count++;
            //triggerAnimationChange(count);
            startTime = ServiceLocator.getTimeSource().getTime();
        }
        triggerAnimationChange(count + HIGHLIGHT_OFFSET);
    }


    private void triggerAnimationChange(int count){
        switch (count) {
            case NOT_WORK_0:
                entity.getEvents().trigger(UPDATE_ANIMATION, "dishwasher_notworking0");
                break;
            case NOT_WORK_1:
                entity.getEvents().trigger(UPDATE_ANIMATION, "dishwasher_notworking1");
                break;
            case NOT_WORK_2:
                entity.getEvents().trigger(UPDATE_ANIMATION, "dishwasher_notworking2");
                break;
            case NOT_WORK_3:
                entity.getEvents().trigger(UPDATE_ANIMATION, "dishwasher_notworking3");
                break;
            case HIGHLIGHT_0:
                entity.getEvents().trigger(UPDATE_ANIMATION, "dishwasher_notworking0_highlight");
                break;
            case HIGHLIGHT_1:
                entity.getEvents().trigger(UPDATE_ANIMATION, "dishwasher_notworking1_highlight");
                break;
            case HIGHLIGHT_2:
                entity.getEvents().trigger(UPDATE_ANIMATION, "dishwasher_notworking2_highlight");
                break;
            case HIGHLIGHT_3:
                entity.getEvents().trigger(UPDATE_ANIMATION, "dishwasher_notworking3_highlight");
                break;
            default:
                entity.getEvents().trigger(UPDATE_ANIMATION, "dishwasher_working");
                entity.getEvents().trigger("chore_complete", ChoreList.DISHWASHER);
                break;
        }
    }
}