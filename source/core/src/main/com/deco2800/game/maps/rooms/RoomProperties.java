package com.deco2800.game.maps.rooms;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("unchecked")
public class RoomProperties implements Json.Serializable {
    private static final Logger logger = LoggerFactory.getLogger(RoomProperties.class);
    private ObjectMap<Class<? extends Room>, ObjectMap<String, ? extends Room>> resources;

    public <T extends Room> T get(Class<T> type) {
        return get(type, "five_by_five");
    }

    public <T extends Room> T get(Class<T> type, String resourceName) {
        T roomInstance = null;
        ObjectMap<String, ? extends Room> retrievedRoom;
        try {
            retrievedRoom = resources.get(type);
            roomInstance = (T) retrievedRoom.get(resourceName);
        } catch (Exception e) {
            logger.error("Couldn't find resource {} in map {}", resourceName, type);
        }
        return roomInstance;
    }

    public ObjectMap<Class<? extends Room>, ObjectMap<String, ? extends Room>> getResources() {
        return resources;
    }

    @Override
    public void write(Json json) {
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        try {
            resources = new ObjectMap<>();
            JsonValue iterator = jsonData.child();
            while (iterator != null) {
                readRoomInstanceMappings((Class<? extends Room>) Class.forName(iterator.name()),
                        json, iterator);
                iterator = iterator.next();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public <T extends Room> void readRoomInstanceMappings(Class<T> type, Json json, JsonValue jsonData)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ObjectMap<String, T> instances = new ObjectMap<>();
        JsonValue iterator = jsonData.child();
        while (iterator != null) {
            T instance = type.getConstructor().newInstance();
            instance.read(json, iterator);
            instances.put(iterator.name(), instance);
            iterator = iterator.next();
        }
        resources.put(type, instances);
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
