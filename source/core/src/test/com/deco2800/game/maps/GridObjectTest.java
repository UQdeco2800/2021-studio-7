package com.deco2800.game.maps;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.deco2800.game.entities.factories.ObjectFactory;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.maps.terrain.TerrainFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
public class GridObjectTest {

    @Test
    void shouldReadGridObject() throws NoSuchMethodException {
        GridObject gridObject = FileLoader
                .readClass(GridObjectWrapper.class, "maps/testing/GridObject.json").gridObject;
        assertNotNull(gridObject);
        assertEquals(gridObject.getMethod(), TerrainFactory.class.getMethod("createBaseTile", String[].class));
        assertArrayEquals(gridObject.getAssets(), new String[]{"images/tiles/iso/iso_wall_1_left.png"});
    }

    @Test
    void shouldGetAssetsWithExtension() throws NoSuchMethodException {
        GridObject gridObject = new GridObject(
                ObjectFactory.class.getMethod("createWall", String[].class),
                new String[]{"test.png", "test.atlas", "test1.atlas"});

        assertArrayEquals(gridObject.getAssets(".png").toArray(), new String[]{"test.png"});
        assertArrayEquals(gridObject.getAssets(".atlas").toArray(), new String[]{"test.atlas", "test1.atlas"});
    }

    static class GridObjectWrapper implements Json.Serializable {
        GridObject gridObject = new GridObject();

        @Override
        public void write(Json json) {
            // No testing purpose
        }

        @Override
        public void read(Json json, JsonValue jsonData) {
            jsonData = jsonData.child();
            gridObject.read(json, jsonData);
        }
    }
}
