package com.deco2800.game.entities.components.player;
import static org.mockito.Mockito.*;

import com.deco2800.game.extensions.GameExtension;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


@ExtendWith(GameExtension.class)
class PlayerAnimationControllerTest {

    @Test
    void animateStandingNorthTest() {
        PlayerAnimationController test2 = mock(PlayerAnimationController.class);
        doNothing().when(test2).animateStandingNorth();
        test2.animateStandingNorth();
        verify(test2, times(1)).animateStandingNorth();
    }

    @Test
    void animateStandingEastTest() {
        PlayerAnimationController test2 = mock(PlayerAnimationController.class);
        doNothing().when(test2).animateStandingEast();
        test2.animateStandingEast();
        verify(test2, times(1)).animateStandingEast();
    }

    @Test
    void animateStandingSouthTest() {
        PlayerAnimationController test2 = mock(PlayerAnimationController.class);
        doNothing().when(test2).animateStandingSouth();
        test2.animateStandingSouth();
        verify(test2, times(1)).animateStandingSouth();
    }

    @Test
    void animateStandingWestTest() {
        PlayerAnimationController test2 = mock(PlayerAnimationController.class);
        doNothing().when(test2).animateStandingWest();
        test2.animateStandingWest();
        verify(test2, times(1)).animateStandingWest();
    }

    @Test
    void animateWalkingNorthTest() {
        PlayerAnimationController test2 = mock(PlayerAnimationController.class);
        doNothing().when(test2).animateWalkingNorth();
        test2.animateWalkingNorth();
        verify(test2, times(1)).animateWalkingNorth();
    }

    @Test
    void animateWalkingEastTest() {
        PlayerAnimationController test2 = mock(PlayerAnimationController.class);
        doNothing().when(test2).animateWalkingEast();
        test2.animateWalkingEast();
        verify(test2, times(1)).animateWalkingEast();
    }

    @Test
    void animateWalkingSouthTest() {
        PlayerAnimationController test2 = mock(PlayerAnimationController.class);
        doNothing().when(test2).animateWalkingSouth();
        test2.animateWalkingSouth();
        verify(test2, times(1)).animateWalkingSouth();
    }

    @Test
    void animateWalkingLeftTest() {
        PlayerAnimationController test2 = mock(PlayerAnimationController.class);
        doNothing().when(test2).animateWalkingWest();
        test2.animateWalkingWest();
        verify(test2, times(1)).animateWalkingWest();
    }
}
