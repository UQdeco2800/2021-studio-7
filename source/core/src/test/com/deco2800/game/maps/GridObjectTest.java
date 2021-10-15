package com.deco2800.game.maps;

import com.deco2800.game.entities.factories.ObjectFactory;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
public class GridObjectTest {

    @Test
    void shouldReturnAssetsWithExtension() throws NoSuchMethodException {
        GridObject gridObject;
        gridObject = new GridObject(ObjectFactory.class.getMethod("createWall", String[].class),
                new String[]{"test.png", "test.atlas", "test1.atlas"});

        assertEquals(gridObject.getAssets(".png").get(0), "test.png");
        assertEquals(gridObject.getAssets(".atlas").get(0), "test.atlas");
        assertEquals(gridObject.getAssets(".atlas").get(1), "test1.atlas");
    }
}
