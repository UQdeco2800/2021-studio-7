package com.deco2800.game.areas.rooms.jaleel;

import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.events.EventHandler;
import com.deco2800.game.utils.math.RandomUtils;

public class HouseGameArea extends GameArea {

    private final TerrainFactory terrainFactory;
    private EventHandler eventHandler;

    public HouseGameArea(TerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
        this.eventHandler = new EventHandler();
    }

    @Override
    public void create() {

    }

    private void spawnPlayer(GridPoint2 gridPosition) {
        Entity newPlayer = PlayerFactory.createPlayer();
        spawnEntityAt(newPlayer, gridPosition, true, true);
    }

    private void spawnBed(GridPoint2 gridPosition) {
        // Note: interactable objects must be created AFTER the player, as it requires the player
        // entity as an argument
        Entity bed = ObstacleFactory.createBed();
        spawnEntityAt(bed, gridPosition, true, true);
    }

    private void spawnMum(GridPoint2 gridPosition) {
        Entity mum = NPCFactory.createMum();
        spawnEntityAt(mum, gridPosition, true, true);
    }
}
