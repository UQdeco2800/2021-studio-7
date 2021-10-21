package com.deco2800.game.maps;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.deco2800.game.entities.components.player.CameraComponent;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.screens.maingame.MainGameScreen;
import com.deco2800.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Container for multiple game areas (floors).
 * Contains functionality for randomising floor plans.
 */
public class Home {
    private static final Logger logger = LoggerFactory.getLogger(Home.class);
    public static final String DIRECTORY = "maps/";
    private final List<Floor> floors = new ArrayList<>();
    private Floor activeFloor;
    // Defined on call for creation
    private MainGameScreen mainGameScreen;
    private boolean created = false;

    public Home() {
    }

    public Home(String filename) {
        Floor newFloor = FileLoader.readClass(Floor.class, filename);
        floors.add(newFloor);
    }

    public void create(CameraComponent cameraComponent) {
        if (!created) {
            if (floors.isEmpty()) {
                Floor newFloor = createRandomFloor();
                floors.add(newFloor);
            }
            activeFloor = floors.get(0);
            activeFloor.setCamera((OrthographicCamera) cameraComponent.getCamera());
            activeFloor.create();
        }
        created = true;
    }

    /**
     * Queries for a list of JSON files in a pre-defined directory. Selects one at random
     * and initialises the floor plan.
     * @return A valid Floor extracted from a JSON file.
     */
    private Floor createRandomFloor() {
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

        return randomFloor;
    }

    public Floor getActiveFloor() {
        return activeFloor;
    }

    public void setActiveFloor(Integer index) {
        if (index < floors.size()) {
            activeFloor = floors.get(index);
        } else {
            logger.error("Home does not have a floor at level {}", index);
        }
    }

    public void setMainGameScreen(MainGameScreen mainGameScreen) {
        this.mainGameScreen = mainGameScreen;
    }
}
