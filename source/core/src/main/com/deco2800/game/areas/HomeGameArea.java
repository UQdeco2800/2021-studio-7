package com.deco2800.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;

public class HomeGameArea extends GameArea {

    /**
     * Invoked from drmObject in spawnEntities()
     *
     * @param gridPosition position on the world from file
     * @param texture wall texture from file
     */
    public void spawnWall(GridPoint2 gridPosition, String texture) {
        Entity newWall = ObstacleFactory.createWall(1f, 1f, texture);
        spawnEntityAt(newWall, gridPosition, true, true);
    }

    /**
     * Invoked from drmObject in spawnEntities(), spawns Player entity
     *
     * @param gridPosition position on the world from file
     */
    public void spawnPlayer(GridPoint2 gridPosition) {
        Entity newPlayer = PlayerFactory.createPlayer();
        spawnEntityAt(newPlayer, gridPosition, true, true);
        player = newPlayer;
    }

    /**
     * Invoked from drmObject in spawnEntities(), spawns Bed entity
     *
     * @param gridPosition position on the world from file
     */
    public void spawnBed(GridPoint2 gridPosition) {
        Entity bed = ObstacleFactory.createBed();
        spawnEntityAt(bed, gridPosition, true, true);
    }

    /**
     * Invoked from drmObject in spawnEntities(), spawns Door entity
     *
     * @param gridPosition position on the world from file
     */
    public void spawnDoor(GridPoint2 gridPosition) {
        Entity door = ObstacleFactory.createDoor();
        spawnEntityAt(door, gridPosition, true, true);
    }

    /**
     * Invoked from drmObject in spawnEntities(), spawns Mum entity
     *
     * @param gridPosition position on the world from file
     */
    public void spawnMum(GridPoint2 gridPosition) {
        Entity mum = NPCFactory.createMum(player);
        spawnEntityAt(mum, gridPosition, true, true);
    }

    public void spawnBananaPeel(GridPoint2 gridPosition){
        Entity peel = ObstacleFactory.createBananaPeel();
        spawnEntityAt(peel, gridPosition, true, true);
    }

    public void spawnTV(GridPoint2 gridPosition){
        Entity tv = ObstacleFactory.createTV();
        spawnEntityAt(tv, gridPosition, true, true);
    }
}
