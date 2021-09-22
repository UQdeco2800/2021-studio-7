package com.deco2800.game.maps.floor.rooms;

import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.files.FileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoomProperties {
    private static final Logger logger = LoggerFactory.getLogger(RoomProperties.class);
    private static ObjectMap<Class<Room>, ObjectMap<String, Room>> resources;

    @SuppressWarnings("unchecked")
    public static void loadProperties() {
        try {
            resources = FileLoader.readClass(ObjectMap.class, ROOM_PROPERTIES_PATH);
        } catch (ClassCastException e) {
            logger.error("File {} is malformed", ROOM_PROPERTIES_PATH);
        }

    }

    public static <T extends Room> T get(Class<T> type) {
        return get(type, "five_by_five");
    }

    @SuppressWarnings("unchecked")
    public static <T extends Room> T get(Class<T> type, String resourceName) {
        T room = null;
        try {
            room = (T) resources.get(Room.class).get(resourceName);
        } catch (ClassCastException e) {
            logger.error("Couldn't find resource {} in map {}", resourceName, type);
        }
        return room;
    }

    public static ObjectMap<Class<Room>, ObjectMap<String, Room>> getAll() {
        return resources;
    }

    public static final String DIRECTORY = "maps/";
    public static final String ROOM_PROPERTIES_PATH = DIRECTORY.concat("room_properties.json");
    public static final ObjectMap<Class<? extends Room>, String> ROOM_CLASS_TO_PATH = new ObjectMap<>();
    {
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
