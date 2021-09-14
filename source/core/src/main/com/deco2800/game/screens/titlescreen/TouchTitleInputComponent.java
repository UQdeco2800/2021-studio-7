package com.deco2800.game.screens.titlescreen;

import com.deco2800.game.input.components.InputComponent;
import com.deco2800.game.ui.terminal.Terminal;

public class TouchTitleInputComponent extends InputComponent {
    private Terminal terminal;

    public TouchTitleInputComponent() {
        super(10);
    }

    public TouchTitleInputComponent(Terminal terminal) {
        this();
        this.terminal = terminal;
    }
}
