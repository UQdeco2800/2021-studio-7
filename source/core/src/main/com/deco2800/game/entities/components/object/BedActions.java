package com.deco2800.game.entities.components.object;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.game.GameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BedActions extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(BedActions.class);
    private static final String UPDATE_ANIMATION = "update_animation";
    private static final String PROMPT_MESSAGE = "I can't go to bed yet! There are still chores to be done!";

    @Override
    public void create() {
        super.create();
        entity.getEvents().trigger(UPDATE_ANIMATION, "bed");
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
            entity.getEvents().trigger(UPDATE_ANIMATION, "bedhighlight2");
        } else {
            logger.debug("BED ended collision with PLAYER, un-highlighting bed");
            entity.getEvents().trigger(UPDATE_ANIMATION, "bed");
        }
    }

    private void triggerBedInteracted() {
        logger.debug("PLAYER interacted with BED, triggering bed interacted");
        if (ServiceLocator.getChoreController().choresComplete()) {
            ServiceLocator.getScreen(GameScreen.class).getGameUI().getEvents().trigger("bed_interacted");
        } else {
            ServiceLocator.getScreen(GameScreen.class).getGameUI().getEvents().trigger("create_textbox", PROMPT_MESSAGE);
        }
    }
}