package com.deco2800.game.maps;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.entities.factories.ObjectFactory;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.maps.terrain.TerrainFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
class InteriorTest {

    @Test
    void shouldReadInterior() {
        Interior interior1 = createBaseInterior();
        Interior interior2 = FileLoader
                .readClass(Interior.class, "maps/testing/interior.json");

        assertEquals(interior1, interior2);
    }

    Interior createBaseInterior() {
        return new Interior(tileMap, entityMap, tileGrid, entityGrid, dimensions);
    }

    static final GridPoint2 dimensions = new GridPoint2(6, 6);
    static final Character[][] tileGrid = {
            {'.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', 'a', '.', '.'},
            {'.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.'},
    };
    static final Character[][] entityGrid = {
            {'W', 'W', 'W', 'W', 'W', 'W'},
            {'.', 's', '.', '.', '.', 'W'},
            {'.', '.', '.', '.', '.', 'W'},
            {'.', '.', '.', '.', '.', 'W'},
            {'.', '.', '.', '.', '.', 'W'},
            {'.', '.', '.', '.', '.', 'W'},
    };
    static final ObjectMap<Character, GridObject> tileMap = new ObjectMap<>();
    static final ObjectMap<Character, GridObject> entityMap = new ObjectMap<>();
    static {
        try {
            tileMap.put('a', new GridObject(
                    TerrainFactory.class.getMethod("createBaseTile", String[].class),
                    new String[]{"images/tiles/iso/iso_wall_1_left.png"}));
            entityMap.put('W', new GridObject(
                    ObjectFactory.class.getMethod("createWall", String[].class),
                    new String[]{"images/objects/walls/3.png"}));
            entityMap.put('s', new GridObject(
                    ObjectFactory.class.getMethod("createWall", String[].class),
                    new String[]{"images/objects/furniture/sink_southeast.png"}));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
