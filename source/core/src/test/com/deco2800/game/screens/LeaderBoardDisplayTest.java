package com.deco2800.game.screens.endgame;
import com.deco2800.game.GdxGame;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.screens.leaderboardscreen.LeaderBoardDisplay;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import java.util.TreeMap;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(GameExtension.class)
public class LeaderBoardDisplayTest {
    public GdxGame game;

    @Test
    void createTest() {
        LeaderBoardDisplay test1 = mock(LeaderBoardDisplay.class);
        doNothing().when(test1).create();
        test1.create();
        verify(test1, times(1)).create();
    }

    @Test
    void updateTest() {
        LeaderBoardDisplay test2 = mock(LeaderBoardDisplay.class);
        doNothing().when(test2).update();
        test2.update();
        test2.update();
        verify(test2, times(2)).update();
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

    @Test
    void valueSortTest() {
        LeaderBoardDisplay test = new LeaderBoardDisplay(game);
        TreeMap<Integer, Integer> map =new TreeMap<>();
        map.put(2,3);
        assertEquals(null,test.valueSort(map).get(2));
    }
}
