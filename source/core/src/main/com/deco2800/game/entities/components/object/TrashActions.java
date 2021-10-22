package com.deco2800.game.entities.components.object;

import com.deco2800.game.chores.ChoreList;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;
import com.deco2800.game.entities.components.SingleUse;
import com.deco2800.game.entities.components.player.PlayerActions;

import com.deco2800.game.generic.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrashActions extends InteractionComponent {
    // Note this class requires the addition of the SingleUse component be added to obstacle entity
    private static final Logger logger = LoggerFactory.getLogger(TrashActions.class);
    private static final String UPDATE_ANIMATION = "update_animation";
    private long startTime;
    private boolean hasInteracted = false;

    @Override
    public void create() {
        super.create();
        entity.getEvents().trigger(UPDATE_ANIMATION, "trash");
    }

    @Override
    public void onInteraction(Entity target) {
        if (target.getComponent(PlayerActions.class) != null) {
            logger.debug("PLAYER interacted with Trash");
            startTime = ServiceLocator.getTimeSource().getTime();
            hasInteracted = true;
            entity.getEvents().trigger(UPDATE_ANIMATION, "dust1");
        }
    }

    @Override
    public void toggleHighlight(boolean shouldHighlight) {
    }

    @Override
    public void update(){
        //long currentTime = ServiceLocator.getTimeSource().getTime();
        if (hasInteracted){
            entity.getComponent(SingleUse.class).remove();
            entity.getEvents().trigger("chore_complete", ChoreList.TRASH);
        }
    }
}