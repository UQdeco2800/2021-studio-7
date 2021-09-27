package com.deco2800.game.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.entities.components.player.CameraComponent;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.maps.floors.Floor;
import com.deco2800.game.maps.floors.FloorPlan;
import com.deco2800.game.maps.rooms.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Home {
    private static final Logger logger = LoggerFactory.getLogger(Home.class);
    private final Array<Floor> floors = new Array<>();
    private Floor activeFloor;

    public Home() {
    }

    public Home(String filename) {
        FloorPlan floorPlan = FileLoader.readClass(FloorPlan.class, filename);
        if (floorPlan != null) {
            floors.add(new Floor());
            floors.get(0).setFloorPlan(floorPlan);
        }
    }

    public void create(CameraComponent cameraComponent) {
        if (floors.size == 0) {
            floors.add(new Floor());
        }
        activeFloor = floors.get(0);
        activeFloor.setCamera((OrthographicCamera) cameraComponent.getCamera());
        activeFloor.create();
    }

    public Floor getActiveFloor() {
        return activeFloor;
    }

    public void setActiveFloor(Integer index) {
        if (index < floors.size) {
            activeFloor = floors.get(index);
        } else {
            logger.error("Home does not have a floor at level {}", index);
        }
    }

    public static final String DIRECTORY = "maps/";
    public static final String ROOM_PROPERTIES_PATH = DIRECTORY.concat("room_properties.json");
    public static final ObjectMap<Class<? extends Room>, String> ROOM_CLASS_TO_PATH = new ObjectMap<>();
    static {
        ROOM_CLASS_TO_PATH.put(Bathroom.class, DIRECTORY.concat("bathroom"));
        ROOM_CLASS_TO_PATH.put(Bedroom.class, DIRECTORY.concat("bedroom"));
        ROOM_CLASS_TO_PATH.put(Dining.class, DIRECTORY.concat("dining"));
        ROOM_CLASS_TO_PATH.put(FrontFoyer.class, DIRECTORY.concat("front_foyer"));
        ROOM_CLASS_TO_PATH.put(Garage.class, DIRECTORY.concat("garage"));
        ROOM_CLASS_TO_PATH.put(Kitchen.class, DIRECTORY.concat("kitchen"));
        ROOM_CLASS_TO_PATH.put(Laundry.class, DIRECTORY.concat("laundry"));
        ROOM_CLASS_TO_PATH.put(Living.class, DIRECTORY.concat("living"));
    }
}
