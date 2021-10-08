package com.deco2800.game.input;
import com.deco2800.game.entities.components.player.KeyboardPlayerInputComponent;
import com.deco2800.game.input.components.InputComponent;
import com.deco2800.game.screens.title.KeyboardTitleInputComponent;
import com.deco2800.game.ui.terminal.KeyboardTerminalInputComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * KeyboardInputFactory creates input handlers that process keyboard and touch support.
 */
public class KeyboardInputFactory extends InputFactory {
    private static final Logger logger = LoggerFactory.getLogger(KeyboardInputFactory.class);

    /**
     * Creates an input handler for the player.
     * @return Player input handler
     */
    @Override
    public InputComponent createForPlayer() {
        logger.debug("Creating player input handler");
        return new KeyboardPlayerInputComponent();
    }

    @Override
    public InputComponent createForTitle() {
        logger.debug("Creating title input handler");
        return new KeyboardTitleInputComponent();
    }
    /**
     * Creates an input handler for the terminal.
     *
     * @return Terminal input handler
     */
    public InputComponent createForTerminal() {
        logger.debug("Creating terminal input handler");
        return new KeyboardTerminalInputComponent();
    }
}
