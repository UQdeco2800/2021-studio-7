package com.deco2800.game.maps;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.generic.Loadable;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.maps.terrain.TerrainComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an area in the game, such as a level, indoor area, etc. An area has a terrain and
 * other entities to create on that terrain.
 *
 * <p>Support for enabling/disabling game areas could be added by making this a Component instead.
 */
public abstract class RetroactiveArea implements Disposable, Loadable {
    protected TerrainComponent terrain;
    protected List<Entity> areaEntities;

    protected RetroactiveArea() {
        areaEntities = new ArrayList<>();
    }

    /**
     * Create the game area in the world.
     */
    public abstract void create();

    /**
     * Create entity at its current position
     *
     * @param entity Entity (not yet registered)
     */
    public void createEntity(Entity entity) {
        areaEntities.add(entity);
        ServiceLocator.getEntityService().register(entity);
    }

    /**
     * Create entity on a given tile. Requires the terrain to be set first.
     *
     * @param entity  Entity (not yet registered)
     * @param tilePos tile position to create at
     * @param centerX true to center entity X on the tile, false to align the bottom left corner
     * @param centerY true to center entity Y on the tile, false to align the bottom left corner
     */
    public void createEntityAt(Entity entity, GridPoint2 tilePos, boolean centerX, boolean centerY) {
        Vector2 worldPos = terrain.tileToWorldPosition(tilePos);
        float tileSize = terrain.getTileSize();

        if (centerX) {
            worldPos.x += (tileSize / 2) - entity.getCenterPosition().x;
        }
        if (centerY) {
            worldPos.y += (tileSize / 2) - entity.getCenterPosition().y;
        }

        entity.setPosition(worldPos);
        createEntity(entity);
    }

    public abstract void createUI();

    @Override
    public void loadAssets() {
    }

    @Override
    public void unloadAssets() {
    }

    /**
     * Dispose of all internal entities in the area
     */
    public void dispose() {
        for (Entity entity : areaEntities) {
            entity.dispose();
        }
        unloadAssets();
    }
}
