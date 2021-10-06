package com.deco2800.game.areas.terrain;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.MiniMap;
import com.deco2800.game.rendering.components.RenderComponent;

/**
 * Render a tiled terrain for a given tiled map and orientation. A terrain is a map of tiles that
 * shows the 'ground' in the game. Enabling/disabling this component will show/hide the terrain.
 */
public class TerrainComponent extends RenderComponent {
  private static final int TERRAIN_LAYER = 0;

  private final TiledMap tiledMap;
  private final TiledMapRenderer tiledMapRenderer;
  private final TiledMapRenderer miniMapRenderer;
  private final OrthographicCamera camera;
  private final OrthographicCamera miniMapCamera;
  private final TerrainOrientation orientation;
  private final float tileSize;
  private final MiniMap miniMap;


  public TerrainComponent(OrthographicCamera camera, OrthographicCamera miniMapCamera, TiledMap map, TiledMapRenderer renderer,
                          TiledMapRenderer miniMapRenderer, TerrainOrientation orientation, float tileSize) {
    this.camera = camera;
    this.miniMapCamera = miniMapCamera;
    this.tiledMap = map;
    this.orientation = orientation;
    this.tileSize = tileSize;
    this.tiledMapRenderer = renderer;
    this.miniMapRenderer = miniMapRenderer;

    miniMap = new MiniMap(this.tiledMap);
  }

  public void update(float x, float y){
    camera.position.x = x;
    camera.position.y = y;
    camera.update();
    tiledMapRenderer.setView(miniMapCamera);
    miniMap.update(x, y);
  }

  public Vector2 tileToWorldPosition(GridPoint2 tilePos) {
    return tileToWorldPosition(tilePos.x, tilePos.y);
  }

  public Vector2 tileToWorldPosition(int x, int y) {
    switch (orientation) {
      case HEXAGONAL:
        float hexLength = tileSize / 2;
        float yOffset = (x % 2 == 0) ? 0.5f * tileSize : 0f;
        return new Vector2(x * (tileSize + hexLength) / 2, y + yOffset);
      case ISOMETRIC:
        return new Vector2((x + y) * tileSize / 2, (y - x) * tileSize / 4f); //3.724
      case ORTHOGONAL:
        return new Vector2(x * tileSize, y * tileSize);
      default:
        return null;
    }
  }

  public float getTileSize() {
    return tileSize;
  }

  public GridPoint2 getMapBounds(int layer) {
    TiledMapTileLayer terrainLayer = (TiledMapTileLayer)tiledMap.getLayers().get(layer);
    return new GridPoint2(terrainLayer.getWidth(), terrainLayer.getHeight());
  }

  public TiledMap getMap() {
    return tiledMap;
  }

  @Override
  public void draw(SpriteBatch batch) {
    tiledMapRenderer.setView(camera);
    tiledMapRenderer.render();
    miniMapRenderer.setView(miniMapCamera);
    miniMapRenderer.render();
  }

  @Override
  public void dispose() {
    tiledMap.dispose();
    super.dispose();
  }

  @Override
  public float getZIndex() {
    return 0f;
  }

  @Override
  public int getLayer() {
    return TERRAIN_LAYER;
  }

  public enum TerrainOrientation {
    ORTHOGONAL,
    ISOMETRIC,
    HEXAGONAL
  }
}
