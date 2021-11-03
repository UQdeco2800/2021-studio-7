package com.deco2800.game.entities.components.npc;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.maps.Home;
import com.deco2800.game.maps.ObjectDescription;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.raycast.RaycastHit;
import com.deco2800.game.screens.game.GameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MumActions extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(MumActions.class);
    private Entity closestDoor;


    @Override
    public void create() {
        super.create();
        entity.getEvents().trigger("update_animation", "standing_south");
        Character[][] floorGrid = ServiceLocator.getHome().getFloor().getFloorGrid();
        ObjectMap<Character, ObjectDescription> floorMap = ServiceLocator.getHome().getFloor().getEntityMap();
        GridPoint2 entityPos = new GridPoint2((int) entity.getPosition().x, (int) entity.getPosition().y);
        String[] adjacentObjectNames = new String[4];
        adjacentObjectNames[0] = Home.getObjectName(floorMap.get(floorGrid[entityPos.x][entityPos.y + 1]).getData());
        adjacentObjectNames[1] = Home.getObjectName(floorMap.get(floorGrid[entityPos.x + 1][entityPos.y]).getData());
        adjacentObjectNames[2] = Home.getObjectName(floorMap.get(floorGrid[entityPos.x][entityPos.y - 1]).getData());
        adjacentObjectNames[3] = Home.getObjectName(floorMap.get(floorGrid[entityPos.x - 1][entityPos.y]).getData());
        for (String objectName : adjacentObjectNames) {
            if (objectName != null && objectName.contains("door")) {
                
            }
        }
    }

    @Override
    public void onCollisionStart(Entity target) {
        if (target.getComponent(PlayerActions.class) != null) {
            triggerPlayerCaught();
        }
    }

    private void triggerPlayerCaught() {
        logger.debug("MUM started collision with PLAYER, triggering player caught");
        ServiceLocator.getScreen(GameScreen.class).getGameUI().getEvents().trigger("player_caught");
    }
}
