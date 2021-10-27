package com.deco2800.game.input.components;

public class KeyboardMenuInputComponent extends InputComponent {

    public KeyboardMenuInputComponent() {
        super(15);
    }

    @Override
    public boolean keyDown(int keycode) {
        entity.getEvents().trigger("key_down", keycode);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return true;
    }
}