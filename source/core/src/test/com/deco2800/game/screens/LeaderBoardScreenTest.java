package com.deco2800.game.screens.endgame;

import com.deco2800.game.GdxGame;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.screens.leaderboard.LeaderBoardDisplay;
import com.deco2800.game.screens.leaderboard.LeaderBoardScreen;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class LeaderBoardScreenTest {
    public GdxGame game;

    @Test
    void renderTest() {
        LeaderBoardScreen screen = mock(LeaderBoardScreen.class);
        ArgumentCaptor<Float> testValue = ArgumentCaptor.forClass(float.class);
        doNothing().when(screen).render(testValue.capture());
        screen.render((float)0.455);
        assertEquals((float)0.455,testValue.getValue());
    }

    @Test
    void reSize() {
        LeaderBoardScreen test = mock(LeaderBoardScreen.class);
        ArgumentCaptor value = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(test).resize(any(Integer.class),(int)value.capture());
        test.resize(1,5);
        assertEquals(5,(int)value.getValue());
    }

    @Test
    void disposeTest() {
        LeaderBoardDisplay test3 = mock(LeaderBoardDisplay.class);
        doNothing().when(test3).dispose();
        test3.dispose();
        test3.dispose();
        test3.dispose();
        verify(test3, times(3)).dispose();
    }
}
