package com.deco2800.game.entities.components.object;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class CatActionsTest {

    @Test
    void create(){
        CatActions test1 = mock(CatActions.class);
        doNothing().when(test1).create();
        test1.create();
        verify(test1, times(1)).create();
    }


}