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
class ObjectDataTest {

    /*@Test
    void shouldReadGridObject() {
        ObjectData objectData1 = createBaseGridObject();
        ObjectData objectData2 = FileLoader
                .readClass(GridObjectWrapper.class, "maps/testing/grid_object.json").objectData;
        assertEquals(objectData1, objectData2);
    }*/

    ObjectData createBaseGridObject() {
        return new ObjectData(method, assets);
    }

    static class GridObjectWrapper implements Json.Serializable {
        ObjectData objectData = new ObjectData();

        @Override
        public void write(Json json) {
            // No testing purpose
        }

        @Override
        public void read(Json json, JsonValue jsonData) {
            jsonData = jsonData.child();
            objectData.read(json, jsonData);
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
