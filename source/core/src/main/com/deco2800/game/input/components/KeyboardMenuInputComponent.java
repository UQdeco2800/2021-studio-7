package com.deco2800.game.input.components;

public class KeyboardMenuInputComponent extends InputComponent {
    private boolean menuInUse = false;

    public KeyboardMenuInputComponent() {
        super(10);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (menuInUse) {
            entity.getEvents().trigger("key_down", keycode);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean keyUp(int keycode) {
        if (menuInUse) {
            entity.getEvents().trigger("key_up", keycode);
            return true;
        } else {
            return false;
        }
    }

    public void setMenuInUse(boolean menuInUse) {
        this.menuInUse = menuInUse;
    }

    public boolean isMenuInUse() {
        return menuInUse;
    }
}