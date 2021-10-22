package com.deco2800.game.screens.context;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.deco2800.game.GdxGame;
import com.deco2800.game.generic.Component;
import com.deco2800.game.generic.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;

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

    @Override
    public void update() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_ENTER)) {
            entity.getComponent(ContextInputProcessor.class).keyDown(Input.Keys.ENTER);
        }
    }

    /**
     * Swaps to the Main Game Screen.
     */
    public static void playGame() {
        logger.info("Exiting context screen...");
        logger.info("Swapping to main game screen...");
        ContextScreen.incrementScreen();
        ServiceLocator.getGame().setScreen(GdxGame.ScreenType.MAIN_GAME);
    }

    public void writeUsername(){
        FileWriter writer = null;
        try {
            writer = new FileWriter("configs/leaderboard.txt",true);
            String username = entity.getComponent(ContextScreenDisplay.class).getUserName() .getText();
            String s = "\n" + username + ":";
            writer.write(s);
            logger.info("Wrote username to leaderboard.");
        } catch (Exception e) {
            logger.debug("Could not write username to leaderboard.");
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                }catch (IOException e) {
                    logger.error("Could not close leaderboard");
                }
            }
        }
    }

}
