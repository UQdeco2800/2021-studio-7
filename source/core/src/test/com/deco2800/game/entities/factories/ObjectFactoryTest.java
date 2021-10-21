package com.deco2800.game.entities.factories;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class ObjectFactoryTest {


    /**
     * Checks if an entity can successfully be created. Since bed is an entity
     * It should pass the test
     */
    @Test
    void shouldCreateEntity() {
        EntityService entityService = new EntityService();
        Entity entity = spy(Entity.class);
        entityService.register(entity);
        verify(entity).create();
    }

    /**
     * Checks if an entity can successfully be disposed. Since bed is an entity
     * It should pass the test
     */
    @Test
    void shouldDisposeEntities() {
        EntityService entityService = new EntityService();
        Entity entity = mock(Entity.class);
        entityService.register(entity);
        entityService.dispose();
        verify(entity).dispose();
    }
}
