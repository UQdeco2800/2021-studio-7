package com.deco2800.game.maps.rooms;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.maps.floors.Doorway;
import com.deco2800.game.maps.floors.FloorPlan;
import com.deco2800.game.maps.terrain.TerrainTile;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.utils.math.MatrixUtils;
import com.deco2800.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class Room implements Json.Serializable {

    private static final Logger logger = LoggerFactory.getLogger(Room.class);
    private Integer maxDoorways;
    private ObjectMap<Class<? extends Room>, String[]> doorwayRestrictions;
    private Array<Doorway> extraDoorways;
    private RoomInterior interior;
    protected boolean created = false;

    public void create(GridPoint2 offset, Vector2 dimensions) {
    }

    public <T extends RoomInterior> T designateInterior(Class<T> type, Vector2 dimensions, String directory) {
        Array<FileHandle> fileHandles = FileLoader.getJsonFiles(directory);

        T randomInterior;
        do {
            FileHandle fileHandle = fileHandles.get(RandomUtils.getSeed().nextInt(fileHandles.size));
            randomInterior = FileLoader.readClass(type, fileHandle.path());
            fileHandles.removeValue(fileHandle, true);

            if (!dimensions.equals(randomInterior.getRoomScale())) {
                randomInterior = null;
            }
        } while (randomInterior == null && fileHandles.size > 0);

        if (randomInterior != null) {
            ((T) randomInterior).create();
        }
        return randomInterior;
    }

    public int getMaxDoorways() {
        return maxDoorways;
    }

    public ObjectMap<Class<? extends Room>, String[]> getDoorwayRestrictions() {
        return doorwayRestrictions;
    }

    public Array<Doorway> getExtraDoorways() {
        return extraDoorways;
    }

    public void addExtraDoorways(Array<Doorway> extraDoorways) {
        this.extraDoorways.addAll(extraDoorways);
    }

    public RoomInterior getInterior() {
        return interior;
    }

    @Override
    public void write(Json json) {
        json.writeObjectStart();
        json.writeValue("maxDoorways", maxDoorways);
        json.writeValue("doorwayRestrictions", doorwayRestrictions);
        json.writeValue("extraDoorways", extraDoorways);
        json.writeValue("interior", interior);
        json.writeObjectEnd();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void read(Json json, JsonValue jsonData) {
        JsonValue iterator = jsonData.child();
        try {
            FileLoader.assertJsonValueName(iterator, "maxDoorways");
            maxDoorways = iterator.asInt();

            iterator = iterator.next();
            FileLoader.assertJsonValueName(iterator, "doorwayRestrictions");
            doorwayRestrictions = new ObjectMap<>();
            JsonValue restrictionsIterator = iterator.child();
            do {
                doorwayRestrictions.put(
                        (Class<? extends Room>) Class.forName(restrictionsIterator.name()),
                        restrictionsIterator.asStringArray());
                restrictionsIterator = restrictionsIterator.next();
            } while (restrictionsIterator != null);

            iterator = iterator.next();
            if (iterator != null) {
                FileLoader.assertJsonValueName(iterator, "extraDoorways");
                extraDoorways = new Array<>();
                FileLoader.readObjectArray(Doorway.class, extraDoorways, json, jsonData);
                iterator = iterator.next();
            } else {
                extraDoorways = null;
            }

            if (iterator != null) {
                FileLoader.assertJsonValueName(iterator, "interior");
                interior = new RoomInterior();
                interior.read(json, iterator);
                iterator = iterator.next();
            } else {
                interior = null;
            }

            FileLoader.assertJsonValueNull(iterator);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    static public class RoomInterior implements Json.Serializable {

        private ObjectMap<Character, RoomObject> interiorTiles;
        private ObjectMap<Character, RoomObject> interiorEntities;
        private Character[][] tileGrid;
        private Character[][] entityGrid;
        private Vector2 roomScale;
        protected boolean created = false;

        public void create() {
        }

        public Vector2 getRoomScale() {
            return roomScale;
        }

        public ObjectMap<Character, RoomObject> getInteriorTiles() {
            return interiorTiles;
        }

        public ObjectMap<Character, RoomObject> getInteriorEntities() {
            return interiorEntities;
        }

        public Character[][] getTileGrid() {
            return tileGrid;
        }

        public Character[][] getEntityGrid() {
            return entityGrid;
        }

        public String[] getRoomAssets(String extension) {
            Array<String> temp = getAssetsWithExtension(interiorTiles, extension);
            temp.addAll(getAssetsWithExtension(interiorEntities, extension));

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
            json.writeValue("interiorTiles", interiorTiles);
            json.writeValue("interiorEntities", interiorEntities);
            json.writeValue("tileGrid", tileGrid);
            json.writeValue("entityGrid", entityGrid);
            json.writeObjectEnd();
        }

        @Override
        public void read(Json json, JsonValue jsonData) {
            JsonValue iterator = jsonData.child();
            try {
                FileLoader.assertJsonValueName(iterator, "interiorTiles");
                interiorTiles = new ObjectMap<>();
                FileLoader.readCharacterObjectMap(RoomObject.class, interiorTiles, json, iterator);

                iterator = iterator.next();
                FileLoader.assertJsonValueName(iterator, "interiorEntities");
                interiorEntities = new ObjectMap<>();
                FileLoader.readCharacterObjectMap(RoomObject.class, interiorEntities, json, iterator);

                iterator = iterator.next();
                FileLoader.assertJsonValueName(iterator, "tileGrid");
                tileGrid = new Character[iterator.size][iterator.child().size];
                FileLoader.readCharacterGrid(tileGrid, iterator);
                MatrixUtils.flipVertically(tileGrid);

                iterator = iterator.next();
                FileLoader.assertJsonValueName(iterator, "entityGrid");
                entityGrid = new Character[iterator.size][iterator.child().size];
                FileLoader.readCharacterGrid(entityGrid, iterator);
                MatrixUtils.flipVertically(entityGrid);

                roomScale = new Vector2(tileGrid[0].length, tileGrid.length);

                FileLoader.assertJsonValueNull(iterator.next());
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }
}