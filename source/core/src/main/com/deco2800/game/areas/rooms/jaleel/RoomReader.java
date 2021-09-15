package com.deco2800.game.areas.rooms.jaleel;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.files.FileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class RoomReader {

    private static final Logger logger = LoggerFactory.getLogger(RoomReader.class);

    // DRM symbols
    private static final String OPEN_BRACKET = "{";
    private static final String CLOSE_BRACKET = "}";
    private static final String LINE_IGNORE = "#";
    private static final String DEF_DELIM = ":";
    private static final String GRID_DELIM = " ";

    // DRM keywords
    private static final String SCALE_KEY = "SCALE";
    private static final String TILE_KEY = "TILES ".concat(OPEN_BRACKET);
    private static final String ENTITY_KEY = "ENTITIES ".concat(OPEN_BRACKET);
    private static final String DEFINE_KEY = "DEFINE ".concat(OPEN_BRACKET);
    private static final String GRID_KEY = "GRID ".concat(OPEN_BRACKET);
    private static final String ANCHOR_KEY = "ANCHOR";

    private final BufferedReader reader;
    private String currentLine;

    public RoomReader(String filename, FileLoader.Location location) {
        this.reader = newBufferedReader(filename, location);
    }

    public static BufferedReader newBufferedReader(String filename, FileLoader.Location location) {
        FileHandle file = FileLoader.getFileHandle(filename, location);
        if (file == null) {
            logger.error("Failed to create file handle for {}", filename);
            return null;
        } else if (file.extension().equals(".drm")) {
            logger.error("{} is not a .drm file", filename);
            return null;
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file.file()));
        } catch (FileNotFoundException e) {
            return null;
        }
        return reader;
    }

    public Vector2 extractRoomScale() {
        String[] scale;
        // Line should be at "SCALE:x:y"
        currentLine = nextLine();
        if (!currentLine.startsWith(SCALE_KEY)) {
            throw new IllegalArgumentException("Scale keyword missing in .drm file");
        } else {
            scale = currentLine.split(DEF_DELIM);
            if (scale.length != 3) {
                throw new IllegalArgumentException("Scale is missing height or width in .drm file");
            }
        }
        return new Vector2(scale[1].charAt(0), scale[2].charAt(0));
    }

    public Array<DrmObject> extractDefinitions(String key) {
        // Line should be at "{OBJECT} {"
        currentLine = nextLine();
        if (!currentLine.startsWith(key)) {
            throw new IllegalArgumentException("Missing object keyword in .drm file");
        }

        // Line should be at "DEFINE {"
        currentLine = nextLine();
        if (!currentLine.startsWith(DEFINE_KEY)) {
            throw new IllegalArgumentException("Missing define keyword in .drm file");
        }

        // Extract object definitions from .drm file
        Array<DrmObject> objectDefinitions = new Array<>();
        currentLine = nextLine();
        do {
            String[] tokens = currentLine.split(DEF_DELIM);
            if (tokens.length == 2) {
                objectDefinitions.add(new DrmObject(tokens[0], tokens[1]));
            } else if (tokens.length == 3) {
                objectDefinitions.add(new DrmObject(tokens[0], tokens[1], tokens[2]));
            } else {
                throw new IllegalArgumentException("Too many object parameters mentioned in .drm file");
            }
            currentLine = nextLine();
        } while (!currentLine.contains(CLOSE_BRACKET));

        // Done extracting definitions
        if (objectDefinitions.size == 0) {
            throw new IllegalArgumentException("Missing object definitions in .drm file");
        }
        return objectDefinitions;
    }

    public Array<Array<String>> extractGrid() {
        // Line should be at "GRID {"
        currentLine = nextLine();
        if (!currentLine.startsWith(GRID_KEY)) {
            throw new IllegalArgumentException("Grid keyword missing in .drm file");
        }

        Array<Array<String>> objectGrid = new Array<>();
        int lineCount = 0;
        currentLine = nextLine();
        do {
            String[] tokens = currentLine.split(GRID_DELIM);
            objectGrid.add(new Array<>());
            for (String token : tokens) {
                objectGrid.get(lineCount).add(token);
            }
            currentLine = nextLine();
        } while (!currentLine.contains(CLOSE_BRACKET));

        // Done extracting grid positions
        if (objectGrid.size == 0 || objectGrid.get(0).size == 0) {
            throw new IllegalArgumentException("Missing grid positions in .drm file");
        }

        // Done extracting entire object type
        currentLine = nextLine();
        if (!currentLine.contains(CLOSE_BRACKET)) {
            throw new IllegalArgumentException("Missing close bracket in .drm file");
        }
        return objectGrid;
    }

    public String nextLine() {
        String line = "";
        try {
            do {
                line = reader.readLine().strip();
                if (line.contains(LINE_IGNORE)) {
                    line = "";
                }
            } while (line.length() == 0);
        } catch (IOException e) {
            return "";
        }
        return line;
    }

    public String getTileKey() {
        return TILE_KEY;
    }

    public String getEntityKey() {
        return ENTITY_KEY;
    }
}