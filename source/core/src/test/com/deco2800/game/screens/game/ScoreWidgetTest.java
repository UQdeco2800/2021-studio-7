package com.deco2800.game.screens.game;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.screens.game.ScoreWidget;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(GameExtension.class)
class ScoreWidgetTest {

    @Test
    void createTest() {
        ScoreWidget test1 = mock(ScoreWidget.class);
        doNothing().when(test1).create();
        test1.create();
        verify(test1, times(1)).create();
    }

    @Test
    void updatePlayerHealthUITest() {
        ScoreWidget test3 = mock(ScoreWidget.class);
        doNothing().when(test3).updatePlayerHealthUI();;
        test3.updatePlayerHealthUI();
        test3.updatePlayerHealthUI();
        test3.updatePlayerHealthUI();
        verify(test3,times(3)).updatePlayerHealthUI();
    }

    @Test
    void getScoreBiggerThan0Test() {
        ScoreWidget test4 = new ScoreWidget(0,2);
        assertEquals(1,test4.getscore());
    }

    @Test
    void getScoreEqual0Test() {
        ScoreWidget test4 = new ScoreWidget(0,1);
        assertEquals(0,test4.getscore());
    }

    @Test
    void getScoreSmallerThan0Test() {
        ScoreWidget test4 = new ScoreWidget(0,0);
        assertEquals(-1,test4.getscore());
    }

    @Test
    void disposeTest() {
        ScoreWidget test5 = mock(ScoreWidget.class);
        doNothing().when(test5).dispose();
        test5.dispose();
        test5.dispose();
        test5.dispose();
        verify(test5,times(3)).dispose();
    }

    @Test
    void countDownTest() {
        ScoreWidget test6 = mock(ScoreWidget.class);
        doNothing().when(test6).countDown();
        test6.countDown();
        test6.countDown();
        test6.countDown();
        verify(test6,times(3)).countDown();
    }
}
