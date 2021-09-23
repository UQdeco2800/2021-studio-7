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
import com.deco2800.game.maps.terrain.TerrainTile;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.utils.math.RandomUtils;

public class Room implements Json.Serializable {

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
            randomInterior.create();
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
        try {
            jsonData = jsonData.child();
            maxDoorways = jsonData.asInt();

            jsonData = jsonData.next();
            JsonValue iterator = jsonData.child();
            doorwayRestrictions = new ObjectMap<>();
            do {
                doorwayRestrictions.put((Class<? extends Room>) Class.forName(iterator.name()),
                        iterator.asStringArray());
                iterator = iterator.next();
            } while (!iterator.isNull());

            jsonData = jsonData.next();
            if (!jsonData.isNull() && jsonData.name().equals("extraDoorways")) {
                iterator = jsonData.child();
                extraDoorways = new Array<>();
                do {
                    Doorway doorway = new Doorway();
                    doorway.read(json, iterator);
                    extraDoorways.add(doorway);
                    iterator = iterator.next();
                } while (!iterator.isNull());
                jsonData = jsonData.next();
            } else {
                extraDoorways = null;
            }

            if (!jsonData.isNull() && jsonData.name().equals("interior")) {
                interior = new RoomInterior();
                interior.read(json, jsonData);
                jsonData = jsonData.next();
            } else {
                interior = null;
            }

            assert jsonData.isNull();
        } catch (NullPointerException | ClassNotFoundException e) {
            throw new IllegalArgumentException("Error reading room at value " + jsonData.name());
        }
    }

    static public class RoomInterior implements Json.Serializable {

        private ObjectMap<Character, RoomObject> tileMappings;
        private ObjectMap<Character, RoomObject> entityMappings;
        private Character[][] tileGrid;
        private Character[][] entityGrid;
        private Vector2 roomScale;
        protected boolean created = false;

        public void create() {
        }

        public Vector2 getRoomScale() {
            return roomScale;
        }

        public ObjectMap<Character, RoomObject> getTileMappings() {
            return tileMappings;
        }

        public ObjectMap<Character, RoomObject> getEntityMappings() {
            return entityMappings;
        }

        public Character[][] getTileGrid() {
            return tileGrid;
        }

        public Character[][] getEntityGrid() {
            return entityGrid;
        }

        public String[] getRoomAssets(String extension) {
            Array<String> temp = getAssetsWithExtension(tileMappings, extension);
            temp.addAll(getAssetsWithExtension(entityMappings, extension));

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

        public ObjectMap<Character, TerrainTile> getCharacterTerrainTileMap() {
            ResourceService resourceService = ServiceLocator.getResourceService();
            ObjectMap<Character, TerrainTile> characterTerrainTileMap = new ObjectMap<>(tileMappings.size);
            for (ObjectMap.Entry<Character, RoomObject> entry : new ObjectMap.Entries<>(tileMappings)) {
                characterTerrainTileMap.put(entry.key, new TerrainTile(new TextureRegion(
                        resourceService.getAsset(entry.value.getAssets()[0], Texture.class))));
            }
            return characterTerrainTileMap;
        }

        @Override
        public void write(Json json) {
            json.writeObjectStart();
            json.writeValue("tileMappings", tileMappings);
            json.writeValue("entityMappings", entityMappings);
            json.writeValue("tileGrid", tileGrid);
            json.writeValue("entityGrid", entityGrid);
            json.writeObjectEnd();
        }

        @Override
        public void read(Json json, JsonValue jsonData) {
            try {
                jsonData = jsonData.child();
                tileMappings = new ObjectMap<>();
                jsonData = iterateRoomObjectMappings(tileMappings, json, jsonData);

                jsonData = jsonData.next();
                entityMappings = new ObjectMap<>();
                jsonData = iterateRoomObjectMappings(entityMappings, json, jsonData);

                jsonData = jsonData.next();
                tileGrid = new Character[jsonData.size][jsonData.child().size];
                jsonData = iterateRoomObjectGrid(tileGrid, jsonData);

                jsonData = jsonData.next();
                entityGrid = new Character[jsonData.size][jsonData.child().size];
                jsonData = iterateRoomObjectGrid(entityGrid, jsonData);

                jsonData = jsonData.next();
                roomScale = new Vector2(tileGrid[0].length, tileGrid.length);
                assert jsonData.isNull();
            } catch (NullPointerException e) {
                throw new IllegalArgumentException("Error reading room interior at value " + jsonData.name());
            }
        }

        public JsonValue iterateRoomObjectMappings(ObjectMap<Character, RoomObject> mappings,
                                                   Json json, JsonValue jsonData)
                throws NullPointerException {
            JsonValue iterator = jsonData.child();
            do {
                RoomObject entityObject = new RoomObject();
                entityObject.read(json, iterator);
                mappings.put(iterator.name().charAt(0), entityObject);
                iterator = iterator.next();
            } while (!iterator.isNull());
            return jsonData;
        }

        public JsonValue iterateRoomObjectGrid(Character[][] grid, JsonValue jsonData)
                throws NullPointerException {
            JsonValue iterator = jsonData.child();
            for (int y = 0; y < grid.length; y++) {
                JsonValue cellIterator = iterator.child();
                for (int x = 0; x < grid[0].length; x++) {
                    grid[x][y] = cellIterator.name().charAt(0);
                    cellIterator = cellIterator.next;
                }
                iterator = iterator.next;
            }
            assert iterator.isNull();
            return jsonData;
        }
    }
}