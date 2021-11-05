package com.deco2800.game.entities.components.object;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.game.GameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class AdviceActions extends InteractionComponent {
    protected static final Logger logger = LoggerFactory.getLogger(AdviceActions.class);
    private static final String[] MESSAGES = new String[]{
            "I could have sworn you used to have neighbours. Guess I'll just stay in your yard now.",
            "Chore menu's getting long, isn't it? Press 'O' to toggle it off.",
            "Some chores require multiple parts, look around to make sure you get it all done!",
            "Oh boy, don't let your mum know you've been slacking again!",
            "It may be beneficial for you to do tasks in a strategic order",
            "Wow you guys sure do remodel often!"
    };
    private static Random generator = new Random();

    @Override
    public void create() {
        super.create();
    }

    @Override
    public void onCollisionStart(Entity target) {
        logger.debug("ADVICE_ANIMAL started collision with PLAYER");
        String msg = MESSAGES[generator.nextInt(MESSAGES.length)];
        ServiceLocator.getScreen(GameScreen.class).getGameUI().getEvents()
                .trigger("create_textbox", msg);
    }
}
