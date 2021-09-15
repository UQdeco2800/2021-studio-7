package com.deco2800.game.areas.rooms;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.areas.components.GameAreaDisplay;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class HouseGameArea extends GameArea {

    private static final Logger logger = LoggerFactory.getLogger(HouseGameArea.class);
    private final TerrainFactory terrainFactory;
    public Entity player;

    private final String[] drmLocations = {"maps/s2/r2_jaleel.drm"};
    private final String[] houseTextureAtlases = {
            "images/characters/boy_01/boy_01.atlas",
            "images/characters/mum_01/mum_01.atlas",
            "images/objects/bed/bed.atlas"
    };
    private Array<Room> rooms;

    public HouseGameArea(TerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
        this.rooms = new Array<>();
    }

    @Override
    public void create() {
        extractRooms();
        loadAssets();
        displayUI();
        createRooms();
    }

    public void extractRooms() {
        for (String drmLocation : drmLocations) {
            RoomReader reader = new RoomReader(drmLocation, FileLoader.Location.INTERNAL);
            // Get room scale
            Vector2 roomScale = reader.extractRoomScale();
            // Get tile information in room
            Array<DrmObject> tileDefinitions = reader.extractDefinitions(reader.getTileKey());
            String[][] tileGrid = reader.extractGrid(roomScale);
            // Get entity information in room
            Array<DrmObject> entityDefinitions = reader.extractDefinitions(reader.getEntityKey());
            String[][] entityGrid = reader.extractGrid(roomScale);

            rooms.add(new Room(roomScale, tileDefinitions, entityDefinitions, tileGrid, entityGrid));
        }
    }

    public void createRooms() {
        for (Room room : rooms) {
            // Tile generation
            terrain = terrainFactory.createRoomTerrain(room);
            spawnEntity(new Entity().addComponent(terrain));

            // Entity generation
            spawnEntities(room);
        }
    }

    public void spawnEntities(Room room) {
        Map<String, DrmObject> symbolDrmObjectMap = room.getSymbolObjectMap();
        for (int x = 0; x < room.getMaxScale(); x++) {
            for (int y = 0; y < room.getMaxScale(); y++) {
                String current = room.getEntityGrid()[x][y];
                DrmObject drmObject = symbolDrmObjectMap.get(current);
                if (drmObject == null) {
                    continue;
                }
                Object[] params;
                if (drmObject.getTexture().equals("")) {
                    params = new Object[]{new GridPoint2(x, y)};
                } else {
                    params = new Object[]{new GridPoint2(x, y), drmObject.getTexture()};
                }
                try {
                    drmObject.getMethod().invoke(this, params);
                } catch (InvocationTargetException e) {
                    logger.error("Couldn't invoke object spawn method");
                } catch (IllegalAccessException e) {
                    logger.error("No access to invoked spawn method");
                }
            }
        }
    }

    /**
     * Invoked from drmObject in spawnEntities()
     *
     * @param gridPosition position on the world from file
     * @param texture wall texture from file
     */
    public void spawnWall(GridPoint2 gridPosition, String texture) {
//        Entity newWall = ObstacleFactory.createWall(1f, 1f, texture);
//        spawnEntityAt(newWall, gridPosition, true, true);
    }

    /**
     * Invoked from drmObject in spawnEntities(), spawns Player entity
     *
     * @param gridPosition position on the world from file
     */
    public void spawnPlayer(GridPoint2 gridPosition) {
        Entity newPlayer = PlayerFactory.createPlayer();
        spawnEntityAt(newPlayer, gridPosition, true, true);
        player = newPlayer;
    }

    /**
     * Invoked from drmObject in spawnEntities(), spawns Bed entity
     *
     * @param gridPosition position on the world from file
     */
    public void spawnBed(GridPoint2 gridPosition) {
        Entity bed = ObstacleFactory.createBed();
        spawnEntityAt(bed, gridPosition, true, true);
    }

    /**
     * Invoked from drmObject in spawnEntities(), spawns Mum entity
     *
     * @param gridPosition position on the world from file
     */
    public void spawnMum(GridPoint2 gridPosition) {
        Entity mum = NPCFactory.createMum(player);
        spawnEntityAt(mum, gridPosition, true, true);
    }

    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Box Forest"));
        spawnEntity(ui);
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();

        for (Room room : rooms) {
            for (DrmObject current : room.getTileDefinitions()) {
                if (current.getTexture() != null) {
                    resourceService.loadTexture(current.getTexture());
                }
            }
            for (DrmObject current : room.getEntityDefinitions()) {
                if (current.getTexture() != null) {
                    resourceService.loadTexture(current.getTexture());
                }
            }
        }
        resourceService.loadTextureAtlases(houseTextureAtlases);

        while (!resourceService.loadForMillis(10)) {
        // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        for (Room room : rooms) {
            resourceService.unloadAssets(room.getTileTextures());
            resourceService.unloadAssets(room.getEntityTextures());
        }
        resourceService.unloadAssets(houseTextureAtlases);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.unloadAssets();
    }
}
