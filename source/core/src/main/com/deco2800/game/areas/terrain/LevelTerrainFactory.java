package com.deco2800.game.areas.terrain;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.areas.rooms.MapLevel;
import com.deco2800.game.areas.rooms.MapRoom;
import com.deco2800.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Factory for creating game terrains.
 */
public class LevelTerrainFactory {
    private static GridPoint2 MAP_SIZE;

    private final OrthographicCamera camera;
    private final TerrainOrientation orientation;

    private MapLevel mapLevel;
    private ArrayList<String> texturePaths;

    private final float TILE_SCALE = 1f;

    /**
     * Create a terrain factory for a level with isometric orientation.
     *
     * @param cameraComponent Camera to render terrains to. Must be orthographic.
     * @throws IOException
     * @throws FileNotFoundException
     */
    public LevelTerrainFactory(
            String mapFilePath,
            CameraComponent cameraComponent
    ) throws IOException, FileNotFoundException {
        this(mapFilePath, cameraComponent, TerrainOrientation.ISOMETRIC);
    }

    /**
     * Create a terrain factory for a level.
     *
     * @param cameraComponent Camera to render terrains to. Must be orthographic.
     * @param orientation     orientation to render terrain at
     * @throws IOException
     * @throws FileNotFoundException
     */
    public LevelTerrainFactory(
            String mapFilePath,
            CameraComponent cameraComponent,
            TerrainOrientation orientation
    ) throws IOException, FileNotFoundException {
        this.camera = (OrthographicCamera) cameraComponent.getCamera();
        this.orientation = orientation;

        // Generate room holder
        this.mapLevel = new MapLevel(mapFilePath);

        // Set map size
        this.MAP_SIZE = mapLevel.getMaxSize();

        // Set textures for assets
        this.texturePaths = mapLevel.getAllTexturePaths();
    }

    /**
     * Create a terrain of the given type, using the orientation of the factory. This can be extended
     * to add additional game terrains.
     *
     * @return Terrain component which renders the terrain
     */
    public TerrainComponent createTerrain() {
        ResourceService resourceService = ServiceLocator.getResourceService();

        // Load a dummy texture for tile size. Assumes all textures exist
        // TODO Find a better way to do this
        // TODO Validate texture existence in MapLevel
        TextureRegion sizer = new TextureRegion(resourceService.getAsset(
                mapLevel.getRooms().get(0).getBaseFloorTexture(),
                Texture.class
        ));

        GridPoint2 tileSize =
                new GridPoint2(sizer.getRegionWidth(), sizer.getRegionHeight());

        // Create tile map
        TiledMap tiledMap = createTiledMap(resourceService, tileSize);

        // Create renderer
        TiledMapRenderer renderer =
                createRenderer(tiledMap, TILE_SCALE / tileSize.x);

        return new TerrainComponent(
                camera, tiledMap, renderer, orientation, TILE_SCALE
        );
    }

    private TiledMap createTiledMap(
            ResourceService resourceService,
            GridPoint2 tileSize
    ) {
        TiledMap tiledMap = new TiledMap();

        // Create layer for tiles
        TiledMapTileLayer layer = new TiledMapTileLayer(
                MAP_SIZE.x, MAP_SIZE.y,
                tileSize.x, tileSize.y
        );

        // Populate layer with tiles
        for (MapRoom room : mapLevel.getRooms()) {
            createRoomTiles(resourceService, layer, room);
        }

        // Add the populated layer to the map
        tiledMap.getLayers().add(layer);

        return tiledMap;
    }

    private void createRoomTiles(
            ResourceService resourceService,
            TiledMapTileLayer layer,
            MapRoom room
    ) {
        HashMap<GridPoint2, TextureRegion> tRegions = new HashMap<>();
        HashMap<GridPoint2, TerrainTile> tTiles = new HashMap<>();

        /* Create texture regions */
        // Base floor
        TextureRegion baseRegion = new TextureRegion(
                resourceService.getAsset(room.getBaseFloorTexture(),
                        Texture.class
                ));

        // Extra textures
        room.getSymbolsToPaths().forEach((pos, path) -> {
            // Map a position to a texture region
            tRegions.put(pos, new TextureRegion(
                    resourceService.getAsset(path, Texture.class)));
        });

        /* Create terrain tiles */
        TerrainTile baseTile = new TerrainTile(baseRegion);

        tRegions.forEach((pos, region) -> {
            // Map a position to a terrain tile
            tTiles.put(pos, new TerrainTile(region));
        });

        /* Fill base tiles */
        GridPoint2[] bounds = room.getRoomBounds();
        fillTileRegion(layer, bounds[0], bounds[1], baseTile);

        /* Insert extra tiles */
        tTiles.forEach((pos, tile) -> {
            fillTile(layer, pos.add(room.getRoomPosition()), tile);
        });
    }


    private TiledMapRenderer createRenderer(TiledMap tiledMap, float tileScale) {
                return new IsometricTiledMapRenderer(tiledMap, tileScale);
    }


    private static void fillTile(
            TiledMapTileLayer layer,
            GridPoint2 pos,
            TerrainTile tile
    ) {
        Cell cell = layer.getCell(pos.x, pos.y);
        cell.setTile(tile);
    }


    private static void fillTileRegion(
            TiledMapTileLayer layer,
            GridPoint2 c1, GridPoint2 c2,
            TerrainTile tile
    ) {
        for (int x = c1.x; x < c2.x; x++) {
            for (int y = c1.y; y < c2.y; y++) {
                Cell cell = new Cell();
                cell.setTile(tile);
                layer.setCell(x, y, cell);
            }
        }
    }
}
