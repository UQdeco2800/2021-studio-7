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
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.areas.rooms.jaleel.DrmObject;
import com.deco2800.game.areas.rooms.jaleel.Room;
import com.deco2800.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.deco2800.game.entities.components.player.CameraComponent;
import com.deco2800.game.utils.math.RandomUtils;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/** Factory for creating game terrains. */
public class TerrainFactory {
  private static final GridPoint2 MAP_SIZE = new GridPoint2(30, 30);
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

  /**
   * Create a terrain of the given type, using the orientation of the factory. This can be extended
   * to add additional game terrains.
   *
   * @param terrainType Terrain to create
   * @return Terrain component which renders the terrain
   */
  public TerrainComponent createTerrain(TerrainType terrainType) {
    ResourceService resourceService = ServiceLocator.getResourceService();
    switch (terrainType) {
      case FOREST_DEMO:
        TextureRegion orthoGrass =
            new TextureRegion(resourceService.getAsset("images/tiles/ortho/ortho_grass_1.png", Texture.class));
        TextureRegion orthoTuft =
            new TextureRegion(resourceService.getAsset("images/tiles/ortho/ortho_grass_2.png", Texture.class));
        TextureRegion orthoRocks =
            new TextureRegion(resourceService.getAsset("images/tiles/ortho/ortho_grass_3.png", Texture.class));
        return createForestDemoTerrain(0.5f, orthoGrass, orthoTuft, orthoRocks);
      case FOREST_DEMO_ISO:
        TextureRegion isoGrass =
            new TextureRegion(resourceService.getAsset("images/tiles/iso/iso_grass_1.png", Texture.class));
        TextureRegion isoTuft =
            new TextureRegion(resourceService.getAsset("images/tiles/iso/iso_grass_2.png", Texture.class));
        TextureRegion isoRocks =
            new TextureRegion(resourceService.getAsset("images/tiles/iso/iso_grass_3.png", Texture.class));
        return createForestDemoTerrain(1f, isoGrass, isoTuft, isoRocks);
      case FOREST_DEMO_HEX:
        TextureRegion hexGrass =
            new TextureRegion(resourceService.getAsset("images/tiles/hex/hex_grass_1.png", Texture.class));
        TextureRegion hexTuft =
            new TextureRegion(resourceService.getAsset("images/tiles/hex/hex_grass_2.png", Texture.class));
        TextureRegion hexRocks =
            new TextureRegion(resourceService.getAsset("images/tiles/hex/hex_grass_3.png", Texture.class));
        return createForestDemoTerrain(1f, hexGrass, hexTuft, hexRocks);
      default:
        return null;
    }
  }

  private TerrainComponent createForestDemoTerrain(
      float tileWorldSize, TextureRegion grass, TextureRegion grassTuft, TextureRegion rocks) {
    GridPoint2 tilePixelSize = new GridPoint2(grass.getRegionWidth(), grass.getRegionHeight());
    TiledMap tiledMap = createForestDemoTiles(tilePixelSize, grass, grassTuft, rocks);
    TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize.x);
    return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
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

  private TiledMap createForestDemoTiles(
      GridPoint2 tileSize, TextureRegion grass, TextureRegion grassTuft, TextureRegion rocks) {
    TiledMap tiledMap = new TiledMap();
    TerrainTile grassTile = new TerrainTile(grass);
    TerrainTile grassTuftTile = new TerrainTile(grassTuft);
    TerrainTile rockTile = new TerrainTile(rocks);
    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);

    // Create base grass
    fillTiles(layer, MAP_SIZE, grassTile);

    // Add some grass and rocks
    fillTilesAtRandom(layer, MAP_SIZE, grassTuftTile, TUFT_TILE_COUNT);
    fillTilesAtRandom(layer, MAP_SIZE, rockTile, ROCK_TILE_COUNT);

    tiledMap.getLayers().add(layer);
    return tiledMap;
  }

  private static void fillTilesAtRandom(
      TiledMapTileLayer layer, GridPoint2 mapSize, TerrainTile tile, int amount) {
    GridPoint2 min = new GridPoint2(0, 0);
    GridPoint2 max = new GridPoint2(mapSize.x - 1, mapSize.y - 1);

    for (int i = 0; i < amount; i++) {
      GridPoint2 tilePos = RandomUtils.random(min, max);
      Cell cell = layer.getCell(tilePos.x, tilePos.y);
      cell.setTile(tile);
    }
  }

  private static void fillTiles(TiledMapTileLayer layer, GridPoint2 mapSize, TerrainTile tile) {
    for (int x = 0; x < mapSize.x; x++) {
      for (int y = 0; y < mapSize.y; y++) {
        Cell cell = new Cell();
        cell.setTile(tile);
        layer.setCell(x, y, cell);
      }
    }
  }

  /**
   * Create a terrain of the given type, using the orientation of the factory. This can be extended
   * to add additional game terrains.
   *
   * @return Terrain component which renders the terrain
   */
  public TerrainComponent createRoomTerrain(Room room) {
    ResourceService resourceService = ServiceLocator.getResourceService();
    TextureRegion textureRegion = new TextureRegion(resourceService.getAsset(room.getTileTextures()[0], Texture.class));

    GridPoint2 tilePixelSize = new GridPoint2(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
    TiledMap tiledMap = createRoomDemoTiles(tilePixelSize, room);
    TiledMapRenderer renderer = createRenderer(tiledMap, 1f / tilePixelSize.x);
    return new TerrainComponent(camera, tiledMap, renderer, orientation, 1f);
  }

  private TiledMap createRoomDemoTiles(GridPoint2 tileSize, Room room) {
    TiledMap tiledMap = new TiledMap();
    Map<String, TerrainTile> stringTerrainTileMap = createStringTerrainTileMap(room);
    TiledMapTileLayer layer = new TiledMapTileLayer((int) room.getRoomScale().x, (int)room.getRoomScale().y, tileSize.x, tileSize.y);

    // Go through grid and set tile cells
    for (int i = 0; i < room.getTileGrid().size; i++) {
      for (int j = 0; j < room.getTileGrid().get(i).size; j++) {
        String current = room.getTileGrid().get(i).get(j);
        Cell cell = new Cell();
        cell.setTile(stringTerrainTileMap.get(current));
        layer.setCell(i, j, cell);
      }
    }

    tiledMap.getLayers().add(layer);
    return tiledMap;
  }

  private Map<String, TerrainTile> createStringTerrainTileMap(Room room) {
    ResourceService resourceService = ServiceLocator.getResourceService();
    Map<String, TerrainTile> stringTerrainTileMap = new Map<String, TerrainTile>() {
      private StringToTileEntry head = null;
      private int size = 0;

      @Override
      public int size() {
        return size;
      }

      @Override
      public boolean isEmpty() {
        return head == null;
      }

      @Override
      public boolean containsKey(Object key) {
        return false;
      }

      @Override
      public boolean containsValue(Object value) {
        return false;
      }

      @Override
      public TerrainTile get(Object key) {
        if (head == null) {
          return null;
        }
        StringToTileEntry current = head;
        do {
          if (current.getKey().equals(key)) {
            return current.getValue();
          }
          current = current.getNext();
        } while (current != null);
        return null;
      }

      @Override
      public TerrainTile put(String key, TerrainTile value) {
        if (head == null) {
          head = new StringToTileEntry(key, value);
          size++;
          return value;
        }
        StringToTileEntry current = head;
        StringToTileEntry previous;
        do {
          if (current.getKey().equals(key)) {
            TerrainTile temp = current.getValue();
            current.setValue(value);
            return temp;
          }
          previous = current;
          current = current.getNext();
        } while (current != null);
        previous.setNext(new StringToTileEntry(key, value));
        size++;
        return null;
      }

      @Override
      public TerrainTile remove(Object key) {
        return null;
      }

      @Override
      public void putAll(Map<? extends String, ? extends TerrainTile> m) {

      }

      @Override
      public void clear() {
        head = null;
        size = 0;
      }

      @Override
      public Set<String> keySet() {
        return null;
      }

      @Override
      public Collection<TerrainTile> values() {
        return null;
      }

      @Override
      public Set<Entry<String, TerrainTile>> entrySet() {
        return null;
      }

      class StringToTileEntry {
        private String symbol;
        private TerrainTile tile;
        private StringToTileEntry next;

        StringToTileEntry(String symbol, TerrainTile tile) {
          this.symbol = symbol;
          this.tile = tile;
          this.next = null;
        }

        String getKey() {
          return symbol;
        }

        TerrainTile getValue() {
          return tile;
        }

        void setValue(TerrainTile tile) {
          this.tile = tile;
        }

        StringToTileEntry getNext() {
          return next;
        }

        void setNext(StringToTileEntry next) {
          this.next = next;
        }
      }
    };
    for (int i = 0; i < room.getTileDefinitions().size; i++) {
      DrmObject current = room.getTileDefinitions().get(i);
      stringTerrainTileMap.put(current.getSymbol(), new TerrainTile(new TextureRegion(
              resourceService.getAsset(current.getTexture(), Texture.class))));
    }
    return stringTerrainTileMap;
  }

  /**
   * This enum should contain the different terrains in your game, e.g. forest, cave, home, all with
   * the same oerientation. But for demonstration purposes, the base code has the same level in 3
   * different orientations.
   */
  public enum TerrainType {
    FOREST_DEMO,
    FOREST_DEMO_ISO,
    FOREST_DEMO_HEX
  }
}
