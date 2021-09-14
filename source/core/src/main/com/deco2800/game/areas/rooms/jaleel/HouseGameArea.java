package com.deco2800.game.areas.rooms.jaleel;

import com.badlogic.gdx.audio.Music;
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
import com.deco2800.game.events.EventHandler;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HouseGameArea extends GameArea {

    private static final Logger logger = LoggerFactory.getLogger(HouseGameArea.class);
    private final TerrainFactory terrainFactory;
    private EventHandler eventHandler;
    public Entity player;

    private String[] drmLocations = {"maps/s2/r1_jaleel.drm"};
    private Array<Room> rooms;

    public HouseGameArea(TerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
        this.eventHandler = new EventHandler();
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
            reader.checkDrmHeader(reader.getTileKey());
            Array<DrmObject> tileDefinitions = reader.extractDefinitions();
            Array<Array<String>> tileGrid = reader.extractGrid();
            // Get entity information in room
            reader.checkDrmHeader(reader.getEntityKey());
            Array<DrmObject> entityDefinitions = reader.extractDefinitions();
            Array<Array<String>> entityGrid = reader.extractGrid();

            rooms.add(new Room(roomScale, tileDefinitions, entityDefinitions, tileGrid, entityGrid));
        }
    }

    public void createRooms() {
        for (Room room : rooms) {
            // Tile generation
            terrain = terrainFactory.createRoomTerrain(room);
            spawnEntity(new Entity().addComponent(terrain));

            // Entity generation
            
        }
    }

    private Entity spawnPlayer(GridPoint2 gridPosition) {
        Entity newPlayer = PlayerFactory.createPlayer();
        spawnEntityAt(newPlayer, gridPosition, true, true);
        return newPlayer;
    }

    private void spawnBed(GridPoint2 gridPosition) {
        // Note: interactable objects must be created AFTER the player, as it requires the player
        // entity as an argument
        Entity bed = ObstacleFactory.createBed();
        spawnEntityAt(bed, gridPosition, true, true);
    }

    private void spawnMum(GridPoint2 gridPosition) {
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
            resourceService.loadTextures(room.getTileTextures());
            resourceService.loadTextures(room.getEntityTextures());
        }

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
    }

    @Override
    public void dispose() {
        super.dispose();
        this.unloadAssets();
    }
}
