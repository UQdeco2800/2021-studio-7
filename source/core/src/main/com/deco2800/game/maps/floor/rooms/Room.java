package com.deco2800.game.maps.floor.rooms;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.maps.floor.Doorway;
import com.deco2800.game.maps.terrain.TerrainTile;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Room {

    protected static final Logger logger = LoggerFactory.getLogger(Room.class);
    private final Integer maxDoorways;
    private final ObjectMap<Class<Room>, String[]> doorwayRestrictions;
    private Array<Doorway> extraDoorways;
    private RoomInterior interior;

    public Room(Integer maxDoorways,
                ObjectMap<Class<Room>, String[]> doorwayRestrictions) {
        this.maxDoorways = maxDoorways;
        this.doorwayRestrictions = doorwayRestrictions;
        this.extraDoorways = new Array<>();
    }

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
        } while (randomInterior == null && fileHandles.size < 0);

        if (randomInterior != null) {
            randomInterior.create();
        }
        return randomInterior;
    }

    public int getMaxDoorways() {
        return maxDoorways;
    }

    public ObjectMap<Class<Room>, String[]> getDoorwayRestrictions() {
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

    public void setInterior(RoomInterior interior) {
        this.interior = interior;
    }

    static public class RoomInterior {

        private final ObjectMap<Character, RoomObject> tileMappings;
        private final ObjectMap<Character, RoomObject> entityMappings;
        private final Character[][] tileGrid;
        private final Character[][] entityGrid;
        private final Vector2 roomScale;

        public RoomInterior(ObjectMap<Character, RoomObject> tileMappings,
                            ObjectMap<Character, RoomObject> entityMappings,
                            Character[][] tileGrid, Character[][] entityGrid) {
            this.tileMappings = tileMappings;
            this.entityMappings = entityMappings;
            this.tileGrid = tileGrid;
            this.entityGrid = entityGrid;
            this.roomScale = new Vector2(tileGrid.length, tileGrid[0].length);
        }

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
}