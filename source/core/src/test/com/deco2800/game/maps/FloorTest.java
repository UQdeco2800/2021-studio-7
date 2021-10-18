package com.deco2800.game.maps;

import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.files.FileLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
public class FloorTest {

    @Test
    void shouldReadFloor() {
        Floor floor1 = createBaseFloor();
        Floor floor2 = FileLoader
                .readClass(Floor.class, "maps/testing/floor.json");

        assertEquals(floor1, floor2);
    }

    Floor createBaseFloor() {
        return null;
    }
}
