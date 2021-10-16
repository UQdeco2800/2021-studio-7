package com.deco2800.game.utils.math;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.files.FileLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(GameExtension.class)
public class Vector2UtilsTest {

    @Test
    void shouldDeserializeGridPoint2OnRead() {
        Vector2Wrapper wrapper = FileLoader.readClass(Vector2Wrapper.class, "maps/testing/Vector2.json");
        assertNotNull(wrapper.vector2);
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
