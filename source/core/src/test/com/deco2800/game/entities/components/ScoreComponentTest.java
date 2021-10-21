package com.deco2800.game.entities.components;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class ScoreComponentTest {

    @Test
    void createTest() {
        ScoreComponent test1 = mock(ScoreComponent.class);
        doNothing().when(test1).create();
        test1.create();
        verify(test1, times(1)).create();
    }

    @Test
    void getScoreTest() {
        ScoreComponent demo = new ScoreComponent(5);
        assertEquals(5,demo.getScore());
    }

    @Test
    void changeScoreTest() {
        ScoreComponent test2 = mock(ScoreComponent.class);
        ArgumentCaptor valueCapture = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(test2).changeScore((Integer) valueCapture.capture());
        test2.changeScore(6);
        assertEquals(6,valueCapture.getValue());
    }

    @Test
    void writeScoreToLeaderBoardTest() {
        ScoreComponent test3 = mock(ScoreComponent.class);
        doNothing().when(test3).writeScoreToLeaderBoard();
        test3.writeScoreToLeaderBoard();
        test3.writeScoreToLeaderBoard();
        verify(test3, times(2)).writeScoreToLeaderBoard();
    }

}
