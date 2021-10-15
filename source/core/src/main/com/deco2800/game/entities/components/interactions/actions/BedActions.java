package com.deco2800.game.entities.components.interactions.actions;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.maingame.MainGameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BedActions extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(BedActions.class);

    @Override
    public void create() {
        super.create();
        entity.getEvents().trigger("update_animation", "bed");
    }

    @Override
    public void onInteraction(Entity target) {
        if (target.getComponent(PlayerActions.class) != null) {
            triggerBedInteracted();
        }
    }

    @Override
    public void toggleHighlight(boolean shouldHighlight) {
        if (shouldHighlight) {
            logger.debug("BED started collision with PLAYER, highlighting bed");
            entity.getEvents().trigger("update_animation", "bedhighlight2");
        } else {
            logger.debug("BED ended collision with PLAYER, un-highlighting bed");
            entity.getEvents().trigger("update_animation", "bed");
        }
    }

    private void triggerBedInteracted() {
        logger.debug("PLAYER interacted with BED, triggering bed interacted");
        if (ServiceLocator.getChoreController().checkComplete()) {
            // Chores complete, finishing level
            ((MainGameScreen) ServiceLocator.getGame().getScreen())
                    .getMainGameEntity().getEvents().trigger("bed_interacted");
        } else {
            // Still have chores to do, tell the player this
            String string = "I haven't finished all my chores!\n(Try pressing O)";
            ((MainGameScreen) ServiceLocator.getGame().getScreen())
                    .getMainGameEntity().getEvents().trigger("create_textbox", string);
        }
    }
}