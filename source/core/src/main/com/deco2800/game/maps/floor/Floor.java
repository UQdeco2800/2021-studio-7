package com.deco2800.game.maps.floor;

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
import com.deco2800.game.maps.floor.rooms.Room;
import com.deco2800.game.maps.floor.rooms.RoomObject;
import com.deco2800.game.maps.floor.rooms.RoomProperties;
import com.deco2800.game.maps.components.TerrainComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.player.CameraComponent;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Floor extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(Floor.class);
    private static final String FLOOR_PLAN_DIRECTORY = RoomProperties.DIRECTORY.concat("_floor_plans");
    private final OrthographicCamera camera;
    private FloorPlan floorPlan;

    public Floor(CameraComponent cameraComponent) {
        super();
        this.camera = (OrthographicCamera) cameraComponent.getCamera();
    }
    
    @Override
    public void create() {
        RoomProperties.loadProperties();
        floorPlan = designateHomeFloorPlan();
        loadAssets();
        displayUI();
        generate();
    }

    public void generate() {
        spawnHomeTiles();
    }

    public FloorPlan designateHomeFloorPlan() {
        Array<FileHandle> fileHandles = FileLoader.getJsonFiles(FLOOR_PLAN_DIRECTORY);

        FloorPlan randomFloorPlan;
        do {
            FileHandle fileHandle = fileHandles.get(RandomUtils.getSeed().nextInt() % fileHandles.size);
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

        for (FloorPlan.RoomPlan roomFloorPlan : floorPlan.getRoomMappings().values()) {
            Room.RoomInterior roomInterior = roomFloorPlan.getRoom().getInterior();
            for (int x = 0; x < (int) roomInterior.getRoomScale().x; x++) {
                for (int y = 0; y < (int) roomInterior.getRoomScale().y; y++) {
                    RoomObject roomTile = roomInterior.getTileMappings().get(roomInterior.getTileGrid()[x][y]);
                    GridPoint2 homePosition = new GridPoint2(
                            x + roomFloorPlan.getOffset().x, y + roomFloorPlan.getOffset().y);
                    invokeTileMethod(roomTile, homePosition, layer);
                }
            }
        }

        TiledMap tiledMap = new TiledMap();
        tiledMap.getLayers().add(layer);
        TiledMapRenderer renderer = new IsometricTiledMapRenderer(tiledMap, 1f / textureRegion.getRegionWidth());
        terrain = new TerrainComponent(camera, tiledMap, renderer, 1f);
    }

    public void invokeTileMethod(RoomObject tileObject, GridPoint2 position, TiledMapTileLayer layer) {
        if (tileObject == null) {
            tileObject = floorPlan.getDefaultTileObject();
        }
        try {
            tileObject.getMethod().invoke(null, position, tileObject.getAssets(), layer);
        } catch (Exception e) {
            logger.error("Error invoking method {}", tileObject.getMethod().getName());
        }
    }

    public void invokeEntityMethod(RoomObject entityObject, GridPoint2 position) {
        if (entityObject == null) {
            return;
        }
        try {
            entityObject.getMethod().invoke(null, position, entityObject.getAssets());
        } catch (Exception e) {
            logger.error("Error invoking method {}", entityObject.getMethod().getName());
        }
    }

    public FloorPlan getHomeFloorPlan() {
        return floorPlan;
    }

    public String[] getAllRoomAssets(String extension) {
        Array<String> temp = new Array<>();
        temp.addAll(floorPlan.getDefaultTileObject().getAssets());
        for (FloorPlan.RoomPlan roomFloorPlan : floorPlan.getRoomMappings().values()) {
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