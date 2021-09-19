package com.deco2800.game.areas.rooms;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.areas.HomeGameArea;
import com.deco2800.game.areas.terrain.TerrainTile;
import com.deco2800.game.generic.Component;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class Room {

    protected static final Logger logger = LoggerFactory.getLogger(Room.class);
    private final String roomFilepath;

    // Room interior
    private String roomStyleResource;
    private final ObjectMap<String, String> tileMappings;
    private final ObjectMap<String, RoomObject> entityMappings;
    private final String[] rawTileGrid;
    private final String[] rawEntityGrid;

    // Room placement
    private RoomStyle roomStyle;
    private GridPoint2[] extraDoorwayTiles;

    public Room(String roomFilepath, RoomSkin roomSkin) {
        this(roomFilepath, null);
    }

    public Room(String roomFilepath, RoomStyle roomStyle) {
        this.roomFilepath = roomFilepath;
        RoomReader reader = new RoomReader(roomFilepath);
        this.tileEntries = reader.extractEntries(RoomReader.TILE_HEADER);
        this.tileGrid = reader.extractGrid();
        this.entityEntries = reader.extractEntries(RoomReader.ENTITY_HEADER);
        this.entityGrid = reader.extractGrid();
        this.roomStyle = roomStyle;
    }

    public Vector2 getRoomScale() {
        return new Vector2(tileGrid.length, tileGrid[0].length);
    }

    public ObjectMap<String, RoomObject> getTileEntries() {
        return tileEntries;
    }

    public ObjectMap<String, RoomObject> getEntityEntries() {
        return entityEntries;
    }

    public String[][] getTileGrid() {
        return tileGrid;
    }

    public String[][] getEntityGrid() {
        return entityGrid;
    }

    public String[] getTileTextures() {
        return getAssetsWithExtension(tileEntries, ".png");
    }

    public String[] getEntityTextures() {
        return getAssetsWithExtension(entityEntries, ".png");
    }

    public String[] getTileAtlases() {
        return getAssetsWithExtension(tileEntries, ".atlas");
    }

    public String[] getEntityAtlases() {
        return getAssetsWithExtension(entityEntries, ".atlas");
    }

    private String[] getAssetsWithExtension(ObjectMap<String, RoomObject> entries, String extension) {
        Array<String> temp = new Array<>();
        for (ObjectMap.Entry<String, RoomObject> entry : entries) {
            String asset = entry.value.getAsset();
            if (asset != null && asset.endsWith(extension)) {
                temp.add(asset);
            }
        }

        String[] assets = new String[temp.size];
        for (int i = 0; i < temp.size; i++) {
            assets[i] = temp.get(i);
        }
        return assets;
    }

    public ObjectMap<String, TerrainTile> getSymbolTerrainTileMap() {
        ResourceService resourceService = ServiceLocator.getResourceService();
        ObjectMap<String, TerrainTile> symbolTerrainTileMap = new ObjectMap(tileEntries.size);
        for (ObjectMap.Entry<String, RoomObject> entry : tileEntries) {
            symbolTerrainTileMap.put(entry.key, new TerrainTile(new TextureRegion(
                    resourceService.getAsset(entry.value.getAsset(), Texture.class))));
        }
        return symbolTerrainTileMap;
    }

    static public class RoomObject {
        // Spawning data
        private final Method method;
        private final String asset;

        public RoomObject(String methodName, String assetName) {
            this.method = getDeclaredMethod(methodName, assetName);
            this.asset = assetName;
        }

        public Method getMethod() {
            return method;
        }

        public String getAsset() {
            return asset;
        }

        /**
         * Attempts to find the method name in the codebase. If found, a method
         * will be invoked later for object generation.
         * @param methodName method to be found
         * @param assetName optional asset to be loaded
         * @return found method signature
         */
        private Method getDeclaredMethod(String methodName, String assetName) {
            Method method = null;
            Class[] paramTypes;
            if (assetName == null) {
                paramTypes = new Class[]{GridPoint2.class};
            } else {
                paramTypes = new Class[]{GridPoint2.class, String.class};
            }

            try {
                method = (HomeGameArea.class).getDeclaredMethod(methodName, paramTypes);
            } catch (Exception e) {
                logger.error("Method {} could not be found", methodName);
            }

            return method;
        }
    }

    static public class RoomStyle {
        // Room placement
        private int maximumEntries;
        private ObjectMap<Class<Room>, DoorwayType[]> doorwayRestrictions;

        public int getMaximumEntries() {
            return maximumEntries;
        }

        public ObjectMap<Class<Room>, DoorwayType[]> getDoorwayRestrictions() {
            return doorwayRestrictions;
        }
    }

    enum DoorwayType {
        SINGLE_DOOR, DOUBLE_DOOR, SINGLE_NO_DOOR, OPEN_SPACE
    }
}