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
public class InteriorTest {

    @Test
    void shouldDeserializeInteriorOnRead() throws NoSuchMethodException {
        Interior interior1 = createTestInterior();
        Interior interior2 = FileLoader
                .readClass(Interior.class, "maps/testing/Interior.json");

        assertEquals(interior1, interior2);
    }

    Interior createTestInterior() throws NoSuchMethodException {

        ObjectMap<Character, GridObject> tileMap = new ObjectMap<>();
        tileMap.put('a', new GridObject(
                TerrainFactory.class.getMethod("createBaseTile", String[].class),
                new String[]{"images/tiles/iso/iso_wall_1_left.png"}));

        ObjectMap<Character, GridObject> entityMap = new ObjectMap<>();
        entityMap.put('W', new GridObject(
                ObjectFactory.class.getMethod("createWall", String[].class),
                new String[]{"images/objects/walls/3.png"}));
        entityMap.put('s', new GridObject(
                ObjectFactory.class.getMethod("createWall", String[].class),
                new String[]{"images/objects/furniture/sink_southeast.png"}));

        Character[][] tileGrid = {
                {'.', '.', '.', '.', '.', '.'},
                {'.', '.', '.', 'a', '.', '.'},
                {'.', '.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.', '.'},
        };

        Character[][] entityGrid = {
                {'W', 'W', 'W', 'W', 'W', 'W'},
                {'.', 's', '.', '.', '.', 'W'},
                {'.', '.', '.', '.', '.', 'W'},
                {'.', '.', '.', '.', '.', 'W'},
                {'.', '.', '.', '.', '.', 'W'},
                {'.', '.', '.', '.', '.', 'W'},
        };

        GridPoint2 dimensions = new GridPoint2(6, 6);

        return new Interior(tileMap, entityMap, tileGrid, entityGrid, dimensions);
    }
}
