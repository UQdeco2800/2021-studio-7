package com.deco2800.game.entities.components.object;

import com.deco2800.game.chores.ChoreList;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlantActions extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(PlantActions.class);
    private static final String UPDATE_ANIMATION = "update_animation";
    private static final String POT_PLANT = "pot_plant";
    private boolean hasInteracted = false;

    @Override
    public void create() {
        super.create();
        entity.getEvents().trigger(UPDATE_ANIMATION, "dead_plant");
    }

    @Override
    public void onInteraction(Entity target) {
        if (target.getComponent(PlayerActions.class) != null) {
            logger.debug("PLAYER interacted with TV, triggering TV animation");
            entity.getEvents().trigger(UPDATE_ANIMATION, POT_PLANT);
            hasInteracted = true;
            // Tell the chore controller that this chore is complete
            entity.getEvents().trigger("chore_complete", ChoreList.PLANT);
        }
    }

    @Override
    public void toggleHighlight(boolean shouldHighlight) {
        if (shouldHighlight) {
            logger.debug("TV started collision with PLAYER, tv animation");
            if (hasInteracted) {
                entity.getEvents().trigger(UPDATE_ANIMATION, POT_PLANT);
            } else {
                entity.getEvents().trigger(UPDATE_ANIMATION, "dead_plant_highlight");
            }
        } else {
            logger.debug("TV ended collision with PLAYER, tv animation");
            if (hasInteracted) {
                entity.getEvents().trigger(UPDATE_ANIMATION, POT_PLANT);
            } else {
                entity.getEvents().trigger(UPDATE_ANIMATION, "dead_plant");
            }
        }
    }
}
