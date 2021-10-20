package com.deco2800.game.maps;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.entities.factories.ObjectFactory;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.maps.terrain.TerrainFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
class FloorTest {

    @Test
    void shouldReadFloor() {
        Floor floor1 = createBaseFloor();
        Floor floor2 = FileLoader
                .readClass(Floor.class, "maps/testing/floor.json");
        assertEquals(floor1, floor2);
    }

    @Test
    void shouldGetAssetsWithExtension() {
        Floor floor = createBaseFloor();
        String[] assetsWithExtension = floor.getAssets(".png");
        assertArrayEquals(new String[]{"images/tiles/iso/iso_floor_1.png", "images/objects/walls/1.png",
                "images/tiles/iso/iso_wall_1_left.png", "images/objects/walls/wall.png",
                "images/tiles/iso/iso_grass_1.png", "images/objects/walls/3.png",
                "images/tiles/iso/iso_grass_2.png", "images/objects/walls/4.png"}, assetsWithExtension);
        assetsWithExtension = floor.getAssets(".atlas");
        assertArrayEquals(new String[]{"images/objects/door/door_animationL.atlas"}, assetsWithExtension);
    }

    Floor createBaseFloor() {
        return new Floor(defaultInteriorTile, defaultInteriorWall,
                tileMap, entityMap, roomMap, floorGrid, floorDimensions);
    }

    static final Character[][] floorGrid = {
            {'.', 'B', 'B', 'B', '-', 'B', 'B', '.', 'A', 'A', 'A', 'A', 'A', 'A'},
            {'.', 'B', 'B', 'B', 'B', 'B', 'B', '.', 'A', 'A', 'A', 'A', 'A', 'A'},
            {'.', 'B', 'B', 'B', 'B', 'B', 'B', '.', 'A', 'A', 'A', 'A', 'A', 'A'},
            {'.', 'B', 'B', 'B', 'B', 'B', 'B', '.', 'A', 'A', 'A', 'A', 'A', 'A'},
            {'.', 'B', 'B', 'B', 'B', 'B', 'B', '.', 'A', 'A', 'A', 'A', 'A', 'A'},
            {'.', 'B', 'B', 'B', 'B', 'B', 'B', '.', 'A', 'A', 'A', 'A', 'A', 'A'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}
    };
    static final Character[][] tileGrid = {
            {'.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.'},
    };
    static final Character[][] entityGrid = {
            {'W', 'W', 'W', 'W', 'W', 'W'},
            {'.', '.', '.', '.', '.', 'W'},
            {'.', '.', '.', '.', '.', 'W'},
            {'.', '.', '.', '.', '.', 'W'},
            {'.', '.', '.', '.', '.', 'W'},
            {'.', '.', '.', '.', '.', 'W'}
    };
    static GridObject defaultInteriorTile = new GridObject();
    static GridObject defaultInteriorWall = new GridObject();
    static final ObjectMap<Character, GridObject> tileMap = new ObjectMap<>();
    static final ObjectMap<Character, GridObject> entityMap = new ObjectMap<>();
    static final GridPoint2 offsetA = new GridPoint2(0, 8);
    static final GridPoint2 offsetB = new GridPoint2(0, 1);
    static final GridPoint2 roomDimensions = new GridPoint2(6, 6);
    static final GridPoint2 floorDimensions = new GridPoint2(7, 14);
    static final ObjectMap<Character, GridObject> tileMapA = new ObjectMap<>();
    static final ObjectMap<Character, GridObject> tileMapB = new ObjectMap<>();
    static final ObjectMap<Character, GridObject> entityMapA = new ObjectMap<>();
    static final ObjectMap<Character, GridObject> entityMapB = new ObjectMap<>();
    static ObjectMap<Character, Room> roomMap = new ObjectMap<>();
    static {
        try {
            defaultInteriorTile = new GridObject(
                    TerrainFactory.class.getMethod("createBaseTile", String[].class),
                    new String[]{"images/tiles/iso/iso_floor_1.png"}
            );
            defaultInteriorWall = new GridObject(
                    ObjectFactory.class.getMethod("createWall", String[].class),
                    new String[]{"images/objects/walls/1.png"}
            );
            tileMap.put('_', new GridObject(
                    TerrainFactory.class.getMethod("createBaseTile", String[].class),
                    new String[]{"images/tiles/iso/iso_wall_1_left.png"}
            ));
            entityMap.put('.', new GridObject(
                    ObjectFactory.class.getMethod("createWall", String[].class),
                    new String[]{"images/objects/walls/wall.png"}
            ));
            entityMap.put('-', new GridObject(
                    ObjectFactory.class.getMethod("createDoor", String[].class),
                    new String[]{"images/objects/door/door_animationL.atlas"}
            ));
            tileMapA.put('a', new GridObject(
                    TerrainFactory.class.getMethod("createBaseTile", String[].class),
                    new String[]{"images/tiles/iso/iso_grass_1.png"}
            ));
            entityMapA.put('W', new GridObject(
                    ObjectFactory.class.getMethod("createWall", String[].class),
                    new String[]{"images/objects/walls/3.png"}
            ));
            tileMapB.put('a', new GridObject(
                    TerrainFactory.class.getMethod("createBaseTile", String[].class),
                    new String[]{"images/tiles/iso/iso_grass_2.png"}
            ));
            entityMapB.put('W', new GridObject(
                    ObjectFactory.class.getMethod("createWall", String[].class),
                    new String[]{"images/objects/walls/4.png"}
            ));
            roomMap.put('A', new Room("bedroom", offsetA, roomDimensions,
                    new Interior(tileMapA, entityMapA, tileGrid, entityGrid, roomDimensions)));
            roomMap.put('B', new Room("bathroom", offsetB, roomDimensions,
                    new Interior(tileMapB, entityMapB, tileGrid, entityGrid, roomDimensions)));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
