package com.deco2800.game.maps;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Holds raw data for room object generation.
 * Contains functionality for randomising room interiors.
 */
public class Room implements Json.Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Room.class);
    // Defined from deserialization or constructor injection
    private String type;
    private GridPoint2 dimensions;
    private GridPoint2 offset;
    // Defined from deserialization, constructor injection or initialisation
    private ObjectMap<Character, ObjectDescription> tileMap;
    private ObjectMap<Character, ObjectDescription> entityMap;
    private Character[][] tileGrid;
    private Character[][] entityGrid;
    // Defined from initialisation
    private Floor floor;
    private Character key;

    public void initialise() {
        for (ObjectMap.Entry<Character, Room> entry : new ObjectMap.Entries<>(floor.getRoomMap())) {
            if (entry.value == this) {
                key = entry.key;
                break;
            }
        }

        if (type.equals("hallway")) {
            initialiseHallwayInterior();
        } else if (tileMap == null) {
            initialiseRandomInterior();
        }
    }

    /**
     * Creates a basic room interior with the default floor tile texture.
     */
    private void initialiseHallwayInterior() {
        tileMap = new ObjectMap<>();
        entityMap = new ObjectMap<>();
        entityMap.put('W', floor.getDefaultWall());
        tileGrid = new Character[dimensions.x + 1][dimensions.y + 1];
        entityGrid = new Character[dimensions.x + 1][dimensions.y + 1];
        Character roomKey = getKey();
        for (int x = 0; x < dimensions.x; x++) {
            for (int y = 0; y < dimensions.y; y++) {
                Character floorKey = floor.getFloorGrid()[x + offset.x][y + offset.y];
                if (floorKey.equals(roomKey)) {
                    tileGrid[x][y + 1] = '.';
                } else {
                    tileGrid[x][y + 1] = floorKey;
                }
                if (floorKey.equals(roomKey)) {
                    if (x == 0 || y == dimensions.y - 1) {
                        entityGrid[x][y + 1] = 'W';
                    } else {
                        entityGrid[x][y + 1] = '.';
                    }
                } else {
                    entityGrid[x][y + 1] = floorKey;
                }
            }
        }
        relaxFloorToRoom();
    }

    /**
     * Queries for a list of JSON files in a pre-defined directory. Selects one at random
     * and initialises the room interior plan.
     */
    private void initialiseRandomInterior() {
        List<FileHandle> fileHandles = FileLoader.getJsonFiles(Home.DIRECTORY.concat(type));

        Interior randomInterior;
        do {
            FileHandle fileHandle = fileHandles.get(RandomUtils.getSeed().nextInt(fileHandles.size()));

            randomInterior = FileLoader.readClass(Interior.class, fileHandle.path());
            randomInterior.setRoom(this);

            if (!randomInterior.calibrateDimensions(dimensions) || !randomInterior.calibrateInterior()) {
                randomInterior = null;
            }

            fileHandles.remove(fileHandle);
        } while (randomInterior == null && !fileHandles.isEmpty());

        if (randomInterior == null) {
            throw new NullPointerException("A valid room interior json file could not be loaded");
        }

        tileMap = randomInterior.getTileMap();
        entityMap = randomInterior.getEntityMap();
        tileGrid = randomInterior.getTileGrid();
        entityGrid = randomInterior.getEntityGrid();
        relaxFloorToRoom();
    }

    private void relaxFloorToRoom() {
        Character roomKey = getKey();
        for (int x = 0; x < dimensions.x; x++) {
            for (int y = 0; y < dimensions.y; y++) {
                GridPoint2 worldPos = new GridPoint2(x + offset.x, y + offset.y);
                Character floorKey = floor.getFloorGrid()[worldPos.x][worldPos.y];
                if (!floor.getRoomMap().containsKey(floorKey)) {
                    floor.getFloorGrid()[worldPos.x][worldPos.y] = roomKey;
                }
            }
        }
    }

    /**
     * Creates all tiles related to this room by invoking their creation method. If a tile
     * symbol is not defined on the grid, it is assumed that the default floor tile texture
     * is to be used.
     *
     * @param layer tile grid with this room's additions
     */
    public void createRoomTiles(TiledMapTileLayer layer) {
        for (int x = 0; x < dimensions.x; x++) {
            for (int y = 0; y < dimensions.y; y++) {
                GridPoint2 worldPos = new GridPoint2(x + offset.x, y + offset.y);
                if (floor.getFloorGrid()[worldPos.x][worldPos.y].equals(getKey())) {
                    Character key = tileGrid[x][y + 1];
                    ObjectDescription tileDesc = tileMap.get(key);
                    if (tileDesc == null) {
                        tileDesc = floor.getTileMap().get(key);
                    }
                    floor.createGridTile(tileDesc, worldPos, layer);
                }
            }
        }
    }

    /**
     * Creates all entities related to this room by invoking their creation method. If an entity
     * symbol is not defined on the grid, it is assumed that no entity should be created there.
     * If the room's key is not present at the relative world position on the floor plan,
     * then it is assumed that the room entity is overridden by the floor plan.
     */
    public void createRoomEntities() {
        for (int x = 0; x < dimensions.x; x++) {
            for (int y = 0; y < dimensions.y; y++) {
                GridPoint2 worldPos = new GridPoint2(x + offset.x, y + offset.y);
                if (floor.getFloorGrid()[worldPos.x][worldPos.y].equals(getKey())) {
                    Character key = entityGrid[x][y + 1];
                    ObjectDescription entityDesc = entityMap.get(key);
                    if (entityDesc == null && !key.equals('.')) {
                        entityDesc = floor.getEntityMap().get(key);
                    }
                    floor.createGridEntity(entityDesc, worldPos);
                }
            }
        }
    }

    /**
     * @return list of all valid create locations for dynamic entities (e.g. players). These entities are
     * typically not defined in the prefabrication files. Null if room type is not a valid creating
     * room type.
     */
    public List<GridPoint2> getValidCreateLocations() {
        if (Arrays.stream(validCreateRooms).noneMatch(Predicate.isEqual(type))) {
            return new ArrayList<>();
        }
        List<GridPoint2> validCreateLocations = new ArrayList<>();
        for (int x = 0; x < dimensions.x; x++) {
            for (int y = 0; y < dimensions.y; y++) {
                if (!entityMap.containsKey(entityGrid[x][y + 1])) {
                    validCreateLocations.add(new GridPoint2(x + offset.x, y + offset.y));
                }
            }
        }
        return validCreateLocations;
    }

    public String getType() {
        return type;
    }

    public GridPoint2 getDimensions() {
        return dimensions;
    }

    public GridPoint2 getOffset() {
        return offset;
    }

    public ObjectMap<Character, ObjectDescription> getTileMap() {
        return tileMap;
    }

    public ObjectMap<Character, ObjectDescription> getEntityMap() {
        return entityMap;
    }

    public Character[][] getTileGrid() {
        return tileGrid;
    }

    public Character[][] getEntityGrid() {
        return entityGrid;
    }

    public Floor getFloor() {
        return floor;
    }

    public Character getKey() {
        return key;
    }

    public void setOffset(GridPoint2 offset) {
        this.offset = offset;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    @Override
    public void write(Json json) {
        // No purpose yet
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        try {
            String[] args = jsonData.asStringArray();
            type = args[0];
            dimensions = new GridPoint2(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            offset = new GridPoint2(Integer.parseInt(args[3]), Integer.parseInt(args[4]));

            assertValidType(type);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Room room = (Room) o;
        return Objects.equals(type, room.type) &&
            Objects.equals(offset, room.offset) &&
            Objects.equals(dimensions, room.dimensions) &&
            Objects.equals(tileMap, room.tileMap) &&
            Objects.equals(entityMap, room.entityMap) &&
            Arrays.deepEquals(tileGrid, room.tileGrid) &&
            Arrays.deepEquals(entityGrid, room.entityGrid);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(type, offset, dimensions, tileMap, entityMap, floor);
        result = 31 * result + Arrays.deepHashCode(tileGrid);
        result = 31 * result + Arrays.deepHashCode(entityGrid);
        return result;
    }

    private static void assertValidType(String type) {
        if (Arrays.stream(validRoomTypes).noneMatch(Predicate.isEqual(type))) {
            throw new IllegalArgumentException("Type " + type + " is not a valid room type");
        }
    }

    private static final String[] validRoomTypes = {
        "bathroom", "bedroom", "dining", "front_foyer", "garage", "hallway", "kitchen", "laundry", "living"
    };

    private static final String[] validCreateRooms = {
        "living"
    };
}