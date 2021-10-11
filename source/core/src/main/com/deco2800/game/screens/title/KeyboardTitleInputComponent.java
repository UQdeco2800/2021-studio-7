package com.deco2800.game.screens.title;

import com.deco2800.game.input.components.InputComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class made with TouchTitleInputComponent to incorporate the "press any key" function for the
 * title.
 */
public class KeyboardTitleInputComponent extends InputComponent {
    private static final Logger logger = LoggerFactory.getLogger(KeyboardTitleInputComponent.class);
    public KeyboardTitleInputComponent(){
        super(5);
    }

    /**
     * This function reads whether any button has been pushed down, to move to the next screen.
     *
     * @param keycode the code for any key
     * @return true as any key can be pressed
     */
    @Override
    public boolean keyDown(int keycode) {
        try {
            entity.getEvents().trigger("go_menu");
        }
        catch (Exception e) {
            logger.debug("Could not go to main menu");
        }
        return true;
    }
}
