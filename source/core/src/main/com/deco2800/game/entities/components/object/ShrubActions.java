package com.deco2800.game.entities.components.object;

import com.deco2800.game.chores.ChoreList;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShrubActions extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(ShrubActions.class);
    private static final String updateAnimation = "update_animation";
    private boolean hasInteracted = false;

    @Override
    public void create() {
        super.create();
        entity.getEvents().trigger(updateAnimation, "Shrub_untrimmed");
    }

    @Override
    public void onInteraction(Entity target) {
        if (target.getComponent(PlayerActions.class) != null) {
            logger.debug("PLAYER interacted with TV, triggering TV animation");
            entity.getEvents().trigger(updateAnimation, "Shrub");
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
                entity.getEvents().trigger(updateAnimation, "Shrub");
            } else {
                entity.getEvents().trigger(updateAnimation, "Shrub_untrimmed_highlight");
            }
        } else {
            logger.debug("TV ended collision with PLAYER, tv animation");
            if (hasInteracted) {
                entity.getEvents().trigger(updateAnimation, "Shrub");
            } else {
                entity.getEvents().trigger(updateAnimation, "Shrub_untrimmed");
            }
        }
    }
}
