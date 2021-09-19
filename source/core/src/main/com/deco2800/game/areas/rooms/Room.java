package com.deco2800.game.areas.rooms;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.areas.terrain.TerrainTile;
import com.deco2800.game.generic.Component;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class Room extends Component {

    // Room interior
    public String[] rawTileGrid;
    public String[] rawEntityGrid;
    public ObjectMap<String, RoomObject> tileDefinitions;
    public ObjectMap<String, RoomObject> entityDefinitions;
    public GridPoint2[] entries;

    public Room(Vector2 roomScale, RoomObject[] tileDefinitions, RoomObject[] entityDefinitions,
                String[][] tileGrid, String[][] entityGrid) {
        this.roomScale = roomScale;
        this.tileDefinitions = tileDefinitions;
        this.entityDefinitions = entityDefinitions;
        this.tileGrid = tileGrid;
        this.entityGrid = entityGrid;
    }

    public Vector2 getRoomScale() {
        return roomScale;
    }

    public RoomObject[] getTileDefinitions() {
        return tileDefinitions;
    }

    public RoomObject[] getEntityDefinitions() {
        return entityDefinitions;
    }

    public String[][] getTileGrid() {
        return tileGrid;
    }

    public String[][] getEntityGrid() {
        return entityGrid;
    }

    public String[] getTileTextures() {
        return getTextures(tileDefinitions);
    }

    public String[] getEntityTextures() {
        return getTextures(entityDefinitions);
    }

    private String[] getTextures(RoomObject[] objectDefinitions) {
        Array<String> temp = new Array<>();
        for (RoomObject objectDefinition : objectDefinitions) {
            if (objectDefinition.getTexture() != null) {
                temp.add(objectDefinition.getTexture());
            }
        }

        String[] textures = new String[temp.size];
        for (int i = 0; i < temp.size; i++) {
            textures[i] = temp.get(i);
        }
        return textures;
    }

    public ObjectMap<String, TerrainTile> getSymbolTerrainTileMap() {
        ResourceService resourceService = ServiceLocator.getResourceService();
        ObjectMap<String, TerrainTile> stringTerrainTileMap = new ObjectMap(tileDefinitions.length);
        for (RoomObject current : tileDefinitions) {
            stringTerrainTileMap.put(current.getSymbol(), new TerrainTile(new TextureRegion(
                    resourceService.getAsset(current.getTexture(), Texture.class))));
        }
        return stringTerrainTileMap;
    }

    public ObjectMap<String, RoomObject> getSymbolObjectMap() {
        ObjectMap<String, RoomObject> stringRoomObjectMap = new ObjectMap(entityDefinitions.length);
        for (RoomObject current : entityDefinitions) {
            stringRoomObjectMap.put(current.getSymbol(), current);
        }
        return stringRoomObjectMap;
    }

    public int getMaxScale() {
        int max = (int) roomScale.x;
        if (roomScale.x < roomScale.y) {
            max = (int) roomScale.y;
        }
        return max;
    }

    static public class RoomStyle {
        // Room placement
        public int maximumEntries;
        public ObjectMap<Class<Room>, EntryType[]> entryRestrictions;
    }

    enum EntryType {
        SINGLE_DOOR, DOUBLE_DOOR, SINGLE_NO_DOOR, OPEN_SPACE
    }
}