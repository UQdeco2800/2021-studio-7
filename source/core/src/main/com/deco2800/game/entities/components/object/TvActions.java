package com.deco2800.game.entities.components.object;

import com.deco2800.game.chores.ChoreList;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TvActions extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(TvActions.class);
    private static final String UPDATE_ANIMATION = "update_animation";
    private static final String TV_ON = "TV_on1";
    private static final String TV_OFF = "TV_off2";
    private boolean hasInteracted = false;


    @Override
    public void create() {
        super.create();
        entity.getEvents().trigger(UPDATE_ANIMATION, TV_ON);
    }

    @Override
    public void onInteraction(Entity target) {
        if (target.getComponent(PlayerActions.class) != null) {
            logger.debug("PLAYER interacted with TV, triggering TV animation");
            entity.getEvents().trigger(UPDATE_ANIMATION, TV_ON);
            entity.getEvents().trigger(UPDATE_ANIMATION, TV_OFF);
            hasInteracted = true;
            // Tell the chore controller that this chore is complete
            entity.getEvents().trigger("chore_complete", ChoreList.TV);
        }
    }

    @Override
    public void toggleHighlight(boolean shouldHighlight) {
        if (shouldHighlight) {
            logger.debug("TV started collision with PLAYER, tv animation");
            if (hasInteracted) {
                entity.getEvents().trigger(UPDATE_ANIMATION, TV_OFF);
            } else {
                entity.getEvents().trigger(UPDATE_ANIMATION, TV_ON);
            }
        } else {
            logger.debug("TV ended collision with PLAYER, tv animation");
            if (hasInteracted) {
                entity.getEvents().trigger(UPDATE_ANIMATION, TV_OFF);
            } else {
                entity.getEvents().trigger(UPDATE_ANIMATION, TV_ON);
            }
        }
    }
}
