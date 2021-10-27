package com.deco2800.game.maps;

import com.badlogic.gdx.files.FileHandle;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.generic.Loadable;
import com.deco2800.game.screens.game.GameScreen;
import com.deco2800.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Container for multiple game areas (floors).
 * Contains functionality for randomising floor plans.
 */
public class Home implements Loadable {
    private static final Logger logger = LoggerFactory.getLogger(Home.class);
    public static final String DIRECTORY = "maps/";
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
}
