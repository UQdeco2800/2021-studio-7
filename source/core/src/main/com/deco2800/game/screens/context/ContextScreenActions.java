package com.deco2800.game.screens.context;

import com.deco2800.game.GdxGame;
import com.deco2800.game.generic.Component;
import com.deco2800.game.generic.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Context Screen and does something when one of the
 * events is triggered.
 */
public class ContextScreenActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(ContextScreenActions.class);

    @Override
    public void create() {
        //
    }

    /**
     * Swaps to the Main Game Screen.
     */
    public static void playGame() {
        logger.info("Exiting context screen...");
        logger.info("Swapping to main game screen...");
        ServiceLocator.getGame().setScreen(GdxGame.ScreenType.MAIN_GAME);
    }

}
