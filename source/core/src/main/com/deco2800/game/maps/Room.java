package com.deco2800.game.maps;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.utils.math.GridPoint2Utils;
import com.deco2800.game.utils.math.MatrixUtils;
import com.deco2800.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Holds raw data for room object generation.
 * Contains functionality for randomising room interiors.
 */
public class Room implements Json.Serializable {

    private static final Logger logger = LoggerFactory.getLogger(Room.class);
    // Defined on deserialization
    private String type;
    private GridPoint2 offset;
    private GridPoint2 dimensions;
    // Defined on deserialization or call for creation
    private ObjectMap<Character, GridObject> tileMap;
    private ObjectMap<Character, GridObject> entityMap;
    private Character[][] tileGrid;
    private Character[][] entityGrid;
    // Defined on call for creation
    private Floor floor;
    private boolean created = false;

    public void create(Floor floor) {
        if (!created) {
            this.floor = floor;
            if (type.equals("hallway")) {
                createHallwayInterior();
            } else if (tileMap == null) {
                randomiseInterior();
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
                tileGrid[x][y] = ' ';
                if (x == 0 || y == dimensions.y - 1) {
                    entityGrid[x][y] = 'W';
                } else {
                    entityGrid[x][y] = ' ';
                }
            }
        }
    }

    /**
     * Queries for a list of JSON files in a pre-defined directory. Selects one at random
     * and initialises the room interior plan.
     */
    private void randomiseInterior() {
        Array<FileHandle> fileHandles = FileLoader.getJsonFiles(Home.DIRECTORY.concat(type));

        Interior randomInterior;
        do {
            FileHandle fileHandle = fileHandles.get(RandomUtils.getSeed().nextInt(fileHandles.size));
            randomInterior = FileLoader.readClass(Interior.class, fileHandle.path());
            fileHandles.removeValue(fileHandle, true);

            if (!dimensions.equals(randomInterior.getDimensions())) {
                randomInterior = null;
            }
        } while (randomInterior == null && fileHandles.size > 0);

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
                floor.invokeTileMethod(roomTile, worldPos, layer);
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
                if (roomEntity != null) {
                    floor.invokeEntityMethod(roomEntity, worldPos);
                }
            }
        }
    }

    public GridPoint2 getOffset() {
        return offset;
    }

    public void setOffset(GridPoint2 offset) {
        this.offset = offset;
    }

    /**
     * @param extension specific extension for all assets returned
     * @return asset filenames from the room to the individual objects
     */
    public String[] getAssets(String extension) {
        Array<String> temp = getAssets(tileMap, extension);
        temp.addAll(getAssets(entityMap, extension));

        String[] assets = new String[temp.size];
        for (int i = 0; i < temp.size; i++) {
            assets[i] = temp.get(i);
        }
        return assets;
    }

    private Array<String> getAssets(ObjectMap<Character, GridObject> map, String extension) {
        Array<String> assets = new Array<>();
        for (GridObject gridObject : new ObjectMap.Values<>(map)) {
            String[] objAssets = gridObject.getAssets(extension);
            assets.addAll(objAssets);
        }
        return assets;
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
}