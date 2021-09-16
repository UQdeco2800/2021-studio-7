package com.deco2800.game.screens.titlescreen;

import com.deco2800.game.input.components.InputComponent;

/**
 * A class made with KeyboardTitleInputComponent to incorporate the "press any key" function for the
 * title. This is being kept in the game as it allows development in touch screen (e.g. mobile).
 * As this game does not require mouse inputs, its controls are simple and keeping this class
 * can leave opportunities open for easier development.
 */
public class TouchTitleInputComponent extends InputComponent {
    public TouchTitleInputComponent() {
        super(10);
    }
}
