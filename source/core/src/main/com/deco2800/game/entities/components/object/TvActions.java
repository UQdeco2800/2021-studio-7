package com.deco2800.game.entities.components.object;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TvActions extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(TvActions.class);
    private static final String updateAnimation = "update_animation";
    private static final String tvOn = "TV_on1";
    private static final String tvOff = "TV_off2";
    private boolean hasInteracted = false;

    @Override
    public void create() {
        super.create();
        entity.getEvents().trigger(updateAnimation, tvOn);
    }

    @Override
    public void onInteraction(Entity target) {
        if (target.getComponent(PlayerActions.class) != null) {
            logger.debug("PLAYER interacted with TV, triggering TV animation");
            entity.getEvents().trigger(updateAnimation, tvOn);
            entity.getEvents().trigger(updateAnimation, tvOff);
            hasInteracted = true;
            // Try and tell the chore controller that this chore is complete (if we are a chore)
            entity.getEvents().trigger("chore_complete", entity);
        }
    }

    @Override
    public void toggleHighlight(boolean shouldHighlight) {
        if (shouldHighlight) {
            logger.debug("TV started collision with PLAYER, tv animation");
            if (hasInteracted) {
                entity.getEvents().trigger(updateAnimation, tvOff);
            } else {
                entity.getEvents().trigger(updateAnimation, tvOn);
            }
        } else {
            logger.debug("TV ended collision with PLAYER, tv animation");
            if (hasInteracted) {
                entity.getEvents().trigger(updateAnimation, tvOff);
            } else {
                entity.getEvents().trigger(updateAnimation, tvOn);
            }
        }
    }
}
