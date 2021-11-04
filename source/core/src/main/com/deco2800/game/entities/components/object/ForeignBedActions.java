package com.deco2800.game.entities.components.object;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.game.GameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ForeignBedActions extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(BedActions.class);
    private static final String UPDATE_ANIMATION = "update_animation";
    private static final String PROMPT_MESSAGE = "Hang on a second... my bed is blue!";

    @Override
    public void create() {
        super.create();
        entity.getEvents().trigger(UPDATE_ANIMATION, "fbed");
    }

    @Override
    public void onInteraction(Entity target) {
        if (target.getComponent(PlayerActions.class) != null) {
            triggerBedInteracted();
        }
    }


    private void triggerBedInteracted() {
        logger.debug("PLAYER interacted with FOREIGN_BED, triggering alert");
        ServiceLocator.getScreen(GameScreen.class).getGameUI().getEvents().trigger("create_textbox", PROMPT_MESSAGE);
    }
}