package com.deco2800.game.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.entities.components.player.CameraComponent;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.maps.floors.Floor;
import com.deco2800.game.maps.rooms.RoomProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Home {
    private static final Logger logger = LoggerFactory.getLogger(Home.class);
    private final Array<Floor> floors = new Array<>();
    private Floor activeFloor;

    public void create(CameraComponent cameraComponent) {
        loadProperties();
        floors.add(new Floor((OrthographicCamera) cameraComponent.getCamera()));
        activeFloor = floors.get(0);
        activeFloor.create();
    }

    public void loadProperties() {
        try {
            RoomProperties roomProperties = FileLoader.readClass(RoomProperties.class, RoomProperties.ROOM_PROPERTIES_PATH);
            ServiceLocator.registerRoomProperties(roomProperties);
        } catch (ClassCastException e) {
            logger.error("File {} is malformed", RoomProperties.ROOM_PROPERTIES_PATH);
        }
    }

    public Floor getActiveFloor() {
        return activeFloor;
    }
}
