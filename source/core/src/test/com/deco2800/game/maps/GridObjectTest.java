package com.deco2800.game.maps;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.maps.terrain.TerrainFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
public class GridObjectTest {

    @Test
    void shouldReadGridObject() throws NoSuchMethodException {
        GridObject gridObject1 = createBaseGridObject();
        GridObject gridObject2 = FileLoader
                .readClass(GridObjectWrapper.class, "maps/testing/grid_object.json").gridObject;
        assertEquals(gridObject1, gridObject2);
    }

    @Test
    void shouldGetAssetsWithExtension() throws NoSuchMethodException {
        GridObject gridObject = createBaseGridObject();
        assertArrayEquals(gridObject.getAssets(".png").toArray(), new String[]{"test.png"});
        assertArrayEquals(gridObject.getAssets(".atlas").toArray(), new String[]{"test.atlas", "test1.atlas"});
    }

    GridObject createBaseGridObject() throws NoSuchMethodException {
        Method method = TerrainFactory.class.getMethod("createBaseTile", String[].class);
        String[] assets = new String[]{"test.png", "test.atlas", "test1.atlas"};
        return new GridObject(method, assets);
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
