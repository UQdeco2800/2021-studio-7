package com.deco2800.game.screens.endgame;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.screens.maingame.MainGameScoreDisplay;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(GameExtension.class)
class MainGameScoreDisplayTest {

    @Test
    void createTest() {
        MainGameScoreDisplay test1 = mock(MainGameScoreDisplay.class);
        doNothing().when(test1).create();
        test1.create();
        verify(test1, times(1)).create();
    }

    @Test
    void addActorsTest() {
        MainGameScoreDisplay test2 = mock(MainGameScoreDisplay.class);
        doNothing().when(test2).addActors();
        test2.addActors();
        test2.addActors();
        verify(test2,times(2)).addActors();
    }

    @Test
    void updatePlayerHealthUITest() {
        MainGameScoreDisplay test3 = mock(MainGameScoreDisplay.class);
        doNothing().when(test3).updatePlayerHealthUI();;
        test3.updatePlayerHealthUI();
        test3.updatePlayerHealthUI();
        test3.updatePlayerHealthUI();
        verify(test3,times(3)).updatePlayerHealthUI();
    }

    @Test
    void getScoreBiggerThan0Test() {
        MainGameScoreDisplay test4 = new MainGameScoreDisplay(0,2);
        assertEquals(1,test4.getscore());
    }

    @Test
    void getScoreEqual0Test() {
        MainGameScoreDisplay test4 = new MainGameScoreDisplay(0,1);
        assertEquals(0,test4.getscore());
    }

    @Test
    void getScoreSmallerThan0Test() {
        MainGameScoreDisplay test4 = new MainGameScoreDisplay(0,0);
        assertEquals(-1,test4.getscore());
    }

    @Test
    void disposeTest() {
        MainGameScoreDisplay test5 = mock(MainGameScoreDisplay.class);
        doNothing().when(test5).dispose();
        test5.dispose();
        test5.dispose();
        test5.dispose();
        verify(test5,times(3)).dispose();
    }

    @Test
    void countDownTest() {
        MainGameScoreDisplay test6 = mock(MainGameScoreDisplay.class);
        doNothing().when(test6).countDown();
        test6.countDown();
        test6.countDown();
        test6.countDown();
        verify(test6,times(3)).countDown();
    }
}
