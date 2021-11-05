package com.deco2800.game.maps;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.generic.Loadable;
import com.deco2800.game.screens.game.GameScreen;
import com.deco2800.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Container for multiple game areas (floors).
 * Contains functionality for randomising floor plans.
 */
public class Home implements Loadable {
    private static final Logger logger = LoggerFactory.getLogger(Home.class);
    public static final String DIRECTORY = "maps/";
    private static final String LIBRARY_FILENAME = DIRECTORY.concat("object_library.json");
    private static ObjectMap<String, ObjectCategory> objectLibrary;
    private final List<Floor> floors = new ArrayList<>();
    private final GameScreen screen;

    public Home(GameScreen screen) {
        this.screen = screen;
    }

    public void initialise() {
        initialise(null);
    }

    public void initialise(String filename) {
        ObjectLibraryWrapper library = FileLoader.readClass(ObjectLibraryWrapper.class, LIBRARY_FILENAME);
        objectLibrary = library.library;

        Floor floor;
        if (filename != null) {
            floor = FileLoader.readClass(Floor.class, filename);
        } else {
            floor = initialiseRandomFloor();
        }
        floors.add(floor);
    }

    /**
     * Queries for a list of JSON files in a pre-defined directory. Selects one at random
     * and initialises the floor plan.
     *
     * @return A valid Floor extracted from a JSON file.
     */
    private Floor initialiseRandomFloor() {
        List<FileHandle> fileHandles = FileLoader.getJsonFiles(DIRECTORY.concat("_floor_plans"));

        Floor randomFloor;
        do {
            FileHandle fileHandle = fileHandles.get(RandomUtils.getSeed().nextInt(fileHandles.size()));

            randomFloor = FileLoader.readClass(Floor.class, fileHandle.path());

            fileHandles.remove(fileHandle);
        } while (randomFloor == null && !fileHandles.isEmpty());

        if (randomFloor == null) {
            throw new NullPointerException("A valid floor plan json file could not be loaded");
        }
        randomFloor.setHome(this);
        randomFloor.initialise();

        return randomFloor;
    }

    public void create() {
        floors.get(0).create();
    }

    public GameScreen getScreen() {
        return screen;
    }

    public static String getObjectName(ObjectData data) {
        for (ObjectMap.Entry<String, ObjectCategory> entry : new ObjectMap.Entries<>(objectLibrary)) {
            String objectName = entry.value.list.findKey(data,false);
            if (objectName != null) {
                return String.format("%s_%s", entry.key, objectName);
            }
        }
        return null;
    }

    public static Method getMethod(String name) {
        if (name != null) {
            ObjectCategory category = objectLibrary.get(name.substring(0, name.indexOf('_')));
            if (category != null) {
                return category.method;
            }
        }
        return null;
    }

    public static ObjectData getObject(String name) {
        int category_delim = name.indexOf('_');
        String categoryName = name.substring(0, category_delim);
        ObjectCategory category = objectLibrary.get(categoryName);
        if (category != null) {
            String objectName = name.substring(category_delim + 1);
            return category.list.get(objectName);
        }
        return null;
    }

    public Floor getFloor() {
        return floors.get(0);
    }

    @Override
    public void loadAssets() {
        logger.debug("    Loading home assets");
        for (ObjectCategory category : new ObjectMap.Values<>(objectLibrary)) {
            for (ObjectData data : new ObjectMap.Values<>(category.list)) {
                data.loadAssets();
            }
        }
    }

    @Override
    public void unloadAssets() {
        logger.debug("    Unloading home assets");
        for (ObjectCategory category : new ObjectMap.Values<>(objectLibrary)) {
            for (ObjectData data : new ObjectMap.Values<>(category.list)) {
                data.unloadAssets();
            }
        }
    }

    public static class ObjectLibraryWrapper implements Json.Serializable {
        ObjectMap<String, ObjectCategory> library;

        @Override
        public void write(Json json) {
            // No purpose yet
        }

        @Override
        public void read(Json json, JsonValue jsonData) {
            try {
                JsonValue iterator = jsonData.child();
                library = new ObjectMap<>();

                while (iterator != null) {
                    ObjectCategory category = new ObjectCategory();
                    category.read(json, iterator);
                    library.put(iterator.name(), category);
                    iterator = iterator.next();
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    public static class ObjectCategory implements Json.Serializable {
        private Method method;
        private ObjectMap<String, ObjectData> list;

        @Override
        public void write(Json json) {
            // No purpose yet
        }

        @Override
        public void read(Json json, JsonValue jsonData) {
            try {
                JsonValue iterator = jsonData.child();

                if (iterator.name().equals("method")) {
                    int delim = iterator.asString().lastIndexOf('.');
                    String className = iterator.asString().substring(0, delim);
                    String methodName = iterator.asString().substring(delim + 1);
                    method = Class.forName(className).getMethod(methodName, ObjectDescription.class, GridPoint2.class);
                    iterator = iterator.next();
                } else {
                    method = null;
                }

                list = new ObjectMap<>();
                while (iterator != null) {
                    ObjectData object = new ObjectData();
                    object.read(json, iterator);
                    list.put(iterator.name(), object);
                    iterator = iterator.next();
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }
}
