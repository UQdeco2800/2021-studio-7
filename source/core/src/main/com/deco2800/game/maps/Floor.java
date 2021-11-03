package com.deco2800.game.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.maps.terrain.TerrainComponent;
import com.deco2800.game.maps.terrain.TerrainTile;
import com.deco2800.game.utils.math.MatrixUtils;
import com.deco2800.game.utils.math.RandomUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Game area representation of a single floor in a home.
 * Holds raw data for room allocation and non-room object generation.
 */
public class Floor extends RetroactiveArea implements Json.Serializable {
    // Defined on deserialization
    private ObjectDescription defaultInteriorTile;
    private ObjectDescription defaultExteriorTile;
    private ObjectDescription defaultWall;
    private ObjectMap<Character, ObjectDescription> tileMap;
    private ObjectMap<Character, ObjectDescription> entityMap;
    private ObjectMap<Character, Room> roomMap;
    private Character[][] floorGrid;
    private GridPoint2 dimensions;
    // Defined on initialisation
    private Home home;
    private GridPoint2 mumPosition;
    private final List<GridPoint2> bedPositions = new ArrayList<>();

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
            ServiceLocator.getResourceService().getAsset(defaultInteriorTile.getData().getAssets()[0], Texture.class));

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
                ObjectDescription tileDesc = tileMap.get(symbol);
                if (tileDesc != null) {
                    createGridTile(tileDesc, new GridPoint2(x, y), layer);
                    continue;
                }
                ObjectDescription entityDesc = entityMap.get(symbol);
                if (entityDesc != null && layer.getCell(x, y) == null) {
                    createGridTile(defaultExteriorTile, new GridPoint2(x, y), layer);
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
                ObjectDescription entityDesc = entityMap.get(symbol);
                if (entityDesc != null) {
                    createGridEntity(entityDesc, new GridPoint2(x, y));
                }
            }
        }

        // Create non-prefab defined entities
        createPlayer();
        createBorders();
        createCat();
        createBeds();
    }

    /**
     * Invokes the method related to the tile.
     *
     * @param tileDesc instance containing the method, entity parameters and number of rotations
     * @param worldPos world-related position
     * @param layer    container for tile cells
     */
    public void createGridTile(ObjectDescription tileDesc, GridPoint2 worldPos, TiledMapTileLayer layer) {
        if (tileDesc == null) {
            tileDesc = defaultInteriorTile;
        }
        try {
            TerrainTile tile = (TerrainTile) tileDesc.getData().getMethod().invoke(null, tileDesc, worldPos);
            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            cell.setTile(tile);
            layer.setCell(worldPos.x, worldPos.y, cell);
        } catch (Exception e) {
            logger.error("Error invoking method {}", tileDesc.getData().getMethod().getName());
        }
    }

    /**
     * Invokes the method related to the entity.
     *
     * @param entityDesc instance containing the method, entity parameters and number of rotations
     * @param worldPos   world-related position
     */
    public void createGridEntity(ObjectDescription entityDesc, GridPoint2 worldPos) {
        if (entityDesc == null) {
            return;
        }
        try {
            Entity entity = (Entity) entityDesc.getData().getMethod().invoke(null, entityDesc, worldPos);
            createEntityAt(entity, worldPos, true, true);
        } catch (Exception e) {
            logger.error("Error invoking {} creation", Home.getObjectName(entityDesc.getData()));
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
        ObjectDescription invisibleDesc = new ObjectDescription(Home.getObject("misc_invisible_0"), 0);
        // Creates north and south borders, left to right
        for (int x = -1; x < floorGrid.length + 1; x++) {
            createGridEntity(invisibleDesc, new GridPoint2(x, -1));
            createGridEntity(invisibleDesc, new GridPoint2(x, floorGrid[0].length));
        }
        // Creates east and west borders, bottom to top
        for (int y = 0; y < floorGrid[0].length; y++) {
            createGridEntity(invisibleDesc, new GridPoint2(-1, y));
            createGridEntity(invisibleDesc, new GridPoint2(floorGrid.length, y));
        }
    }

    private void createCat() {
        createGridEntity(new ObjectDescription(Home.getObject("npc_cat_0"), 0), new GridPoint2(20, 20));
    }

    public void createMum() {
        if (mumPosition == null) {
            mumPosition = new GridPoint2(0, 0);
        }
        createGridEntity(new ObjectDescription(Home.getObject("npc_mum_0"), 0), mumPosition);
    }

    private void createBeds() {
        ObjectData playerBed = Home.getObject("interactive_bed_0");
        ObjectData normalBed = Home.getObject("object_bed_0");
        if (playerBed == null || normalBed == null) {
            throw new NullPointerException("Player or normal bed objects couldn't be retrieved");
        }
        playerBed.setNullMethod();
        normalBed.setNullMethod();

        GridPoint2 playerBedPosition = bedPositions.get(RandomUtils.getSeed().nextInt(bedPositions.size()));
        createGridEntity(new ObjectDescription(playerBed, 0), playerBedPosition);

        bedPositions.remove(playerBedPosition);
        for (GridPoint2 normalBedPosition : bedPositions) {
            createGridEntity(new ObjectDescription(normalBed, 0), normalBedPosition);
        }
        bedPositions.add(playerBedPosition);
    }

    public ObjectDescription getDefaultWall() {
        return defaultWall;
    }

    public ObjectMap<Character, ObjectDescription> getTileMap() {
        return tileMap;
    }

    public ObjectMap<Character, ObjectDescription> getEntityMap() {
        return entityMap;
    }

    public ObjectMap<Character, Room> getRoomMap() {
        return roomMap;
    }

    public Character[][] getFloorGrid() {
        return floorGrid;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public void stashBedPosition(GridPoint2 worldPos) {
        bedPositions.add(worldPos);
    }

    public void stashMumPosition(GridPoint2 worldPos) {
        mumPosition = worldPos;
    }

    @Override
    public void createUI() {
        // Add UI here
    }

    @Override
    public void write(Json json) {
        // No purpose yet
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        try {
            JsonValue iterator = jsonData.child();
            FileLoader.assertJsonValueName(iterator, "defaultInteriorTile");
            String[] desc = iterator.asStringArray();
            defaultInteriorTile = new ObjectDescription(Home.getObject(desc[0]), Integer.parseInt(desc[1]));

            iterator = iterator.next();
            FileLoader.assertJsonValueName(iterator, "defaultExteriorTile");
            desc = iterator.asStringArray();
            defaultExteriorTile = new ObjectDescription(Home.getObject(desc[0]), Integer.parseInt(desc[1]));

            iterator = iterator.next();
            FileLoader.assertJsonValueName(iterator, "defaultWall");
            desc = iterator.asStringArray();
            defaultWall = new ObjectDescription(Home.getObject(desc[0]), Integer.parseInt(desc[1]));

            iterator = iterator.next();
            tileMap = new ObjectMap<>();
            FileLoader.readCharacterObjectNameMap("tileMap", tileMap, iterator);

            iterator = iterator.next();
            entityMap = new ObjectMap<>();
            FileLoader.readCharacterObjectNameMap("entityMap", entityMap, iterator);

            iterator = iterator.next();
            roomMap = new ObjectMap<>();
            FileLoader.assertJsonValueName(iterator, "roomMap");
            JsonValue subIterator = iterator.child();
            while (subIterator != null) {
                Room room = new Room();
                room.read(json, subIterator);
                roomMap.put(subIterator.name().charAt(0), room);
                subIterator = subIterator.next();
            }

            iterator = iterator.next();
            floorGrid = new Character[iterator.size][iterator.child().size];
            FileLoader.readCharacterGrid("floorGrid", floorGrid, iterator);

            dimensions = new GridPoint2(floorGrid[0].length, floorGrid.length);

            floorGrid = MatrixUtils.rotateClockwise(floorGrid);
            for (Room room : new ObjectMap.Values<>(roomMap)) {
                //noinspection SuspiciousNameCombination
                room.setOffset(new GridPoint2(room.getOffset().y, dimensions.y - room.getOffset().x - 1));
                room.setFloor(this);
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
            Objects.equals(defaultWall, floor.defaultWall) &&
            Objects.equals(tileMap, floor.tileMap) &&
            Objects.equals(entityMap, floor.entityMap) &&
            Objects.equals(roomMap, floor.roomMap) &&
            Arrays.deepEquals(floorGrid, floor.floorGrid) &&
            Objects.equals(dimensions, floor.dimensions);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(defaultInteriorTile, defaultWall, tileMap, entityMap, roomMap, dimensions);
        result = 31 * result + Arrays.deepHashCode(floorGrid);
        return result;
    }
}