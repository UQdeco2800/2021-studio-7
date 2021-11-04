package com.deco2800.game.entities.components.object;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.game.GameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdviceActions extends InteractionComponent {
    protected static final Logger logger = LoggerFactory.getLogger(AdviceActions.class);
    private static final String MESSAGE = "I could have sworn you used to have neighbours. Guess I'll just stay in your yard now.";

    @Override
    public void create() {
        super.create();
    }

    @Override
    public void onCollisionStart(Entity target) {
        logger.debug("ADVICE_ANIMAL started collision with PLAYER");
        ServiceLocator.getScreen(GameScreen.class).getGameUI().getEvents()
                .trigger("create_textbox", MESSAGE);
    }
}
