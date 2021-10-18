package com.deco2800.game.utils.math;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.files.FileLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(GameExtension.class)
public class GridPoint2UtilsTest {

    @Test
    void shouldDeserializeGridPoint2OnRead() {
        GridPoint2 gridPoint2 = FileLoader
                .readClass(GridPoint2Wrapper.class, "maps/testing/GridPoint2.json").gridPoint2;
        assertNotNull(gridPoint2);
        assertEquals(gridPoint2.x, 1);
        assertEquals(gridPoint2.y, 2);
    }

    static class GridPoint2Wrapper implements Json.Serializable {
        GridPoint2 gridPoint2 = null;

        @Override
        public void write(Json json) {
            // No testing purpose
        }

        @Override
        public void read(Json json, JsonValue jsonData) {
            jsonData = jsonData.child();
            gridPoint2 = GridPoint2Utils.read(jsonData);
        }
    }
}
