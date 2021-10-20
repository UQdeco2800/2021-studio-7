package com.deco2800.game.entities.components.object;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

<<<<<<< HEAD
@ExtendWith(GameExtension.class)
=======
>>>>>>> 273cd24ae849b8789f03336ee5641ddb0c42d668
class CatActionsTest {

    @Test
    void create(){
        CatActions test1 = mock(CatActions.class);
        doNothing().when(test1).create();
        test1.create();
        verify(test1, times(1)).create();
    }


}