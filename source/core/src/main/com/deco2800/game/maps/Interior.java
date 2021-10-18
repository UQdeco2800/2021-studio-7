package com.deco2800.game.maps;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.utils.math.MatrixUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Objects;

/**
 * Main purpose is to extract raw room interior data,
 * which is injected into a Room instance.
 */
public class Interior implements Json.Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Interior.class);
    // Defined from deserialization or constructor injection
    private ObjectMap<Character, GridObject> tileMap;
    private ObjectMap<Character, GridObject> entityMap;
    private Character[][] tileGrid;
    private Character[][] entityGrid;
    private GridPoint2 dimensions;

    public Interior() {
    }

    public Interior(ObjectMap<Character, GridObject> tileMap, ObjectMap<Character, GridObject> entityMap,
                    Character[][] tileGrid, Character[][] entityGrid, GridPoint2 dimensions) {
        this.tileMap = tileMap;
        this.entityMap = entityMap;
        this.tileGrid = tileGrid;
        this.entityGrid = entityGrid;
        this.dimensions = dimensions;
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

    public GridPoint2 getDimensions() {
        return dimensions;
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
    public void write(Json json) {
        json.writeObjectStart();
        json.writeValue("tileMap", tileMap);
        json.writeValue("entityMap", entityMap);
        json.writeValue("tileGrid", tileGrid);
        json.writeValue("entityGrid", entityGrid);
        json.writeObjectEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        try {
            JsonValue iterator = jsonData.child();
            tileMap = new ObjectMap<>();
            FileLoader.readCharacterObjectMap("tileMap", tileMap, GridObject.class, json, iterator);

            iterator = iterator.next();
            entityMap = new ObjectMap<>();
            FileLoader.readCharacterObjectMap("entityMap", entityMap, GridObject.class, json, iterator);

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
}