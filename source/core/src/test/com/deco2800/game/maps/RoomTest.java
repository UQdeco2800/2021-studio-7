package com.deco2800.game.maps;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.entities.factories.ObjectFactory;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.maps.terrain.TerrainFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class RoomTest {

    @Test
    void shouldReadRoom() {
        Room roomA = createBaseRoom("bedroom");
        Room roomB = createStyledRoom("living");
        RoomWrapper wrapper = FileLoader
                .readClass(RoomWrapper.class, "maps/testing/room.json");
        assertEquals(roomA, wrapper.roomA);
        assertEquals(roomB, wrapper.roomB);
    }

    /*
    @Test
    void shouldCreateHallwayInteriorFromHallway() throws NoSuchMethodException {
        Floor floor = new Floor();
        floor.setDefaultInteriorWall(new GridObject(
                ObjectFactory.class.getMethod("createWall", String[].class),
                new String[]{"images/objects/walls/3.png"}));
        Room room = createBaseRoom("hallway");
        room.setFloor(floor);
        room.create();
        assertEquals(0, room.getTileMap().size);
        assertEquals(1, room.getEntityMap().size);
        assertNotNull(room.getEntityMap().get('W'));
        assertTrue(Arrays.deepEquals(room.getTileGrid(), hallwayTileGrid));
        assertTrue(Arrays.deepEquals(room.getEntityGrid(), hallwayEntityGrid));
    }*/

    /*
    @Test
    void shouldCreateRandomInteriorFromOther() {
        Floor floor = new Floor();
        Room room = createBaseRoom("bedroom");
        room.setFloor(floor);
        room.create();
        assertNotNull(room.getTileMap());
        assertNotNull(room.getEntityMap());
        assertNotNull(room.getTileGrid());
        assertNotNull(room.getEntityGrid());
    }*/

    @Test
    void shouldGetValidSpawnLocationsFromLiving() {
        Room room = createStyledRoom("living");
        List<GridPoint2> validSpawnLocations = room.getValidSpawnLocations();
        assertEquals(26, validSpawnLocations.size());
    }

    @Test
    void shouldGetNoValidSpawnLocationsFromOther() {
        Room room = createStyledRoom("bedroom");
        List<GridPoint2> validSpawnLocations = room.getValidSpawnLocations();
        assertEquals(0, validSpawnLocations.size());
    }

    @Test
    void shouldGetAssetsWithExtension() {
        Room room = createStyledRoom("bedroom");
        List<String> assetsWithExtension = room.getAssets(".png");
        assertArrayEquals(new String[]{"images/tiles/iso/iso_wall_1_left.png", "images/objects" +
                "/walls/3.png"}, assetsWithExtension.toArray());
    }

    Room createBaseRoom(String type) {
        return new Room(type, offset, baseDimensions);
    }

    Room createStyledRoom(String type) {
        return new Room(type, offset, styledDimensions,
                new Interior(styledTileMap, styledEntityMap, styledTileGrid, styledEntityGrid, styledDimensions));
    }

    static class RoomWrapper implements Json.Serializable {
        Room roomA = new Room();
        Room roomB = new Room();

        @Override
        public void write(Json json) {
            // No testing purpose
        }

        @Override
        public void read(Json json, JsonValue jsonData) {
            jsonData = jsonData.child();
            roomA.read(json, jsonData);
            jsonData = jsonData.next();
            roomB.read(json, jsonData);
        }
    }

    static final GridPoint2 offset = new GridPoint2(0, 0);
    static final GridPoint2 baseDimensions = new GridPoint2(9, 9);
    static final GridPoint2 styledDimensions = new GridPoint2(10, 4);
    static final Character[][] hallwayTileGrid = {
            {'.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.'}
    };
    static final Character[][] hallwayEntityGrid = {
            {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
            {'.', '.', '.', '.', '.', '.', '.', '.', 'W'},
            {'.', '.', '.', '.', '.', '.', '.', '.', 'W'},
            {'.', '.', '.', '.', '.', '.', '.', '.', 'W'},
            {'.', '.', '.', '.', '.', '.', '.', '.', 'W'},
            {'.', '.', '.', '.', '.', '.', '.', '.', 'W'},
            {'.', '.', '.', '.', '.', '.', '.', '.', 'W'},
            {'.', '.', '.', '.', '.', '.', '.', '.', 'W'},
            {'.', '.', '.', '.', '.', '.', '.', '.', 'W'},
    };
    static final Character[][] styledTileGrid = {
            {'.', '.', '.', '.'},
            {'.', '.', '.', '.'},
            {'.', '.', '.', '.'},
            {'.', '.', '.', '.'},
            {'.', '.', '.', '.'},
            {'.', '.', '.', '.'},
            {'.', '.', '.', '.'},
            {'.', '.', '.', '.'},
            {'.', '.', '.', '.'},
            {'.', '.', '.', '.'}
    };
    static final Character[][] styledEntityGrid = {
            {'W', 'W', 'W', 'W'},
            {'.', 'B', '.', 'W'},
            {'.', '.', '.', 'W'},
            {'.', '.', '.', 'W'},
            {'.', '.', '.', 'W'},
            {'.', '.', '.', 'W'},
            {'.', '.', '.', 'W'},
            {'.', '.', '.', 'W'},
            {'.', '.', '.', 'W'},
            {'.', '.', '.', 'W'}
    };
    static final ObjectMap<Character, GridObject> styledTileMap = new ObjectMap<>();
    static final ObjectMap<Character, GridObject> styledEntityMap = new ObjectMap<>();
    static {
        try {
            styledTileMap.put('a', new GridObject(
                    TerrainFactory.class.getMethod("createBaseTile", String[].class),
                    new String[]{"images/tiles/iso/iso_wall_1_left.png"}));
            styledEntityMap.put('W', new GridObject(
                    ObjectFactory.class.getMethod("createWall", String[].class),
                    new String[]{"images/objects/walls/3.png"}));
            styledEntityMap.put('B', new GridObject(
                    ObjectFactory.class.getMethod("createBed", String[].class),
                    new String[]{"images/objects/bed/bed_animation.atlas"}));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
