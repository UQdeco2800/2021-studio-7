package com.deco2800.game.screens.contextscreen;

import com.deco2800.game.GdxGame;
import com.deco2800.game.generic.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextScreenActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(ContextScreenActions.class);
    private GdxGame game;

    public ContextScreenActions(GdxGame game) {
        this.game = game;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("play_game", this::playGame);
    }

    /**
     * Swaps to the next level on the Main Game Screen.
     */
    public void playGame() {
        logger.info("Exiting context screen...");
        logger.info("Swapping to main game screen...");
        game.setScreen(GdxGame.ScreenType.MAIN_GAME);
    }

}
