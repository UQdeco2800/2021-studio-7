package com.deco2800.game.components.player;

import com.badlogic.gdx.Input;
import com.deco2800.game.input.InputComponent;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

class KeyboardPlayerInputComponentTest {

    @Test
    void keyUpStandUpTest() {
        InputComponent inputComponent = spy(InputComponent.class);
        assertFalse(inputComponent.keyUp(6));
    }

    @Test
    void keyUpStandLeftTest() {
        InputComponent inputComponent = spy(InputComponent.class);
        assertFalse(inputComponent.keyUp(5));
    }

    @Test
    void keyUpStandDownTest() {
        InputComponent inputComponent = spy(InputComponent.class);
        assertFalse(inputComponent.keyUp(4));
    }

    @Test
    void keyUpStandRightTest() {
        InputComponent inputComponent = spy(InputComponent.class);
        assertFalse(inputComponent.keyUp(2));
    }

    @Test
    void keyUpShift_Left() {
        InputComponent inputComponent = spy(InputComponent.class);
        assertFalse(inputComponent.keyUp(3));
    }

    @Test
    void keyUpOther() {
        KeyboardPlayerInputComponent test2 = new KeyboardPlayerInputComponent();
        assertEquals(false, test2.keyUp(Input.Keys.UNKNOWN));
    }

}
