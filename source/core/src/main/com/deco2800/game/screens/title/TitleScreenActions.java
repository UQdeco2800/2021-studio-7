package com.deco2800.game.screens.title;

import com.deco2800.game.GdxGame;
import com.deco2800.game.generic.Component;
import com.deco2800.game.generic.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Title Screen and does something when one of the
 * events is triggered.
 */
public class TitleScreenActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(TitleScreenActions.class);

    @Override
    public void create() {
        entity.getEvents().addListener("go_menu", this::goMenu);
    }

    /**
     * Swaps to the Main Menu Screen.
     */
    public void goMenu() {
        logger.info("Exiting title screen...");
        logger.info("Swapping to main menu screen...");
        ServiceLocator.getGame().setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

}
