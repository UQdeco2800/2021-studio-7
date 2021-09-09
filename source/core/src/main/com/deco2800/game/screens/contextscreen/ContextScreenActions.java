package com.deco2800.game.screens.contextscreen;

import com.deco2800.game.GdxGame;
import com.deco2800.game.generic.Component;
import com.deco2800.game.screens.endgame.EndGameActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextScreenActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(EndGameActions.class);
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
        logger.info("Exiting end game screen...");
        logger.info("Swapping to next level on main game screen...");
        game.setScreen(GdxGame.ScreenType.MAIN_GAME);
    }

}
