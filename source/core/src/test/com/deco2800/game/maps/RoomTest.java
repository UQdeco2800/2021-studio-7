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
public class RoomTest {

    private final Character[][] hallwayTileGrid = {
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
    private final Character[][] hallwayEntityGrid = {
            {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
            {'.', '.', '.', '.', '.', '.', '.', '.', 'W'},
            {'.', '.', '.', '.', '.', '.', '.', '.', 'W'},
            {'.', '.', '.', '.', '.', '.', '.', '.', 'W'},
            {'.', '.', '.', '.', '.', '.', '.', '.', 'W'},
            {'.', '.', '.', '.', '.', '.', '.', '.', 'W'},
            {'.', '.', '.', '.', '.', '.', '.', '.', 'W'},
            {'.', '.', '.', '.', '.', '.', '.', '.', 'W'},
            {'.', '.', '.', '.', '.', '.', '.', '.', 'W'}
    };

    @Test
    void shouldReadRoom() throws NoSuchMethodException {
        Room roomA = createBaseRoom("bedroom");
        Room roomB = createStyledRoom("living");
        RoomWrapper wrapper = FileLoader
                .readClass(RoomWrapper.class, "maps/testing/Room.json");

        assertEquals(roomA, wrapper.roomA);
        assertEquals(roomB, wrapper.roomB);
    }

    @Test
    void shouldCreateHallwayInteriorFromHallway() throws NoSuchMethodException {
        Floor floor = new Floor();
        floor.setDefaultInteriorWall(new GridObject(
                ObjectFactory.class.getMethod("createWall", String[].class),
                new String[]{"images/objects/walls/3.png"}));
        Room room = createBaseRoom("hallway");
        room.create(floor);
        assertEquals(room.getTileMap().size, 0);
        assertEquals(room.getEntityMap().size, 1);
        assertNotNull(room.getEntityMap().get('W'));
        assertTrue(Arrays.deepEquals(room.getTileGrid(), hallwayTileGrid));
        assertTrue(Arrays.deepEquals(room.getEntityGrid(), hallwayEntityGrid));
    }

    @Test
    void shouldCreateRandomInteriorFromOther() {
        Floor floor = new Floor();
        Room room = createBaseRoom("bedroom");
        room.create(floor);
        assertNotNull(room.getTileMap());
        assertNotNull(room.getEntityMap());
        assertNotNull(room.getTileGrid());
        assertNotNull(room.getEntityGrid());
    }

    @Test
    void shouldGetValidSpawnLocationsFromLiving() throws NoSuchMethodException {
        Room room = createStyledRoom("living");
        List<GridPoint2> validSpawnLocations = room.getValidSpawnLocations();
        assertEquals(validSpawnLocations.size(), 26);
    }

    @Test
    void shouldGetNoValidSpawnLocationsFromOther() throws NoSuchMethodException {
        Room room = createStyledRoom("bedroom");
        List<GridPoint2> validSpawnLocations = room.getValidSpawnLocations();
        assertEquals(validSpawnLocations.size(), 0);
    }

    @Test
    void shouldGetAssetsWithExtension() throws NoSuchMethodException {
        Room room = createStyledRoom("bedroom");
        List<String> assetsWithExtension = room.getAssets(".png");
        assertArrayEquals(assetsWithExtension.toArray(),
                new String[]{"images/tiles/iso/iso_wall_1_left.png", "images/objects/walls/3.png"});
    }

    Room createBaseRoom(String type) {
        GridPoint2 offset = new GridPoint2(0, 0);
        GridPoint2 dimensions = new GridPoint2(9, 9);
        return new Room(type, offset, dimensions);
    }

    Room createStyledRoom(String type) throws NoSuchMethodException {
        GridPoint2 offset = new GridPoint2(14, 0);
        GridPoint2 dimensions = new GridPoint2(10, 4);

        ObjectMap<Character, GridObject> tileMap = new ObjectMap<>();
        tileMap.put('a', new GridObject(
                TerrainFactory.class.getMethod("createBaseTile", String[].class),
                new String[]{"images/tiles/iso/iso_wall_1_left.png"}));
        ObjectMap<Character, GridObject> entityMap = new ObjectMap<>();
        entityMap.put('W', new GridObject(
                ObjectFactory.class.getMethod("createWall", String[].class),
                new String[]{"images/objects/walls/3.png"}));
        entityMap.put('B', new GridObject(
                ObjectFactory.class.getMethod("createBed", String[].class),
                new String[]{"images/objects/bed/bed_animation.atlas"}));
        Character[][] tileGrid = {
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
        Character[][] entityGrid = {
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

        return new Room(type, offset, dimensions,
                new Interior(tileMap, entityMap, tileGrid, entityGrid, dimensions));
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
}
