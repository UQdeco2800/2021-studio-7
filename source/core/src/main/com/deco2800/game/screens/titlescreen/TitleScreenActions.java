package com.deco2800.game.screens.titlescreen;

import com.deco2800.game.GdxGame;
import com.deco2800.game.generic.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TitleScreenActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(TitleScreenActions.class);
    private GdxGame game;

    public TitleScreenActions(GdxGame game) {
        this.game = game;
    }

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
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

}
