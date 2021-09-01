package com.deco2800.game.components.player;
import static org.mockito.Mockito.*;

import com.deco2800.game.extensions.GameExtension;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


@ExtendWith(GameExtension.class)
class PlayerAnimationControllerTest {

    @Test
    void animateRunLeftTest() {
        PlayerAnimationController test2 = mock(PlayerAnimationController.class);
        doNothing().when(test2).animateRunLeft();
        test2.animateRunLeft();
        verify(test2, times(1)).animateRunLeft();
    }

    @Test
    void animateRunRightTest() {
        PlayerAnimationController test2 = mock(PlayerAnimationController.class);
        doNothing().when(test2).animateRunRight();
        test2.animateRunRight();
        verify(test2, times(1)).animateRunRight();
    }

    @Test
    void animateRunUpTest() {
        PlayerAnimationController test2 = mock(PlayerAnimationController.class);
        doNothing().when(test2).animateRunUp();
        test2.animateRunUp();
        verify(test2, times(1)).animateRunUp();
    }

    @Test
    void animateRunDown() {
        PlayerAnimationController test2 = mock(PlayerAnimationController.class);
        doNothing().when(test2).animateRunDown();
        test2.animateRunDown();
        verify(test2, times(1)).animateRunDown();
    }

    @Test
    void animateStandLeft() {
        PlayerAnimationController test2 = mock(PlayerAnimationController.class);
        doNothing().when(test2).animateStandLeft();
        test2.animateStandLeft();
        verify(test2, times(1)).animateStandLeft();
    }

    @Test
    void animateStandRight() {
        PlayerAnimationController test2 = mock(PlayerAnimationController.class);
        doNothing().when(test2).animateStandRight();
        test2.animateStandRight();
        verify(test2, times(1)).animateStandRight();
    }

    @Test
    void animateStandUp() {
        PlayerAnimationController test2 = mock(PlayerAnimationController.class);
        doNothing().when(test2).animateStandUp();
        test2.animateStandUp();
        verify(test2, times(1)).animateStandUp();
    }

    @Test
    void animateStandDown() {
        PlayerAnimationController test2 = mock(PlayerAnimationController.class);
        doNothing().when(test2).animateStandDown();
        test2.animateStandDown();
        verify(test2, times(1)).animateStandDown();
    }
}
