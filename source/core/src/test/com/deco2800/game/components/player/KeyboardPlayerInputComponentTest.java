package com.deco2800.game.components.player;

import com.badlogic.gdx.Input;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

class KeyboardPlayerInputComponentTest {


    @Test
    void keyUpStandUpTest() {
        KeyboardPlayerInputComponent test1 = spy(KeyboardPlayerInputComponent.class);
        assertEquals(true, test1.keyUp(47));
    }

    @Test
    void keyUpStandLeftTest() {
        KeyboardPlayerInputComponent test2 = new KeyboardPlayerInputComponent();
        assertEquals(true, test2.keyUp(47));
    }

    @Test
    void keyUpStandDownTest() {
        KeyboardPlayerInputComponent test1 = spy(KeyboardPlayerInputComponent.class);
        assertEquals(true, test1.keyUp(87));
    }

    @Test
    void keyUpStandRightTest() {
        KeyboardPlayerInputComponent test2 = new KeyboardPlayerInputComponent();
        assertTrue(test2.keyUp(Input.Keys.W));
    }

    @Test
    void keyUpShift_Left() {
        KeyboardPlayerInputComponent test2 = new KeyboardPlayerInputComponent();
        assertEquals(true, test2.keyUp(Input.Keys.SHIFT_LEFT));
    }

    @Test
    void keyUpOther() {
        KeyboardPlayerInputComponent test2 = new KeyboardPlayerInputComponent();
        assertEquals(false, test2.keyUp(Input.Keys.UNKNOWN));
    }
    //TODO only the last one can pass

}
