package com.deco2800.game.areas.rooms.jaleel;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.Method;
import com.deco2800.game.files.FileLoader;
import net.dermetfan.gdx.physics.box2d.PositionController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class RoomReader {

    private static final Logger logger = LoggerFactory.getLogger(RoomReader.class);

    // DRM symbols
    private static final char OPEN_BRACKET = '{';
    private static final char CLOSE_BRACKET = '}';
    private static final char LINE_IGNORE = '#';
    private static final String DELIMITER = ":";

    // DRM keywords
    private static final String SCALE_START = "SCALE " + OPEN_BRACKET;
    private static final String TILE_START = "TILE " + OPEN_BRACKET;
    private static final String ENTITY_START = "ENTITY " + OPEN_BRACKET;
    private static final String DEFINE_KEY = "DEFINE";
    private static final String GRID_KEY = "GRID";
    private static final String ANCHOR_KEY = "ANCHOR";

    private FileReader reader;
    private String currentLine;

    public static FileReader getRoomFileHandle(String filename, FileLoader.Location location) {
        FileHandle file = FileLoader.getFileHandle(filename, location);
        if (file == null) {
            logger.error("Failed to create file handle for {}", filename);
            return null;
        } else if (file.extension().equals("drm")) {
            logger.error("{} is not a .drm file", filename);
            return null;
        }

        FileReader reader;
        try {
            reader = new FileReader(file.file());
        } catch (FileNotFoundException e) {
            return null;
        }
        return reader;
    }

    public String nextLine() {
        StringBuilder buffer = new StringBuilder();
        try {
            while (buffer.length() == 0) {
                int data = reader.read();
                while (data != -1 && (char) data != '\n') {
                    if ((char) data == LINE_IGNORE) {
                        break;
                    }
                    if ((char) data != ' ' && (char) data != '\t') {
                        buffer.append((char) data);
                    }
                    data = reader.read();
                }
            }
        } catch (IOException e) {
            return null;
        }
        return buffer.toString();
    }

    public static String[] extractTokens(String line) {
        return line.split(DELIMITER);
    }

    private Vector2 extractMapScale() {
        String[] scale;
        // Line should be at "SCALE:x:y"
        currentLine = nextLine();
        if (!currentLine.startsWith(SCALE_START)) {
            throw new IllegalArgumentException("Scale keyword missing in .drm file");
        } else {
            scale = extractTokens(currentLine);
            if (scale.length != 2) {
                throw new IllegalArgumentException("Scale is missing height or width in .drm file");
            }
        }
        return new Vector2(scale[0].charAt(0), scale[1].charAt(0));
    }
    
    private Array<DrmObject> extractTileDefinitions() {
        // Line should be at "TILES {"
        currentLine = nextLine();
        if (!currentLine.startsWith(TILE_START)) {
            throw new IllegalArgumentException("Tile keyword missing in .drm file");
        }

        // Line should be at "DEFINE {"
        currentLine = nextLine();
        if (!currentLine.startsWith(DEFINE_KEY)) {
            throw new IllegalArgumentException("Define keyword missing in .drm file");
        }

        // Extract tile definitions from .drm file
        Array<DrmObject> tileDefinitions = new Array<>();
        while (currentLine.charAt(0) != CLOSE_BRACKET) {
            currentLine = nextLine();
            String[] tokens = extractTokens(currentLine);
            if (tokens.length == 2) {
                tileDefinitions.add(new DrmObject(tokens[0], tokens[1]));
            } else if (tokens.length == 3) {
                tileDefinitions.add(new DrmObject(tokens[0], tokens[1], tokens[2]));
            } else {
                throw new IllegalArgumentException("Too many tile arguments in .drm file");
            }
        }

        // Done extracting definitions
        if (tileDefinitions.size == 0) {
            throw new IllegalArgumentException("No tile definitions specified in .drm file");
        }
        return tileDefinitions;
    }

    private Array<DrmObject> extractEntityDefinitions() {
        // Line should be at "ENTITIES {"
        currentLine = nextLine();
        if (!currentLine.startsWith(ENTITY_START)) {
            throw new IllegalArgumentException("Entity keyword missing in .drm file");
        }

        // Line should be at "DEFINE {"
        currentLine = nextLine();
        if (!currentLine.startsWith(DEFINE_KEY)) {
            throw new IllegalArgumentException("Define keyword missing in .drm file");
        }

        // Extract entity definitions from .drm file
        Array<DrmObject> entityDefinitions = new Array<>();
        while (currentLine.charAt(0) != CLOSE_BRACKET) {
            currentLine = nextLine();
            String[] tokens = extractTokens(currentLine);
            if (tokens.length == 2) {
                entityDefinitions.add(new DrmObject(tokens[0], tokens[1]));
            } else if (tokens.length == 3) {
                entityDefinitions.add(new DrmObject(tokens[0], tokens[1], tokens[2]));
            } else {
                throw new IllegalArgumentException("Too many entity arguments in .drm file");
            }
        }

        // Done extracting definitions
        if (entityDefinitions.size == 0) {
            throw new IllegalArgumentException("No tile definitions specified in .drm file");
        }
        return entityDefinitions;
    }
}


