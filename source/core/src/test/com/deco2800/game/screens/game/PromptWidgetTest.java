package com.deco2800.game.screens.game;

import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.screens.game.PromptWidget;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(GameExtension.class)
class PromptWidgetTest {

    @Test
    void create() {
        PromptWidget textDisplay = mock(PromptWidget.class);
        doNothing().when(textDisplay).create();
        textDisplay.create();
        verify(textDisplay, times(1)).create();
    }

    @Test
    void display() {
        PromptWidget textDisplay = mock(PromptWidget.class);
        textDisplay.display("Test");
        verify(textDisplay, times(1)).display("Test");
    }

    @Test
    void update() {
        PromptWidget textDisplay = mock(PromptWidget.class);
        textDisplay.update();
        textDisplay.update();
        textDisplay.update();
        verify(textDisplay, times(3)).update();
    }

    @Test
    void dispose() {
        PromptWidget textDisplay = mock(PromptWidget.class);
        textDisplay.dispose();
        textDisplay.dispose();
        verify(textDisplay, times(2)).dispose();
    }
}
