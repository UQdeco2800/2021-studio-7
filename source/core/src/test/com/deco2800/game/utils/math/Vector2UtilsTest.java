package com.deco2800.game.utils.math;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.files.FileLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(GameExtension.class)
class Vector2UtilsTest {

    @Test
    void shouldDeserializeGridPoint2OnRead() {
        Vector2 vector2 = FileLoader
                .readClass(Vector2Wrapper.class, "maps/testing/vector_2.json").vector2;
        assertNotNull(vector2);
        assertEquals(1, vector2.x);
        assertEquals(2, vector2.y);
    }

    static class Vector2Wrapper implements Json.Serializable {
        Vector2 vector2 = null;

        @Override
        public void write(Json json) {
            // No testing purpose
        }

        @Override
        public void read(Json json, JsonValue jsonData) {
            jsonData = jsonData.child();
            vector2 = Vector2Utils.read(jsonData);
        }
    }
}
