package com.deco2800.game.areas.terrain;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.areas.home.roomtypes.RoomType;
import com.deco2800.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.deco2800.game.entities.components.player.CameraComponent;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;

/** Factory for creating game terrains. */
public class TerrainFactory {
  public static final GridPoint2 MAP_SIZE = new GridPoint2(20, 20);
  private static final int TUFT_TILE_COUNT = 30;
  private static final int ROCK_TILE_COUNT = 30;

  private final OrthographicCamera camera;
  private final TerrainOrientation orientation;

  /**
   * Create a terrain factory with Orthogonal orientation
   *
   * @param cameraComponent Camera to render terrains to. Must be ortographic.
   */
  public TerrainFactory(CameraComponent cameraComponent) {
    this(cameraComponent, TerrainOrientation.ORTHOGONAL);
  }

  /**
   * Create a terrain factory
   *
   * @param cameraComponent Camera to render terrains to. Must be orthographic.
   * @param orientation orientation to render terrain at
   */
  public TerrainFactory(CameraComponent cameraComponent, TerrainOrientation orientation) {
    this.camera = (OrthographicCamera) cameraComponent.getCamera();
    this.orientation = orientation;
  }

  private TiledMapRenderer createRenderer(TiledMap tiledMap, float tileScale) {
    switch (orientation) {
      case ORTHOGONAL:
        return new OrthogonalTiledMapRenderer(tiledMap, tileScale);
      case ISOMETRIC:
        return new IsometricTiledMapRenderer(tiledMap, tileScale);
      case HEXAGONAL:
        return new HexagonalTiledMapRenderer(tiledMap, tileScale);
      default:
        return null;
    }
  }

  /**
   * Create a terrain of the given type, using the orientation of the factory. This can be extended
   * to add additional game terrains.
   *
   * @return Terrain component which renders the terrain
   */
  public TerrainComponent createRoomTerrain(RoomType roomType) {
    ResourceService resourceService = ServiceLocator.getResourceService();
    TextureRegion textureRegion = new TextureRegion(resourceService.getAsset(roomType.getTileTextures()[0], Texture.class));

    GridPoint2 tilePixelSize = new GridPoint2(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
    TiledMap tiledMap = setRoomTiles(tilePixelSize, roomType);
    TiledMapRenderer renderer = createRenderer(tiledMap, 1f / tilePixelSize.x);
    return new TerrainComponent(camera, tiledMap, renderer, orientation, 1f);
  }

  private TiledMap setRoomTiles(GridPoint2 tileSize, RoomType roomType) {
    TiledMap tiledMap = new TiledMap();
    ObjectMap<String, TerrainTile> stringTerrainTileMap = roomType.getSymbolTerrainTileMap();
    TiledMapTileLayer layer = new TiledMapTileLayer(
            roomType.getMaxScale(), roomType.getMaxScale(), tileSize.x, tileSize.y);

    // Go through grid and set tile cells
    for (int x = 0; x < roomType.getMaxScale(); x++) {
      for (int y = 0; y < roomType.getMaxScale(); y++) {
        String current = roomType.getTileGrid()[x][y];
        TerrainTile tile = stringTerrainTileMap.get(current);
        if (tile != null) {
          Cell cell = new Cell();
          cell.setTile(tile);
          layer.setCell(x, y, cell);
        }
      }
    }

    tiledMap.getLayers().add(layer);
    return tiledMap;
  }
}
