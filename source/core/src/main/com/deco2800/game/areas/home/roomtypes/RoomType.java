package com.deco2800.game.areas.home.roomtypes;

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

public class RoomType {

    protected static final Logger logger = LoggerFactory.getLogger(RoomType.class);

    // Room type
    protected Integer maxDoorways;
    protected ObjectMap<Class<RoomType>, DoorwayType[]> doorwayRestrictions;
    protected GridPoint2[] extraDoorwayTiles;
    protected RoomInterior interior;

    public RoomType(Integer maxDoorways,
                    ObjectMap<Class<RoomType>, DoorwayType[]> doorwayRestrictions) {
        this.maxDoorways = maxDoorways;
        this.doorwayRestrictions = doorwayRestrictions;
    }

    public int getMaximumDoorways() {
        return maxDoorways;
    }

    public ObjectMap<Class<RoomType>, DoorwayType[]> getDoorwayRestrictions() {
        return doorwayRestrictions;
    }

    public GridPoint2[] getExtraDoorwayTiles() {
        return extraDoorwayTiles;
    }

    public RoomInterior getInterior() {
        return interior;
    }

    static public class RoomInterior {
        protected ObjectMap<Character, RoomObject> tileMappings;
        protected ObjectMap<Character, RoomObject> entityMappings;
        protected Character[][] tileGrid;
        protected Character[][] entityGrid;

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

        public String[] getTextures() {
            Array<String> temp = getAssetsWithExtension(tileMappings, ".png");
            temp.addAll(getAssetsWithExtension(entityMappings, ".png"));

            String[] textures = new String[temp.size];
            for (int i = 0; i < temp.size; i++) {
                textures[i] = temp.get(i);
            }
            return textures;
        }

        public String[] getAtlases() {
            Array<String> temp = getAssetsWithExtension(tileMappings, ".atlas");
            temp.addAll(getAssetsWithExtension(entityMappings, ".atlas"));

            String[] atlases = new String[temp.size];
            for (int i = 0; i < temp.size; i++) {
                atlases[i] = temp.get(i);
            }
            return atlases;
        }

        private Array<String> getAssetsWithExtension(ObjectMap<Character, RoomObject> mappings, String extension) {
            Array<String> assets = new Array<>();
            for (ObjectMap.Entry<Character, RoomObject> mapping : mappings) {
                String[] objAssets = mapping.value.getAssets();
                if (objAssets != null) {
                    for (String asset : objAssets) {
                        assets.add(asset);
                    }
                }
            }
            return assets;
        }

        public ObjectMap<Character, TerrainTile> getCharacterTerrainTileMap() {
            ResourceService resourceService = ServiceLocator.getResourceService();
            ObjectMap<Character, TerrainTile> characterTerrainTileMap = new ObjectMap<>(tileMappings.size);
            for (ObjectMap.Entry<Character, RoomObject> entry : tileMappings) {
                characterTerrainTileMap.put(entry.key, new TerrainTile(new TextureRegion(
                        resourceService.getAsset(entry.value.getAssets()[0], Texture.class))));
            }
            return characterTerrainTileMap;
        }
    }

    enum DoorwayType {
        SINGLE_DOOR, DOUBLE_DOOR, SINGLE_NO_DOOR, OPEN_SPACE
    }
}