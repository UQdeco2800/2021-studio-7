package com.deco2800.game.screens.titlescreen;

import com.deco2800.game.input.components.InputComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyboardTitleInputComponent extends InputComponent {
    private static final Logger logger = LoggerFactory.getLogger(KeyboardTitleInputComponent.class);
    public KeyboardTitleInputComponent(){
        super(5);
    }

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
