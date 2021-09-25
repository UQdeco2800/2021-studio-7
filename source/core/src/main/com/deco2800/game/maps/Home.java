package com.deco2800.game.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.entities.components.player.CameraComponent;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.maps.floors.Floor;
import com.deco2800.game.maps.floors.FloorPlan;
import com.deco2800.game.maps.rooms.RoomProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Home {
    private static final Logger logger = LoggerFactory.getLogger(Home.class);
    private final Array<Floor> floors = new Array<>();
    private Floor activeFloor;
    private RoomProperties roomProperties;

    public Home() {
    }

    public Home(String filename) {
        floors.add(new Floor());
        floors.get(0).setFloorPlan(FileLoader.readClass(FloorPlan.class, filename));
    }

    public void create(CameraComponent cameraComponent) {
        loadProperties();
        if (floors.size == 0) {
            floors.add(new Floor());
        }
        activeFloor = floors.get(0);
        activeFloor.setCamera((OrthographicCamera) cameraComponent.getCamera());
        activeFloor.create();
    }

    public void loadProperties() {
        try {
            roomProperties = FileLoader.readClass(RoomProperties.class, RoomProperties.ROOM_PROPERTIES_PATH);
        } catch (ClassCastException e) {
            logger.error("File {} is malformed", RoomProperties.ROOM_PROPERTIES_PATH);
        }
    }

    public Floor getActiveFloor() {
        return activeFloor;
    }

    public RoomProperties getRoomProperties() {
        return roomProperties;
    }
}
