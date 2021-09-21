package com.deco2800.game.areas.home;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.areas.terrain.TerrainTile;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Room {

    protected static final Logger logger = LoggerFactory.getLogger(Room.class);
    protected final Integer maxDoorways;
    protected final ObjectMap<Class<Room>, String[]> doorwayRestrictions;
    protected Doorway[] extraDoorways;
    private RoomInterior interior;

    public Room(Integer maxDoorways,
                ObjectMap<Class<Room>, String[]> doorwayRestrictions) {
        this.maxDoorways = maxDoorways;
        this.doorwayRestrictions = doorwayRestrictions;
    }

    public static <T extends RoomInterior> T loadRandomInterior(Class<T> type, Vector2 dimensions, String directory) {
        T interior = null;
        Array<FileHandle> jsons = FileLoader.getJsonFiles(directory);
        do {
            FileHandle chosenFile = jsons.get(RandomUtils.getSeed().nextInt() % jsons.size);
            try {
                interior = FileLoader.readClass(type, chosenFile.path());
            } catch (ClassCastException e) {
                logger.error("File {} did not contain an instance of {}", chosenFile.path(), type);
            }
            if (interior == null || interior.getRoomScale().equals(dimensions)) {
                jsons.removeValue(chosenFile, true);
                interior = null;
                if (jsons.size == 0) {
                    break;
                }
            }
        } while (interior == null);

        return interior;
    }

    public int getMaximumDoorways() {
        return maxDoorways;
    }

    public ObjectMap<Class<Room>, String[]> getDoorwayRestrictions() {
        return doorwayRestrictions;
    }

    public Doorway[] getExtraDoorways() {
        return extraDoorways;
    }

    public void setExtraDoorways(Doorway[] extraDoorways) {
        this.extraDoorways = extraDoorways;
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
}