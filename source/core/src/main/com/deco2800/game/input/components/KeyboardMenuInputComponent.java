package com.deco2800.game.input.components;

import com.badlogic.gdx.Input.Keys;

public class KeyboardMenuInputComponent extends InputComponent {

    public KeyboardMenuInputComponent() {
        super(5);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.W:
            case Keys.A:
            case Keys.S:
            case Keys.D:
            case Keys.ENTER:
            case Keys.ESCAPE:
                entity.getEvents().trigger("menu_key_pressed", keycode);
                return true;
            default:
                entity.getEvents().trigger("non_menu_key_pressed", keycode);
                return false;
        }
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.W:
            case Keys.A:
            case Keys.S:
            case Keys.D:
            case Keys.ENTER:
            case Keys.ESCAPE:
                return true;
            default:
                return false;
        }
    }
}