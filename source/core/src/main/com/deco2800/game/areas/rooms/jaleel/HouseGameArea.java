package com.deco2800.game.areas.rooms.jaleel;

import com.deco2800.game.areas.GameArea;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.events.EventHandler;

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
}
