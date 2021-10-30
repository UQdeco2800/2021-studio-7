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
    private Floor activeFloor;
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
        floor.setHome(this);
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
            fileHandles.remove(fileHandle);
            randomFloor = FileLoader.readClass(Floor.class, fileHandle.path());
        } while (randomFloor == null && !fileHandles.isEmpty());

        if (randomFloor == null) {
            throw new NullPointerException("A valid floor plan json file could not be loaded");
        }
        randomFloor.initialise();

        return randomFloor;
    }

    public void create() {
        activeFloor = floors.get(0);
        activeFloor.create();
    }

    public Floor getActiveFloor() {
        return activeFloor;
    }

    public GameScreen getScreen() {
        return screen;
    }

    public Method getMethod(String name) {
        int delim = name.indexOf('_');
        String categoryName = name.substring(0, delim);
        return objectLibrary.get(categoryName).method;
    }

    public GridObject getObject(String name) {
        int delim = name.indexOf('_');
        String categoryName = name.substring(0, delim);
        return objectLibrary.get(categoryName).list.get(name);
    }

    @Override
    public void loadAssets() {
        logger.debug("    Loading home assets");
        for (Floor floor : floors) {
            floor.loadAssets();
        }
    }

    @Override
    public void unloadAssets() {
        logger.debug("    Unloading home assets");
        for (Floor floor : floors) {
            floor.unloadAssets();
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
                    JsonValue subIterator = iterator.child();
                    ObjectCategory category = new ObjectCategory();
                    category.read(json, iterator);
                    library.put(subIterator.name(), category);
                    iterator = iterator.next();
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    public static class ObjectCategory implements Json.Serializable {
        private Method method;
        private ObjectMap<String, GridObject> list;

        @Override
        public void write(Json json) {
            // No purpose yet
        }

        @Override
        public void read(Json json, JsonValue jsonData) {
            try {
                JsonValue iterator = jsonData.child();

                FileLoader.assertJsonValueName(iterator, "method");
                int delim = iterator.asString().lastIndexOf('.');
                String className = iterator.asString().substring(delim);
                String methodName = iterator.asString().substring(0, delim);
                method = Class.forName(className).getMethod(methodName, GridObject.class, GridPoint2.class);

                list = new ObjectMap<>();
                while (iterator != null) {
                    GridObject object = new GridObject();
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
