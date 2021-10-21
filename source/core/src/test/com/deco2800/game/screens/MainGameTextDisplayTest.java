package com.deco2800.game.screens;

import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.screens.maingame.MainGameTextDisplay;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(GameExtension.class)
class MainGameTextDisplayTest {

    @Test
    void create() {
        MainGameTextDisplay textDisplay = mock(MainGameTextDisplay.class);
        doNothing().when(textDisplay).create();
        textDisplay.create();
        verify(textDisplay, times(1)).create();
    }

    @Test
    void display() {
        MainGameTextDisplay textDisplay = mock(MainGameTextDisplay.class);
        textDisplay.display("Test");
        verify(textDisplay, times(1)).display("Test");
    }

    @Test
    void update() {
        MainGameTextDisplay textDisplay = mock(MainGameTextDisplay.class);
        textDisplay.update();
        textDisplay.update();
        textDisplay.update();
        verify(textDisplay, times(3)).update();
    }

    @Test
    void dispose() {
        MainGameTextDisplay textDisplay = mock(MainGameTextDisplay.class);
        textDisplay.dispose();
        textDisplay.dispose();
        verify(textDisplay, times(2)).dispose();
    }
}
