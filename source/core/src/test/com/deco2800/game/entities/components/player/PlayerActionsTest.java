package com.deco2800.game.entities.components.player;

import com.badlogic.gdx.math.Vector2;
import static org.junit.jupiter.api.Assertions.*;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(GameExtension.class)
public class PlayerActionsTest {

    @Test
    void createTest() {
        PlayerActions test1 = mock(PlayerActions.class);
        doNothing().when(test1).create();
        test1.create();
        verify(test1, times(1)).create();
    }

    @Test
    void updateTest() {
        PlayerActions test1 = mock(PlayerActions.class);
        doNothing().when(test1).update();
        test1.update();
        test1.update();
        verify(test1, times(2)).update();
    }

    @Test
    void stopWalkingTest() {
        PlayerActions test1 = mock(PlayerActions.class);
        doNothing().when(test1).stopWalking();
        test1.stopWalking();
        test1.stopWalking();
        test1.stopWalking();
        verify(test1, times(3)).stopWalking();
    }

    @Test
    void runTest() {
        PlayerActions test1 = mock(PlayerActions.class);
        doNothing().when(test1).run();
        test1.run();
        test1.run();
        verify(test1, times(2)).run();
    }

    @Test
    void stopRunningTest() {
        PlayerActions test1 = mock(PlayerActions.class);
        doNothing().when(test1).stopRunning();
        test1.stopRunning();
        test1.stopRunning();
        verify(test1, times(2)).stopRunning();
    }

    @Test
    void walkTest() {
        PlayerActions test = mock(PlayerActions.class);
        Vector2 vector = new Vector2();
        ArgumentCaptor<Vector2> testValue = ArgumentCaptor.forClass(Vector2.class);
        doNothing().when(test).walk(testValue.capture());
        test.walk(vector);
        assertEquals(vector,testValue.getValue());
    }

}
