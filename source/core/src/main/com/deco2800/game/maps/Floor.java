package com.deco2800.game.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.ObjectFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.maps.terrain.TerrainComponent;
import com.deco2800.game.maps.terrain.TerrainTile;
import com.deco2800.game.utils.math.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Game area representation of a single floor in a home.
 * Holds raw data for room allocation and non-room object generation.
 */
public class Floor extends GameArea implements Json.Serializable {

    private static final Logger logger = LoggerFactory.getLogger(Floor.class);
    private OrthographicCamera camera;
    private Entity player = null;
    private Entity cat = null;
    // Defined on deserialization
    private GridObject defaultInteriorTile;
    private GridObject defaultInteriorWall;
    private ObjectMap<Character, GridObject> tileMap;
    private ObjectMap<Character, GridObject> entityMap;
    private ObjectMap<Character, Room> roomMap;
    private Character[][] floorGrid;
    private GridPoint2 dimensions;
    // Defined on call for creation
    private boolean created = false;

    public void create() {
        if (!created) {
            for (Room room : new ObjectMap.Values<>(roomMap)) {
                room.create(this);
            }
            loadAssets();
            displayUI();
            spawnFloorTiles();
            spawnFloorEntities();
        }
        created = true;
    }

    /**
     * Allows rooms to spawn their tiles first, then will iterate through the entire floor grid
     * for miscellaneous tile spawning. Creates a new TerrainComponent and spawns it into the world.
     */
    private void spawnFloorTiles() {
        TextureRegion textureRegion = new TextureRegion(
                ServiceLocator.getResourceService().getAsset(defaultInteriorTile.getAssets()[0], Texture.class));

        TiledMapTileLayer layer = new TiledMapTileLayer(
                dimensions.x, dimensions.y,
                textureRegion.getRegionWidth(), textureRegion.getRegionHeight());

        // Spawn all room tiles for each room plan
        for (Room room : new ObjectMap.Values<>(roomMap)) {
            room.spawnRoomTiles(layer);
        }

        // Iterate entire grid to find non-room tiles
        for (int x = 0; x < floorGrid.length; x++) {
            for (int y = 0; y < floorGrid[x].length; y++) {
                Character symbol = floorGrid[x][y];
                GridObject floorTile = tileMap.get(symbol);
                if (floorTile != null) {
                    spawnGridTile(floorTile, new GridPoint2(x, y), layer);
                    continue;
                }
                GridObject floorEntity = entityMap.get(symbol);
                if (floorEntity != null && layer.getCell(x, y) == null) {
                    spawnGridTile(defaultInteriorTile, new GridPoint2(x, y), layer);
                }
            }
        }
        TiledMap tiledMap = new TiledMap();
        tiledMap.getLayers().add(layer);
        TiledMapRenderer renderer = new IsometricTiledMapRenderer(tiledMap, 1f / textureRegion.getRegionWidth());
        terrain = new TerrainComponent(camera, tiledMap, renderer,1f);
        spawnEntity(new Entity().addComponent(terrain));
    }

    /**
     * Creates player first to alleviate any dependencies.
     * Allow rooms to spawn their entities first, then will iterate through the entire floor grid
     * for miscellaneous entity spawning. Finally, spawns non-prefab defined entities into the world.
     */
    private void spawnFloorEntities() {

        // Create player entity for dependency injection
        createPlayer();

        // Spawn all room entities for each room interior
        for (ObjectMap.Entry<Character, Room> entry : new ObjectMap.Entries<>(roomMap)) {
            entry.value.spawnRoomEntities(entry.key);
        }

        // Spawn all non-room entities in floor plan
        for (int x = 0; x < floorGrid.length; x++) {
            for (int y = 0; y < floorGrid[x].length; y++) {
                Character symbol = floorGrid[x][y];
                GridObject entity = entityMap.get(symbol);
                if (entity != null) {
                    spawnGridEntity(entity, new GridPoint2(x, y));
                }
            }
        }

        // Spawn non-prefab defined entities
        spawnPlayer();
        spawnBorders();
        spawnCat();
    }

    /**
     * Creates the player entity. Does not spawn yet to account for static entity generation first.
     */
    private void createPlayer() {
        String[] playerAssets = new String[]{PlayerFactory.getAtlas()};
        ServiceLocator.getResourceService().loadTextureAtlases(playerAssets);
        ServiceLocator.getResourceService().loadAll();
        player = PlayerFactory.createPlayer(playerAssets);
    }

    /**
     * Spawns the player into the world. Will try to spawn on a non-entity living room tile,
     * otherwise a random non-entity tile in the world.
     */
    private void spawnPlayer() {
        GridPoint2 spawnLocation = null;
        // Iterate through rooms to find a valid spawn location
        for (Room room : new ObjectMap.Values<>(roomMap)) {
            Array<GridPoint2> roomSpawnLocations = room.getValidSpawnLocations();
            if (roomSpawnLocations != null) {
                spawnLocation = roomSpawnLocations.get(RandomUtils.getSeed().nextInt(roomSpawnLocations.size));
                break;
            }
        }
        // Set default spawn location if one was not found
        if (spawnLocation == null) {
            spawnLocation = new GridPoint2(1, 1);
        }

        spawnEntityAt(player, spawnLocation, true, true);
        player.getEvents().trigger("update_animation", "standing_south_normal");
    }

    /**
     * Spawns border walls into the world. These borders outline the map given by the floor grid
     */
    private void spawnBorders() {
        // Spawns north and south borders, left to right
        for (int x = -1; x < floorGrid.length + 1; x++) {
            Entity borderWall1 = ObjectFactory.createBaseObject(new String[0], BodyDef.BodyType.StaticBody);
            Entity borderWall2 = ObjectFactory.createBaseObject(new String[0], BodyDef.BodyType.StaticBody);
            spawnEntityAt(borderWall1, new GridPoint2(x, -1), true, true);
            spawnEntityAt(borderWall2, new GridPoint2(x, floorGrid[0].length), true, true);
        }
        // Spawns east and west borders, bottom to top
        for (int y = 0; y < floorGrid[0].length; y++) {
            Entity borderWall1 = ObjectFactory.createBaseObject(new String[0], BodyDef.BodyType.StaticBody);
            Entity borderWall2 = ObjectFactory.createBaseObject(new String[0], BodyDef.BodyType.StaticBody);
            spawnEntityAt(borderWall1, new GridPoint2(-1, y), true, true);
            spawnEntityAt(borderWall2, new GridPoint2(floorGrid.length, y), true, true);
        }
    }
    /**
     * Spawns the NPC Cat into map.
     */
    private void spawnCat(){
        String[] catAssets = new String[]{"images/characters/cat_00/cat_00.atlas"};
        ServiceLocator.getResourceService().loadTextureAtlases(catAssets);
        ServiceLocator.getResourceService().loadAll();
        cat = NPCFactory.createCat(catAssets);
        spawnEntityAt(cat, new GridPoint2(20,20), true, true);
    }

    /**
     * Invokes the method related to the tile.
     * @param tileObject instance containing the method and parameters
     * @param position world-related position
     * @param layer container for tile cells
     */
    public void spawnGridTile(GridObject tileObject, GridPoint2 position, TiledMapTileLayer layer) {
        if (tileObject == null) {
            tileObject = defaultInteriorTile;
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

    /**
     * Invokes the method related to the entity.
     * @param entityObject instance containing the method and parameters
     * @param position world-related position
     */
    public void spawnGridEntity(GridObject entityObject, GridPoint2 position) {
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

    public GridObject getDefaultInteriorTile() {
        return defaultInteriorTile;
    }

    public GridObject getDefaultInteriorWall() {
        return defaultInteriorWall;
    }

    public ObjectMap<Character, GridObject> getEntityMap() {
        return entityMap;
    }

    public Character[][] getFloorGrid() {
        return floorGrid;
    }

    public Entity getPlayer() {
        return player;
    }

    public Entity getCat() {
        return cat;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    private void displayUI() {
        // Add UI unique to this game area here
    }

    /**
     * @param extension specific extension for all assets returned
     * @return asset filenames from the floor plan to the individual objects
     */
    private String[] getAssets(String extension) {
        Array<String> temp = new Array<>();
        // Add default floor tile assets
        temp.addAll(defaultInteriorTile.getAssets(extension));
        // Add floor-level tile assets
        for (GridObject gridTile : new ObjectMap.Values<>(tileMap)) {
            temp.addAll(gridTile.getAssets(extension));
        }
        // Add floor-level entity assets
        for (GridObject gridEntity : new ObjectMap.Values<>(entityMap)) {
            temp.addAll(gridEntity.getAssets(extension));
        }
        // Add room-level assets
        for (Room room : new ObjectMap.Values<>(roomMap)) {
            temp.addAll(room.getAssets(extension));
        }
        // Convert to String[] type
        String[] assets = new String[temp.size];
        for (int i = 0; i < temp.size; i++) {
            assets[i] = temp.get(i);
        }
        return assets;
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(getAssets(".png"));
        resourceService.loadTextureAtlases(getAssets(".atlas"));

        while (!resourceService.loadForMillis(20)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(getAssets(".png"));
        resourceService.unloadAssets(getAssets(".atlas"));
    }

    @Override
    public void dispose() {
        super.dispose();
        this.unloadAssets();
    }

    @Override
    public void write(Json json) {
        json.writeObjectStart();
        json.writeValue("tileMap", tileMap);
        json.writeValue("entityMap", entityMap);
        json.writeValue("roomMap", roomMap);
        json.writeValue("floorGrid", floorGrid);
        json.writeObjectEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        try {
            JsonValue iterator = jsonData.child();
            FileLoader.assertJsonValueName(iterator, "defaultInteriorTile");
            defaultInteriorTile = new GridObject();
            defaultInteriorTile.read(json, iterator);

            iterator = iterator.next();
            FileLoader.assertJsonValueName(iterator, "defaultInteriorWall");
            defaultInteriorWall = new GridObject();
            defaultInteriorWall.read(json, iterator);

            iterator = iterator.next();
            tileMap = new ObjectMap<>();
            FileLoader.readCharacterObjectMap("tileMap", tileMap, GridObject.class, json, iterator);
            
            iterator = iterator.next();
            entityMap = new ObjectMap<>();
            FileLoader.readCharacterObjectMap("entityMap", entityMap, GridObject.class, json, iterator);

            iterator = iterator.next();
            roomMap = new ObjectMap<>();
            FileLoader.readCharacterObjectMap("roomMap", roomMap, Room.class, json, iterator);

            iterator = iterator.next();
            floorGrid = new Character[iterator.size][iterator.child().size];
            FileLoader.readCharacterGrid("floorGrid", floorGrid, iterator);

            dimensions = new GridPoint2(floorGrid[0].length, floorGrid.length);

            floorGrid = MatrixUtils.rotateClockwise(floorGrid);
            for (Room room : new ObjectMap.Values<>(roomMap)) {
                //noinspection SuspiciousNameCombination
                room.setOffset(new GridPoint2(room.getOffset().y, dimensions.y - room.getOffset().x - 1));
            }

            FileLoader.assertJsonValueNull(iterator.next());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}