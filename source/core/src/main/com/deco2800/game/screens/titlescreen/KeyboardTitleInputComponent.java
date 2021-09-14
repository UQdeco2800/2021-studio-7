package com.deco2800.game.screens.titlescreen;

import com.deco2800.game.input.components.InputComponent;

public class KeyboardTitleInputComponent extends InputComponent {
    public KeyboardTitleInputComponent(){
        super(5);
    }

    @Override
    public boolean keyDown(int keycode) {
        entity.getEvents().trigger("go_menu");
        return true;
    }
}
