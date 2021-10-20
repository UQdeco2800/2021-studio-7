package com.deco2800.game.maps;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.entities.factories.ObjectFactory;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.utils.math.GridPoint2Utils;
import com.deco2800.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Predicate;

/**
 * Holds raw data for room object generation.
 * Contains functionality for randomising room interiors.
 */
public class Room implements Json.Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Room.class);
    // Defined from deserialization or constructor injection
    private String type;
    private GridPoint2 offset;
    private GridPoint2 dimensions;
    // Defined from deserialization (when interior is defined) or constructor injection
    private ObjectMap<Character, GridObject> tileMap;
    private ObjectMap<Character, GridObject> entityMap;
    private Character[][] tileGrid;
    private Character[][] entityGrid;
    // Defined from constructor injection
    private Floor floor;
    private boolean created = false;

    public Room() {
    }

    public Room(String type, GridPoint2 offset, GridPoint2 dimensions) {
        this(type, offset, dimensions, null);
    }

    public Room(String type, GridPoint2 offset, GridPoint2 dimensions, Interior interior) {
        this.type = type;
        this.offset = offset;
        this.dimensions = dimensions;
        if (interior != null) {
            this.tileMap = interior.getTileMap();
            this.entityMap = interior.getEntityMap();
            this.tileGrid = interior.getTileGrid();
            this.entityGrid = interior.getEntityGrid();
        }
    }

    public void create(Floor floor) {
        if (!created) {
            this.floor = floor;
            if (type.equals("hallway")) {
                createHallwayInterior();
            } else if (tileMap == null) {
                createRandomInterior();
            }
        }
        created = true;
    }

    /**
     * Creates a basic room interior with the default floor tile texture.
     */
    private void createHallwayInterior() {
        tileMap = new ObjectMap<>();
        entityMap = new ObjectMap<>();
        entityMap.put('W', floor.getDefaultInteriorWall());
        tileGrid = new Character[dimensions.x][dimensions.y];
        entityGrid = new Character[dimensions.x][dimensions.y];
        for (int x = 0; x < dimensions.x; x++) {
            for (int y = 0; y < dimensions.y; y++) {
                tileGrid[x][y] = '.';
                if (x == 0 || y == dimensions.y - 1) {
                    entityGrid[x][y] = 'W';
                } else {
                    entityGrid[x][y] = '.';
                }
            }
        }
    }

    /**
     * Queries for a list of JSON files in a pre-defined directory. Selects one at random
     * and initialises the room interior plan.
     */
    private void createRandomInterior() {
        List<FileHandle> fileHandles = FileLoader.getJsonFiles(Home.DIRECTORY.concat(type));

        Interior randomInterior;
        do {
            FileHandle fileHandle = fileHandles.get(RandomUtils.getSeed().nextInt(fileHandles.size()));
            randomInterior = FileLoader.readClass(Interior.class, fileHandle.path());
            fileHandles.remove(fileHandle);

            if (!dimensions.equals(randomInterior.getDimensions())) {
                randomInterior = null;
            }
        } while (randomInterior == null && !fileHandles.isEmpty());

        if (randomInterior == null) {
            throw new NullPointerException("A valid room interior json file could not be loaded");
        }

        tileMap = randomInterior.getTileMap();
        entityMap = randomInterior.getEntityMap();
        tileGrid = randomInterior.getTileGrid();
        entityGrid = randomInterior.getEntityGrid();
    }

    /**
     * Spawns all tiles related to this room by invoking their creation method. If a tile
     * symbol is not defined on the grid, it is assumed that the default floor tile texture
     * is to be used.
     * @param layer tile grid with this room's additions
     */
    public void spawnRoomTiles(TiledMapTileLayer layer) {
        for (int x = 0; x < dimensions.x; x++) {
            for (int y = 0; y < dimensions.y; y++) {
                GridPoint2 worldPos = new GridPoint2(x + offset.x, y + offset.y);
                GridObject roomTile = tileMap.get(tileGrid[x][y]);
                if (roomTile == null) {
                    roomTile = floor.getDefaultInteriorTile();
                }
                floor.spawnGridTile(roomTile, worldPos, layer);
            }
        }
    }

    /**
     * Spawns all entities related to this room by invoking their creation method. If an entity
     * symbol is not defined on the grid, it is assumed that no entity should be spawned there.
     * If the room's key is not present at the relative world position on the floor plan,
     * then it is assumed that the room entity is overridden by the floor plan.
     * @param key symbol throughout the floor plan that denotes this room.
     */
    public void spawnRoomEntities(Character key) {
        for (int x = 0; x < dimensions.x; x++) {
            for (int y = 0; y < dimensions.y; y++) {
                GridPoint2 worldPos = new GridPoint2(x + offset.x, y + offset.y);
                Character floorSymbol = floor.getFloorGrid()[worldPos.x][worldPos.y];
                Character roomSymbol = entityGrid[x][y];
                GridObject roomEntity;
                if (!floorSymbol.equals(key)) {
                    // Restore overridden room symbol on floor grid
                    floor.getFloorGrid()[worldPos.x][worldPos.y] = key;
                    // Retain overriding entity on room's entity grid
                    entityGrid[x][y] = floorSymbol;
                    roomEntity = floor.getEntityMap().get(floorSymbol);
                } else {
                    roomEntity = entityMap.get(roomSymbol);
                }
                if (roomEntity == null) {
                    continue;
                }
                if (roomEntity.getMethod().getName().equals("createBed")) {
                    floor.stashBedPosition(worldPos);
                } else {
                    floor.spawnGridEntity(roomEntity, worldPos);
                }
            }
        }
    }

    /**
     * @return list of all valid spawn locations for dynamic entities (e.g. players). These entities are
     * typically not defined in the prefabrication files. Null if room type is not a valid spawning
     * room type.
     */
    public List<GridPoint2> getValidSpawnLocations() {
        if (Arrays.stream(validSpawnRooms).noneMatch(Predicate.isEqual(type))) {
            return new ArrayList<>();
        }
        List<GridPoint2> validSpawnLocations = new ArrayList<>();
        for (int x = 0; x < entityGrid.length; x++) {
            for (int y = 0; y < entityGrid[x].length; y++) {
                if (!entityMap.containsKey(entityGrid[x][y])) {
                    validSpawnLocations.add(new GridPoint2(x + offset.x, y + offset.y));
                }
            }
        }
        return validSpawnLocations;
    }

    public GridPoint2 getOffset() {
        return offset;
    }

    public void setOffset(GridPoint2 offset) {
        this.offset = offset;
    }

    public GridPoint2 getDimensions() {
        return dimensions;
    }

    public ObjectMap<Character, GridObject> getTileMap() {
        return tileMap;
    }

    public ObjectMap<Character, GridObject> getEntityMap() {
        return entityMap;
    }

    public Character[][] getTileGrid() {
        return tileGrid;
    }

    public Character[][] getEntityGrid() {
        return entityGrid;
    }

    /**
     * @param extension specific extension for all assets returned
     * @return asset filenames from the room to the individual objects
     */
    public List<String> getAssets(String extension) {
        List<String> assetsWithExtension = new ArrayList<>(getAssets(tileMap, extension));
        assetsWithExtension.addAll(getAssets(entityMap, extension));
        return assetsWithExtension;
    }

    private List<String> getAssets(ObjectMap<Character, GridObject> map, String extension) {
        List<String> assetsWithExtension = new ArrayList<>();
        for (GridObject gridObject : new ObjectMap.Values<>(map)) {
            assetsWithExtension.addAll(gridObject.getAssets(extension));
        }
        return assetsWithExtension;
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
        int result = Objects.hash(type, offset, dimensions, tileMap, entityMap, floor, created);
        result = 31 * result + Arrays.deepHashCode(tileGrid);
        result = 31 * result + Arrays.deepHashCode(entityGrid);
        return result;
    }

    @Override
    public void write(Json json) {
        json.writeObjectStart();
        json.writeValue("type", type);
        json.writeValue("offset", offset);
        json.writeValue("dimensions", dimensions);
        json.writeObjectEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        try {
            JsonValue iterator = jsonData.child();
            FileLoader.assertJsonValueName(iterator, "type");
            type = iterator.asString();
            Room.assertValidType(type);

            iterator = iterator.next();
            FileLoader.assertJsonValueName(iterator, "offset");
            offset = GridPoint2Utils.read(iterator);

            iterator = iterator.next();
            FileLoader.assertJsonValueName(iterator, "dimensions");
            dimensions = GridPoint2Utils.read(iterator);

            iterator = iterator.next();
            if (iterator != null) {
                FileLoader.assertJsonValueName(iterator, "interior");
                Interior interior = new Interior();
                interior.read(json, iterator);
                tileMap = interior.getTileMap();
                entityMap = interior.getEntityMap();
                tileGrid = interior.getTileGrid();
                entityGrid = interior.getEntityGrid();
                iterator = iterator.next();
            }

            FileLoader.assertJsonValueNull(iterator);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private static void assertValidType(String type) {
        if (Arrays.stream(validRoomTypes).noneMatch(Predicate.isEqual(type))) {
            throw new IllegalArgumentException("Type " + type + " is not a valid room type");
        }
    }

    private static final String[] validRoomTypes = {
            "bathroom", "bedroom", "dining", "front_foyer", "garage", "hallway", "kitchen", "laundry", "living"
    };

    private static final String[] validSpawnRooms = {
            "living"
    };
}