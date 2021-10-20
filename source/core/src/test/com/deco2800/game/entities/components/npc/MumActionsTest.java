package com.deco2800.game.entities.components.npc;

import com.deco2800.game.entities.Entity;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MumActionsTest {

    @Test
    void createTest() {
        MumActions test = mock(MumActions.class);
        doNothing().when(test).create();
        test.create();
        verify(test,times(1)).create();
    }

    @Test
    void onCollisionStartTest() {
        ArgumentCaptor<Entity> target = ArgumentCaptor.forClass(Entity.class);
        MumActions entity = mock(MumActions.class);
        Entity test = new Entity();
        doNothing().when(entity).onCollisionStart(target.capture());
        entity.onCollisionStart(test);
        assertEquals(test, target.getValue());
    }
 }
