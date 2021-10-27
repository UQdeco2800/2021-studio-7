package com.deco2800.game.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.entities.factories.ObjectFactory;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.maps.terrain.TerrainComponent;
import com.deco2800.game.maps.terrain.TerrainTile;
import com.deco2800.game.utils.math.MatrixUtils;
import com.deco2800.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Game area representation of a single floor in a home.
 * Holds raw data for room allocation and non-room object generation.
 */
public class Floor extends RetroactiveArea implements Json.Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Floor.class);
    private static final String PLAYER_BED_ATLAS = "images/objects/bed/bed_animation.atlas";
    private static final String NORMAL_BED_TEXTURE = "images/objects/furniture/newBed.png";
    private Home home;
    private Entity cat = null;
    private final List<GridPoint2> bedPositions = new ArrayList<>();
    // Defined on deserialization
    private GridObject defaultInteriorTile;
    private GridObject defaultInteriorWall;
    private ObjectMap<Character, GridObject> tileMap;
    private ObjectMap<Character, GridObject> entityMap;
    private ObjectMap<Character, Room> roomMap;
    private Character[][] floorGrid;
    private GridPoint2 dimensions;

    public Floor() {
    }

    public Floor(GridObject defaultInteriorTile, GridObject defaultInteriorWall,
                 ObjectMap<Character, GridObject> tileMap, ObjectMap<Character, GridObject> entityMap,
                 ObjectMap<Character, Room> roomMap, Character[][] floorGrid, GridPoint2 dimensions) {
        this.defaultInteriorTile = defaultInteriorTile;
        this.defaultInteriorWall = defaultInteriorWall;
        this.tileMap = tileMap;
        this.entityMap = entityMap;
        this.roomMap = roomMap;
        this.floorGrid = floorGrid;
        this.dimensions = dimensions;
    }

    public void initialise() {
        for (Room room : new ObjectMap.Values<>(roomMap)) {
            room.initialise();
        }
    }

    public void create() {
        createUI();
        createAllTiles();
        createAllEntities();
    }

    /**
     * Allows rooms to create their tiles first, then will iterate through the entire floor grid
     * for miscellaneous tile creating. Creates a new TerrainComponent and creates it into the world.
     */
    private void createAllTiles() {
        TextureRegion textureRegion = new TextureRegion(
            ServiceLocator.getResourceService().getAsset(defaultInteriorTile.getAssets()[0], Texture.class));

        TiledMapTileLayer layer = new TiledMapTileLayer(
            dimensions.x, dimensions.y,
            textureRegion.getRegionWidth(), textureRegion.getRegionHeight());

        // Create all room tiles for each room plan
        for (Room room : new ObjectMap.Values<>(roomMap)) {
            room.createRoomTiles(layer);
        }

        // Iterate entire grid to find non-room tiles
        for (int x = 0; x < floorGrid.length; x++) {
            for (int y = 0; y < floorGrid[x].length; y++) {
                Character symbol = floorGrid[x][y];
                GridObject floorTile = tileMap.get(symbol);
                if (floorTile != null) {
                    createGridTile(floorTile, new GridPoint2(x, y), layer);
                    continue;
                }
                GridObject floorEntity = entityMap.get(symbol);
                if (floorEntity != null && layer.getCell(x, y) == null) {
                    createGridTile(defaultInteriorTile, new GridPoint2(x, y), layer);
                }
            }
        }

        TiledMap tiledMap = new TiledMap();
        tiledMap.getLayers().add(layer);
        TiledMapRenderer renderer = new IsometricTiledMapRenderer(tiledMap, 1f / textureRegion.getRegionWidth());
        terrain = new TerrainComponent(
            (OrthographicCamera) home.getScreen().getCameraComponent().getCamera(), tiledMap, renderer, 1f);
        createEntity(new Entity().addComponent(terrain));
    }

    /**
     * Creates player first to alleviate any dependencies.
     * Allow rooms to create their entities first, then will iterate through the entire floor grid
     * for miscellaneous entity creating. Finally, creates non-prefab defined entities into the world.
     */
    private void createAllEntities() {
        // Create all room entities for each room interior
        for (Room room : new ObjectMap.Values<>(roomMap)) {
            room.createRoomEntities();
        }

        // Create all non-room entities in floor plan
        for (int x = 0; x < floorGrid.length; x++) {
            for (int y = 0; y < floorGrid[x].length; y++) {
                Character symbol = floorGrid[x][y];
                GridObject entity = entityMap.get(symbol);
                if (entity != null) {
                    createGridEntity(entity, new GridPoint2(x, y));
                }
            }
        }

        // Create non-prefab defined entities
        createPlayer();
        createBorders();
        createCat();
        createMum();
        createBeds();
    }

    /**
     * Invokes the method related to the tile.
     *
     * @param tileObject instance containing the method and parameters
     * @param position   world-related position
     * @param layer      container for tile cells
     */
    public void createGridTile(GridObject tileObject, GridPoint2 position, TiledMapTileLayer layer) {
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
     *
     * @param entityObject instance containing the method and parameters
     * @param position     world-related position
     */
    public void createGridEntity(GridObject entityObject, GridPoint2 position) {
        if (entityObject == null) {
            return;
        }
        try {
            Entity entity = (Entity) entityObject.getMethod().invoke(null, (Object) entityObject.getAssets());
            createEntityAt(entity, position, true, true);
        } catch (Exception e) {
            logger.error("Error invoking method {}", entityObject.getMethod().getName());
        }
    }

    /**
     * Creates the player into the world. Will try to create on a non-entity living room tile,
     * otherwise a random non-entity tile in the world.
     */
    private void createPlayer() {
        GridPoint2 createLocation = null;
        // Iterate through rooms to find a valid creation location
        for (Room room : new ObjectMap.Values<>(roomMap)) {
            List<GridPoint2> roomCreateLocations = room.getValidCreateLocations();
            if (!roomCreateLocations.isEmpty()) {
                createLocation = roomCreateLocations.get(RandomUtils.getSeed().nextInt(roomCreateLocations.size()));
                break;
            }
        }
        // Set default create location if one was not found
        if (createLocation == null) {
            createLocation = new GridPoint2(1, 1);
        }


        createEntityAt(home.getScreen().getPlayer(), createLocation, true, true);
        home.getScreen().getPlayer().getEvents().trigger("update_animation", "standing_south_normal");
    }

    /**
     * Creates border walls into the world. These borders outline the map given by the floor grid
     */
    private void createBorders() {
        String[] boarderspec = {"", "0"};
        // Creates north and south borders, left to right
        for (int x = -1; x < floorGrid.length + 1; x++) {
            Entity borderWall1 = ObjectFactory.createBaseObject(boarderspec);
            Entity borderWall2 = ObjectFactory.createBaseObject(boarderspec);
            createEntityAt(borderWall1, new GridPoint2(x, -1), true, true);
            createEntityAt(borderWall2, new GridPoint2(x, floorGrid[0].length), true, true);
        }
        // Creates east and west borders, bottom to top
        for (int y = 0; y < floorGrid[0].length; y++) {
            Entity borderWall1 = ObjectFactory.createBaseObject(boarderspec);
            Entity borderWall2 = ObjectFactory.createBaseObject(boarderspec);
            createEntityAt(borderWall1, new GridPoint2(-1, y), true, true);
            createEntityAt(borderWall2, new GridPoint2(floorGrid.length, y), true, true);
        }
    }

    /**
     * Creates the NPC Cat into map.
     */
    private void createCat() {
        String catAtlas = "images/characters/cat_00/cat_00.atlas";
        ServiceLocator.getResourceService().loadAsset(catAtlas, TextureAtlas.class);
        ServiceLocator.getResourceService().loadAll();
        createEntityAt(NPCFactory.createCat(new String[]{catAtlas}),
            new GridPoint2(20, 20), true, true);
    }

    /**
     * Creates the mum into the game
     */
    private void createMum() {
        String mumAtlas = "images/characters/mum_01/mum_01.atlas";
        ServiceLocator.getResourceService().loadAsset(mumAtlas, TextureAtlas.class);
        ServiceLocator.getResourceService().loadAll();
        createEntityAt(NPCFactory.createMum(new String[]{mumAtlas}),
            new GridPoint2(24, 0), true, true);
    }

    private void createBeds() {
        GridObject playerBed;
        GridObject normalBed;
        try {
            playerBed = new GridObject(ObjectFactory.class.getMethod("createPlayerBed", String[].class),
                new String[]{PLAYER_BED_ATLAS, "0", "4", "0"});
            normalBed = new GridObject(ObjectFactory.class.getMethod("createNormalBed", String[].class),
                new String[]{NORMAL_BED_TEXTURE, "0", "0", "0"});
        } catch (NoSuchMethodException e) {
            throw new NullPointerException("Could not retrieve either createNormalBed or createPlayerBed methods");
        }

        GridPoint2 playerBedPosition = bedPositions.get(RandomUtils.getSeed().nextInt(bedPositions.size()));
        createGridEntity(playerBed, playerBedPosition);

        bedPositions.remove(playerBedPosition);
        for (GridPoint2 normalBedPosition : bedPositions) {
            createGridEntity(normalBed, normalBedPosition);
        }
        bedPositions.add(playerBedPosition);
    }

    public void stashBedPosition(GridPoint2 worldPos) {
        bedPositions.add(worldPos);
    }

    public GridObject getDefaultInteriorTile() {
        return defaultInteriorTile;
    }

    public void setDefaultInteriorTile(GridObject defaultInteriorTile) {
        this.defaultInteriorTile = defaultInteriorTile;
    }

    public GridObject getDefaultInteriorWall() {
        return defaultInteriorWall;
    }

    public void setDefaultInteriorWall(GridObject defaultInteriorWall) {
        this.defaultInteriorWall = defaultInteriorWall;
    }

    public ObjectMap<Character, GridObject> getEntityMap() {
        return entityMap;
    }

    public ObjectMap<Character, Room> getRoomMap() {
        return roomMap;
    }

    public Character[][] getFloorGrid() {
        return floorGrid;
    }

    public Entity getCat() {
        return cat;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    @Override
    public void createUI() {
        // Add UI here
    }

    @Override
    public void loadAssets() {
        logger.debug("        Loading floor assets");

        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadAsset(NORMAL_BED_TEXTURE, Texture.class);
        resourceService.loadAsset(PLAYER_BED_ATLAS, TextureAtlas.class);
        defaultInteriorTile.loadAssets();
        defaultInteriorWall.loadAssets();
        for (GridObject tile : new ObjectMap.Values<>(tileMap)) {
            tile.loadAssets();
        }
        for (GridObject entity : new ObjectMap.Values<>(entityMap)) {
            entity.loadAssets();
        }
        for (Room room : new ObjectMap.Values<>(roomMap)) {
            room.loadAssets();
        }

        while (!resourceService.loadForMillis(20)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    @Override
    public void unloadAssets() {
        logger.debug("        Unloading floor assets");

        ServiceLocator.getResourceService().unloadAsset(NORMAL_BED_TEXTURE);
        ServiceLocator.getResourceService().unloadAsset(PLAYER_BED_ATLAS);
        defaultInteriorTile.unloadAssets();
        defaultInteriorWall.unloadAssets();
        for (GridObject tile : new ObjectMap.Values<>(tileMap)) {
            tile.unloadAssets();
        }
        for (GridObject entity : new ObjectMap.Values<>(entityMap)) {
            entity.unloadAssets();
        }
        for (Room room : new ObjectMap.Values<>(roomMap)) {
            room.unloadAssets();
        }
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
            for (ObjectMap.Entry<Character, Room> entry : new ObjectMap.Entries<>(roomMap)) {
                //noinspection SuspiciousNameCombination
                entry.value.setOffset(new GridPoint2(
                    entry.value.getOffset().y, dimensions.y - entry.value.getOffset().x - 1));
                entry.value.setFloor(this);
            }

            FileLoader.assertJsonValueNull(iterator.next());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Floor floor = (Floor) o;
        return Objects.equals(defaultInteriorTile, floor.defaultInteriorTile) &&
            Objects.equals(defaultInteriorWall, floor.defaultInteriorWall) &&
            Objects.equals(tileMap, floor.tileMap) &&
            Objects.equals(entityMap, floor.entityMap) &&
            Objects.equals(roomMap, floor.roomMap) &&
            Arrays.deepEquals(floorGrid, floor.floorGrid) &&
            Objects.equals(dimensions, floor.dimensions);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(defaultInteriorTile, defaultInteriorWall, tileMap, entityMap, roomMap, dimensions);
        result = 31 * result + Arrays.deepHashCode(floorGrid);
        return result;
    }
}