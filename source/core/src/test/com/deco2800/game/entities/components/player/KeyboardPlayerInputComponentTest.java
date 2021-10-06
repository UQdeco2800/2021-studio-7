package com.deco2800.game.entities.components.player;

import com.badlogic.gdx.Input;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

class KeyboardPlayerInputComponentTest {


//    @Test
//    void keyDownUpTest() {
//        KeyboardPlayerInputComponent test = new KeyboardPlayerInputComponent();
//        Input.Keys key = new Input.Keys();
//        assertTrue(test.keyDown(key.E));
//    }

    @Test
    void keyUpOther() {
        KeyboardPlayerInputComponent test2 = new KeyboardPlayerInputComponent();
        assertEquals(false, test2.keyUp(Input.Keys.UNKNOWN));
    }

}
