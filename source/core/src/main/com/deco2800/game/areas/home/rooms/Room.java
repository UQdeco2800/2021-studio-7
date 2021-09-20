package com.deco2800.game.areas.home.rooms;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.areas.home.RoomObject;
import com.deco2800.game.areas.terrain.TerrainTile;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Room {

    protected static final Logger logger = LoggerFactory.getLogger(Room.class);
    protected String roomFilepath;

    // Room interior
    protected String roomStyleResource;
    protected ObjectMap<Character, RoomObject> tileMappings;
    protected ObjectMap<Character, RoomObject> entityMappings;
    protected Character[][] tileGrid;
    protected Character[][] entityGrid;

    // Room placement
    protected RoomStyle roomStyle;
    protected GridPoint2[] extraDoorwayTiles;

    public Room(String roomFilepath) {
        this(roomFilepath, null);
    }

    public Room(String roomFilepath, RoomStyle roomStyle) {
    }

    public Vector2 getRoomScale() {
        return new Vector2(tileGrid.length, tileGrid[0].length);
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

    public String[] getTileTextures() {
        return getAssetsWithExtension(tileMappings, ".png");
    }

    public String[] getEntityTextures() {
        return getAssetsWithExtension(entityMappings, ".png");
    }

    public String[] getTileAtlases() {
        return getAssetsWithExtension(tileMappings, ".atlas");
    }

    public String[] getEntityAtlases() {
        return getAssetsWithExtension(entityMappings, ".atlas");
    }

    private String[] getAssetsWithExtension(ObjectMap<Character, RoomObject> mappings, String extension) {
        Array<String> temp = new Array<>();
        for (ObjectMap.Entry<Character, RoomObject> mapping : mappings) {
            String asset = mapping.value.getAsset();
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

    public ObjectMap<Character, TerrainTile> getSymbolTerrainTileMap() {
        ResourceService resourceService = ServiceLocator.getResourceService();
        ObjectMap<Character, TerrainTile> symbolTerrainTileMap = new ObjectMap(tileMappings.size);
        for (ObjectMap.Entry<Character, RoomObject> entry : tileMappings) {
            symbolTerrainTileMap.put(entry.key, new TerrainTile(new TextureRegion(
                    resourceService.getAsset(entry.value.getAsset(), Texture.class))));
        }
        return symbolTerrainTileMap;
    }

    static public class RoomStyle {
        // Room placement
        protected int maximumEntries;
        protected ObjectMap<Class<Room>, DoorwayType[]> doorwayRestrictions;

        public RoomStyle(int maximumEntries, ObjectMap<Class<Room>, DoorwayType[]> doorwayRestrictions) {
            this.maximumEntries = maximumEntries;
            this.doorwayRestrictions = doorwayRestrictions;
        }

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