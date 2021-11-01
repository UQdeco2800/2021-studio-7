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
    private ObjectMap<Character, ObjectDescription> tileMap;
    private ObjectMap<Character, ObjectDescription> entityMap;
    private Character[][] tileGrid;
    private Character[][] entityGrid;
    private GridPoint2 dimensions;
    // Defined on initialisation
    private Room room;
    private ObjectMap<Character, ObjectDescription> overrideMap;
    private Character[][] overrideGrid;
    private int numRotations;

    public boolean calibrateDimensions(GridPoint2 roomDimensions) {
        if (!dimensions.equals(roomDimensions)) {
            if (dimensions.x != roomDimensions.y || dimensions.y != roomDimensions.x) {
                return false;
            }
            tileGrid = MatrixUtils.rotateClockwise(tileGrid);
            entityGrid = MatrixUtils.rotateClockwise(entityGrid);
            dimensions = roomDimensions;
            setNumRotationsDescriptions(1);
        }
        return true;
    }

    public boolean calibrateInterior() {
        extractOverridingObjects();
        calibrateInteriorToFloor();
        if (numRotations < 4) {
            setNumRotationsDescriptions(numRotations);
            injectOverridingObjects();
            return true;
        }
        return false;
    }

    private void extractOverridingObjects() {
        overrideMap = new ObjectMap<>();
        overrideGrid = new Character[dimensions.x][dimensions.y];

        for (int x = 0; x < dimensions.x; x++) {
            for (int y = 0; y < dimensions.y; y++) {
                GridPoint2 worldPos = new GridPoint2(x + room.getOffset().x, y + room.getOffset().y);
                Character key = room.getFloor().getFloorGrid()[worldPos.x][worldPos.y];
                if (!key.equals(room.getKey())) {
                    ObjectDescription desc = room.getFloor().getTileMap().get(key);
                    if (desc == null) {
                        desc = room.getFloor().getEntityMap().get(key);
                    }
                    overrideMap.put(key, desc);
                    overrideGrid[x][y] = key;
                } else {
                    overrideGrid[x][y] = null;
                }
            }
        }
    }

    private void calibrateInteriorToFloor() {
        List<Integer> horizontalDoors = getHorizontalDoorPositions();
        List<Integer> verticalDoors = getVerticalDoorPositions();

        do {
            if (areHorizontallyBlocked(horizontalDoors) || areVerticallyBlocked(verticalDoors)) {
                tileGrid = MatrixUtils.rotateClockwise(tileGrid);
                entityGrid = MatrixUtils.rotateClockwise(entityGrid);
                numRotations++;

                if (dimensions.x != dimensions.y) {
                    tileGrid = MatrixUtils.rotateClockwise(tileGrid);
                    entityGrid = MatrixUtils.rotateClockwise(entityGrid);
                    numRotations++;
                }
                continue;
            }
            break;
        } while (numRotations < 4);
    }

    private void setNumRotationsDescriptions(int numRotations) {
        for (ObjectDescription description : new ObjectMap.Values<>(tileMap)) {
            description.setNumRotations(numRotations);
        }
        for (ObjectDescription description : new ObjectMap.Values<>(entityMap)) {
            description.setNumRotations(numRotations);
        }
    }

    private void injectOverridingObjects() {
        for (int x = 0; x < dimensions.x; x++) {
            for (int y = 0; y < dimensions.y; y++) {
                Character key = overrideGrid[x][y];
                if (key != null) {
                    if (room.getFloor().getTileMap().containsKey(key)) {
                        tileGrid[x][y + 1] = key;
                    } else {
                        entityGrid[x][y + 1] = key;
                    }
                }
            }
        }
    }

    public List<Integer> getHorizontalDoorPositions() {
        List<Integer> horizontalDoors = new ArrayList<>();
        for (int y = 0; y < dimensions.y; y++) {
            Character key = overrideGrid[0][y];
            if (key != null && overrideMap.get(key) != null) {
                String objectName = Home.getObjectName(room.getFloor().getEntityMap().get(key).getData());
                if (objectName != null && objectName.contains("door")) {
                    horizontalDoors.add(y);
                }
            }
        }
        return horizontalDoors;
    }

    public List<Integer> getVerticalDoorPositions() {
        List<Integer> verticalDoors = new ArrayList<>();
        for (int x = 0; x < dimensions.x; x++) {
            Character key = overrideGrid[x][dimensions.y - 1];
            if (key != null && overrideMap.get(key) != null) {
                String objectName = Home.getObjectName(room.getFloor().getEntityMap().get(key).getData());
                if (objectName != null && objectName.contains("door")) {
                    verticalDoors.add(x);
                }
            }
        }
        return verticalDoors;
    }

    public boolean areHorizontallyBlocked(List<Integer> horizontalDoors) {
        for (Integer doorPosition : horizontalDoors) {
            if (entityMap.containsKey(entityGrid[1][doorPosition + 1])) {
                return true;
            }
        }
        return false;
    }

    public boolean areVerticallyBlocked(List<Integer> verticalDoors) {
        for (Integer doorPosition : verticalDoors) {
            if (entityMap.containsKey(entityGrid[doorPosition][dimensions.y - 1])) {
                return true;
            }
        }
        return false;
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

    public GridPoint2 getDimensions() {
        return dimensions;
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

            dimensions = new GridPoint2(tileGrid[0].length - 1, tileGrid.length - 1);

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