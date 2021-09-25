package com.deco2800.game.maps.floors;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.maps.rooms.Room;
import com.deco2800.game.maps.rooms.RoomObject;
import com.deco2800.game.maps.rooms.RoomProperties;
import com.deco2800.game.maps.terrain.TerrainComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.maps.terrain.TerrainTile;
import com.deco2800.game.screens.maingame.MainGameScreen;
import com.deco2800.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Floor extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(Floor.class);
    private static final String FLOOR_PLAN_DIRECTORY = RoomProperties.DIRECTORY.concat("_floor_plans");
    private final OrthographicCamera camera;
    private FloorPlan floorPlan;
    private Entity player;
    private boolean created = false;

    public Floor(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public void create() {
        if (!created) {
            floorPlan = designateHomeFloorPlan();
            loadAssets();
            displayUI();
            spawnHomeTiles();
            spawnHomeEntities();
        }
        created = true;
    }

    public FloorPlan designateHomeFloorPlan() {
        Array<FileHandle> fileHandles = FileLoader.getJsonFiles(FLOOR_PLAN_DIRECTORY);

        FloorPlan randomFloorPlan;
        do {
            FileHandle fileHandle = fileHandles.get(RandomUtils.getSeed().nextInt(fileHandles.size));
            randomFloorPlan = FileLoader.readClass(FloorPlan.class, fileHandle.path());
            fileHandles.removeValue(fileHandle, true);
        } while (randomFloorPlan == null && fileHandles.size > 0);

        if (randomFloorPlan != null) {
            randomFloorPlan.create();
        }
        return randomFloorPlan;
    }

    public void spawnHomeTiles() {
        TextureRegion textureRegion = new TextureRegion(
                ServiceLocator.getResourceService().getAsset(
                        floorPlan.getDefaultTileObject().getAssets()[0], Texture.class));

        TiledMapTileLayer layer = new TiledMapTileLayer(
                (int) floorPlan.getHomeDimensions().x,
                (int) floorPlan.getHomeDimensions().y,
                textureRegion.getRegionWidth(), textureRegion.getRegionHeight());

        for (int y = 0; y < floorPlan.getFloorGrid().length; y++) {
            for (int x = 0; x < floorPlan.getFloorGrid()[y].length; x++) {
                Character symbol = floorPlan.getFloorGrid()[y][x];
                FloorPlan.RoomPlan roomPlan = floorPlan.getRoomMappings().get(symbol);
                Room.RoomInterior roomInterior;
                RoomObject roomTile;
                if (roomPlan != null) {
                    roomInterior = roomPlan.getRoom().getInterior();
                    roomTile = roomInterior.getInteriorTiles().get(roomInterior.getTileGrid()
                    [y - roomPlan.getOffset().y][x - roomPlan.getOffset().x]);
                } else {
                    roomTile = floorPlan.getDefaultTileObject();
                }
                invokeTileMethod(roomTile, new GridPoint2(x, y), layer);
            }
        }

        TiledMap tiledMap = new TiledMap();
        tiledMap.getLayers().add(layer);
        TiledMapRenderer renderer = new IsometricTiledMapRenderer(tiledMap, 1f / textureRegion.getRegionWidth());
        terrain = new TerrainComponent(camera, tiledMap, renderer, 1f);
        spawnEntity(new Entity().addComponent(terrain));
    }

    public void spawnHomeEntities() {
        ServiceLocator.getResourceService().loadTexture("images/objects/walls/wall.png");
        ServiceLocator.getResourceService().loadTextureAtlas("images/characters/boy_00/boy_00.atlas");
        ServiceLocator.getResourceService().loadAll();

        for (int y = 0; y < floorPlan.getFloorGrid().length; y++) {
            for (int x = 0; x < floorPlan.getFloorGrid()[y].length; x++) {
                Character symbol = floorPlan.getFloorGrid()[y][x];
                FloorPlan.RoomPlan roomPlan = floorPlan.getRoomMappings().get(symbol);
                Room.RoomInterior roomInterior;
                RoomObject roomEntity;
                if (roomPlan != null) {
                    roomInterior = roomPlan.getRoom().getInterior();
                    roomEntity = roomInterior.getInteriorEntities().get(roomInterior.getEntityGrid()
                            [y - roomPlan.getOffset().y][x - roomPlan.getOffset().x]);
                    invokeEntityMethod(roomEntity, new GridPoint2(x, y));
                } else {
                    Entity wall = ObstacleFactory.createWall(new String[]{"images/objects/walls/wall.png"});
                    spawnEntityAt(wall, new GridPoint2(x, y), true, true);
                }
            }
        }

        player = PlayerFactory.createPlayer(new String[]{"images/characters/boy_00/boy_00.atlas"});
        spawnEntityAt(player, new GridPoint2(4,4), true, true);
    }

    public void invokeTileMethod(RoomObject tileObject, GridPoint2 position, TiledMapTileLayer layer) {
        if (tileObject == null) {
            tileObject = floorPlan.getDefaultTileObject();
        }
        try {
            TerrainTile tile = (TerrainTile) tileObject.getMethod().invoke(null, (Object) tileObject.getAssets());
            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            cell.setTile(tile);
            layer.setCell(position.x, position.y, cell);
        } catch (Exception e) {
            logger.error("Error invoking method {}", tileObject.getMethod().getName());
        }
    }

    public void invokeEntityMethod(RoomObject entityObject, GridPoint2 position) {
        if (entityObject == null) {
            return;
        }
        try {
            Entity entity = (Entity) entityObject.getMethod().invoke(null, (Object) entityObject.getAssets());
            spawnEntityAt(entity, position, true, true);
        } catch (Exception e) {
            logger.error("Error invoking method {}", entityObject.getMethod().getName());
        }
    }

    public Entity getPlayer() {
        return player;
    }

    public String[] getAllRoomAssets(String extension) {
        Array<String> temp = new Array<>();
        temp.addAll(floorPlan.getDefaultTileObject().getAssets());
        for (FloorPlan.RoomPlan roomFloorPlan : new ObjectMap.Values<>(floorPlan.getRoomMappings())) {
            for (String asset : roomFloorPlan.getRoom().getInterior().getRoomAssets(extension)) {
                if (!temp.contains(asset, true)) {
                    temp.add(asset);
                }
            }
        }
        String[] assets = new String[temp.size];
        for (int i = 0; i < assets.length; i++) {
            assets[i] = temp.get(i);
        }
        return assets;
    }

    private void displayUI() {
        Entity ui = new Entity();
        //ui.addComponent(new GameAreaDisplay("Box Forest"));
        spawnEntity(ui);
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(getAllRoomAssets(".png"));
        resourceService.loadTextureAtlases(getAllRoomAssets(".atlas"));

        while (!resourceService.loadForMillis(20)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(getAllRoomAssets(".png"));
        resourceService.unloadAssets(getAllRoomAssets(".atlas"));
    }

    @Override
    public void dispose() {
        super.dispose();
        this.unloadAssets();
    }
}