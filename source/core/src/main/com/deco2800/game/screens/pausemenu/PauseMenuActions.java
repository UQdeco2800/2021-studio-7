package com.deco2800.game.screens.pausemenu;

import com.deco2800.game.GdxGame;
import com.deco2800.game.generic.Component;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.mainmenu.MainMenuActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PauseMenuActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(PauseMenuActions.class);

    @Override
    public void create() {
        entity.getEvents().addListener("resume", this::onResume);
        entity.getEvents().addListener("restart", this::onRestart);
        entity.getEvents().addListener("settings", this::onSettings);
        entity.getEvents().addListener("main_menu", this::onMainMenu);
    }

    /**
     * Restarts game, by resetting game by running start.
     */
    private void onRestart() {
        logger.info("Restart game");
//        ServiceLocator.getGame().dispose();
        ServiceLocator.getGame().setScreen(GdxGame.ScreenType.MAIN_GAME);
        //TODO
        // How will restarting work
    }

    /**
     * Resumes game, by unpausing game state.
     */
    private void onResume() {
        logger.info("Resume game");
        ServiceLocator.getGame().setScreen(GdxGame.ScreenType.MAIN_GAME);
        ServiceLocator.getGame().resume();
    }

    /**
     * Opens settings menu.
     */
    private void onSettings() {
        logger.info("Launch settings screen");
        ServiceLocator.getGame().setScreen(GdxGame.ScreenType.SETTINGS);
    }

    /**
     * Opens main menu.
     */
    private void onMainMenu() {
        logger.info("Launch main menu screen");
        ServiceLocator.getGame().setScreen(GdxGame.ScreenType.MAIN_MENU);
    }
}
