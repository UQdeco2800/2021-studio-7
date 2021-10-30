package com.deco2800.game.maps;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.utils.math.MatrixUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Main purpose is to extract raw room interior data,
 * which is injected into a Room instance.
 */
public class Interior implements Json.Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Interior.class);
    // Defined from deserialization or constructor injection
    private ObjectMap<Character, ObjectData> tileMap;
    private ObjectMap<Character, ObjectData> entityMap;
    private Character[][] tileGrid;
    private Character[][] entityGrid;
    private GridPoint2 dimensions;
    private int numRotations;
    private Room room;

    public Interior() {
    }

    public Interior(ObjectMap<Character, ObjectData> tileMap, ObjectMap<Character, ObjectData> entityMap,
                    Character[][] tileGrid, Character[][] entityGrid, GridPoint2 dimensions) {
        this.tileMap = tileMap;
        this.entityMap = entityMap;
        this.tileGrid = tileGrid;
        this.entityGrid = entityGrid;
        this.dimensions = dimensions;
    }

    public boolean calibrateInteriorToRoom() {
        injectFloorEntityOverrides();
        calibrateInteriorToFloor();
        return numRotations < 4;
    }

    private void injectFloorEntityOverrides() {
        for (int x = 0; x < dimensions.x; x++) {
            for (int y = 0; y < dimensions.y; y++) {
                GridPoint2 worldPos = new GridPoint2(x + room.getOffset().x, y + room.getOffset().y);
                Character floorSymbol = room.getFloor().getFloorGrid()[worldPos.x][worldPos.y];
                if (!floorSymbol.equals(room.getKey())) {
                    // Retain overriding entity on room's entity grid
                    entityGrid[x][y] = floorSymbol;
                }
            }
        }
    }

    private void calibrateInteriorToFloor() {
        Character[][] nonWallTileGrid = new Character[dimensions.x - 1][dimensions.y - 1];
        Character[][] nonWallEntityGrid = new Character[dimensions.x - 1][dimensions.y - 1];
        for (int x = 1; x < dimensions.x; x++) {
            for (int y = 0; y < dimensions.y - 1; y++) {
                nonWallTileGrid[x - 1][y] = tileGrid[x][y];
                nonWallEntityGrid[x - 1][y] = entityGrid[x][y];
            }
        }

        List<GridPoint2> horizontalDoors = getHorizontalDoors();
        List<GridPoint2> verticalDoors = getVerticalDoors();
        int numRotations = 0;
        do {
            if (!checkHorizontalDoorCollisions(horizontalDoors, nonWallEntityGrid) ||
                !checkVerticalDoorCollisions(verticalDoors, nonWallEntityGrid)) {
                nonWallTileGrid = MatrixUtils.rotateClockwise(nonWallTileGrid);
                nonWallEntityGrid = MatrixUtils.rotateClockwise(nonWallEntityGrid);
                numRotations++;
                if (dimensions.x != dimensions.y) {
                    nonWallTileGrid = MatrixUtils.rotateClockwise(nonWallTileGrid);
                    nonWallEntityGrid = MatrixUtils.rotateClockwise(nonWallEntityGrid);
                    numRotations++;
                }
            } else {
                break;
            }
        } while (numRotations < 4);

        if (numRotations < 4) {
            for (int x = 1; x < dimensions.x; x++) {
                for (int y = 0; y < dimensions.y - 1; y++) {
                    tileGrid[x][y] = nonWallTileGrid[x - 1][y];
                    entityGrid[x][y] = nonWallEntityGrid[x - 1][y];
                }
            }
        }

        this.numRotations = numRotations;
    }

    public List<GridPoint2> getHorizontalDoors() {
        List<GridPoint2> horizontalDoors = new ArrayList<>();
        for (int y = 0; y < dimensions.y; y++) {
            if (!entityMap.containsKey(entityGrid[0][y])) {
                String floorObjectName = Home.getObjectName(
                    room.getFloor().getEntityMap().get(entityGrid[0][y]));
                if (floorObjectName != null && floorObjectName.contains("door")) {
                    horizontalDoors.add(new GridPoint2(0, y));
                }
            }
        }
        return horizontalDoors;
    }

    public List<GridPoint2> getVerticalDoors() {
        List<GridPoint2> verticalDoors = new ArrayList<>();
        for (int x = 0; x < dimensions.x; x++) {
            if (!entityMap.containsKey(entityGrid[x][dimensions.y - 1])) {
                String floorObjectName = Home.getObjectName(
                    room.getFloor().getEntityMap().get(entityGrid[x][dimensions.y - 1]));
                if (floorObjectName != null && floorObjectName.contains("door")) {
                    verticalDoors.add(new GridPoint2(x, dimensions.y - 1));
                }
            }
        }
        return verticalDoors;
    }

    public boolean checkHorizontalDoorCollisions(List<GridPoint2> horizontalDoors, Character[][] entityGrid) {
        for (GridPoint2 doorPosition : horizontalDoors) {
            if (entityMap.containsKey(entityGrid[0][doorPosition.y])) {
                return false;
            }
        }
        return true;
    }

    public boolean checkVerticalDoorCollisions(List<GridPoint2> verticalDoors, Character[][] entityGrid) {
        for (GridPoint2 doorPosition : verticalDoors) {
            if (entityMap.containsKey(entityGrid[doorPosition.x - 1][entityGrid[0].length - 1])) {
                return false;
            }
        }
        return true;
    }

    public ObjectMap<Character, ObjectData> getTileMap() {
        return tileMap;
    }

    public ObjectMap<Character, ObjectData> getEntityMap() {
        return entityMap;
    }

    public Character[][] getTileGrid() {
        return tileGrid;
    }

    public Character[][] getEntityGrid() {
        return entityGrid;
    }

    public GridPoint2 getDimensions() {
        return dimensions;
    }

    public int getNumRotations() {
        return numRotations;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public void write(Json json) {
        // No purpose yet
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        try {
            JsonValue iterator = jsonData.child();
            tileMap = new ObjectMap<>();
            FileLoader.readCharacterObjectNameMap("tileMap", tileMap, iterator);

            iterator = iterator.next();
            entityMap = new ObjectMap<>();
            FileLoader.readCharacterObjectNameMap("entityMap", entityMap, iterator);

            iterator = iterator.next();
            tileGrid = new Character[iterator.size][iterator.child().size];
            FileLoader.readCharacterGrid("tileGrid", tileGrid, iterator);

            iterator = iterator.next();
            entityGrid = new Character[iterator.size][iterator.child().size];
            FileLoader.readCharacterGrid("entityGrid", entityGrid, iterator);

            dimensions = new GridPoint2(tileGrid[0].length, tileGrid.length);

            tileGrid = MatrixUtils.rotateClockwise(tileGrid);
            entityGrid = MatrixUtils.rotateClockwise(entityGrid);

            FileLoader.assertJsonValueNull(iterator.next());
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
        Interior interior = (Interior) o;
        return Objects.equals(tileMap, interior.tileMap) &&
            Objects.equals(entityMap, interior.entityMap) &&
            Arrays.deepEquals(tileGrid, interior.tileGrid) &&
            Arrays.deepEquals(entityGrid, interior.entityGrid) &&
            Objects.equals(dimensions, interior.dimensions);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(tileMap, entityMap, dimensions);
        result = 31 * result + Arrays.deepHashCode(tileGrid);
        result = 31 * result + Arrays.deepHashCode(entityGrid);
        return result;
    }
}