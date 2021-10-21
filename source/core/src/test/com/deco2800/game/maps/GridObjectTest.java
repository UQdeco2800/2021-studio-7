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
class GridObjectTest {

    @Test
    void shouldReadGridObject() {
        GridObject gridObject1 = createBaseGridObject();
        GridObject gridObject2 = FileLoader
                .readClass(GridObjectWrapper.class, "maps/testing/grid_object.json").gridObject;
        assertEquals(gridObject1, gridObject2);
    }

    @Test
    void shouldGetAssetsWithExtension() {
        GridObject gridObject = createBaseGridObject();
        assertArrayEquals(new String[]{"test.png"}, gridObject.getAssets(".png").toArray());
        assertArrayEquals(new String[]{"test.atlas", "test1.atlas"}, gridObject.getAssets(".atlas").toArray());
    }

    GridObject createBaseGridObject() {
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

    static Method method;
    static final String[] assets = new String[]{"test.png", "test.atlas", "test1.atlas"};
    static {
        try {
            method = TerrainFactory.class.getMethod("createBaseTile", String[].class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
