package com.deco2800.game.maps.rooms;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.utils.math.MatrixUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Room implements Json.Serializable {

    private static final Logger logger = LoggerFactory.getLogger(Room.class);
    private ObjectMap<Character, RoomObject> roomTiles;
    private ObjectMap<Character, RoomObject> roomEntities;
    private Character[][] tileGrid;
    private Character[][] entityGrid;
    private Vector2 dimensions;
    protected boolean created = false;

    public void create() {
    }

    public ObjectMap<Character, RoomObject> getRoomTiles() {
        return roomTiles;
    }

    public ObjectMap<Character, RoomObject> getRoomEntities() {
        return roomEntities;
    }

    public Character[][] getTileGrid() {
        return tileGrid;
    }

    public Character[][] getEntityGrid() {
        return entityGrid;
    }

    public Vector2 getDimensions() {
        return dimensions;
    }

    public String[] getRoomAssets(String extension) {
        Array<String> temp = getAssetsWithExtension(roomTiles, extension);
        temp.addAll(getAssetsWithExtension(roomEntities, extension));

        String[] assets = new String[temp.size];
        for (int i = 0; i < temp.size; i++) {
            assets[i] = temp.get(i);
        }
        return assets;
    }

    private Array<String> getAssetsWithExtension(ObjectMap<Character, RoomObject> mappings, String extension) {
        Array<String> assets = new Array<>();
        for (RoomObject roomObject : new ObjectMap.Values<>(mappings)) {
            String[] objAssets = roomObject.getAssets();
            if (objAssets != null) {
                for (String asset : objAssets) {
                    if (asset.endsWith(extension)) {
                        assets.add(asset);
                    }
                }
            }
        }
        return assets;
    }

    @Override
    public void write(Json json) {
        json.writeObjectStart();
        json.writeValue("roomTiles", roomTiles);
        json.writeValue("roomEntities", roomEntities);
        json.writeValue("tileGrid", tileGrid);
        json.writeValue("entityGrid", entityGrid);
        json.writeObjectEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        JsonValue iterator = jsonData.child();
        try {
            FileLoader.assertJsonValueName(iterator, "roomTiles");
            roomTiles = new ObjectMap<>();
            FileLoader.readCharacterObjectMap(RoomObject.class, roomTiles, json, iterator);

            iterator = iterator.next();
            FileLoader.assertJsonValueName(iterator, "roomEntities");
            roomEntities = new ObjectMap<>();
            FileLoader.readCharacterObjectMap(RoomObject.class, roomEntities, json, iterator);

            iterator = iterator.next();
            FileLoader.assertJsonValueName(iterator, "tileGrid");
            tileGrid = new Character[iterator.size][iterator.child().size];
            FileLoader.readCharacterGrid(tileGrid, iterator);
            tileGrid = MatrixUtils.rotateAntiClockwise(tileGrid);

            iterator = iterator.next();
            FileLoader.assertJsonValueName(iterator, "entityGrid");
            entityGrid = new Character[iterator.size][iterator.child().size];
            FileLoader.readCharacterGrid(entityGrid, iterator);
            entityGrid = MatrixUtils.rotateAntiClockwise(entityGrid);

            dimensions = new Vector2(tileGrid.length, tileGrid[0].length);

            FileLoader.assertJsonValueNull(iterator.next());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}