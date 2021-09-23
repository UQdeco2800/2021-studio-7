package com.deco2800.game.areas.rooms;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.utils.math.IntUtils;
import com.deco2800.game.utils.math.MatrixUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class RoomReader {

    private static final Logger logger = LoggerFactory.getLogger(RoomReader.class);
    private BufferedReader reader = null;
    private String currentLine = "";

    // DRM symbols
    private static final String OPEN_BRACKET = "{";
    private static final String CLOSE_BRACKET = "}";
    private static final char IGNORE_REST = '#';
    private static final String DEF_DELIM = ":";
    private static final String GRID_DELIM = " ";

    // DRM keywords
    private static final String SCALE_HEADER = "SCALE";
//  private static final String ANCHOR_HEADER = "ANCHOR";
    private static final String TILE_HEADER = "TILES ".concat(OPEN_BRACKET);
    private static final String ENTITY_HEADER = "ENTITIES ".concat(OPEN_BRACKET);
    private static final String DEFINE_HEADER = "DEFINE ".concat(OPEN_BRACKET);
    private static final String GRID_HEADER = "GRID ".concat(OPEN_BRACKET);

    /**
     * @param filename name of the file
     * @param location relative location of the file, usually passed as INTERNAL
     */
    public void setBufferedReader(String filename, FileLoader.Location location) {
        FileHandle file = FileLoader.getFileHandle(filename, location);
        // Check file state
        if (file == null) {
            logger.error("Failed to create file handle for {}", filename);
            return;
        } else if (!file.extension().equals("drm")) {
            logger.error("{} is not a .drm file", filename);
            return;
        }

        // Create buffered reader using .drm file above
        try {
            reader = new BufferedReader(new FileReader(file.file()));
        } catch (FileNotFoundException e) {
            logger.error("The file {} was not found", filename);
        }
    }

    /**
     * @return room with all data extracted from .drm file
     */
    public Room extractRoom() {
        Vector2 roomScale = extractRoomScale();
        // Extracting tile information
        RoomObject[] tileDefinitions = extractDefinitions(TILE_HEADER);
        String[][] tileGrid = extractGrid(roomScale);
        // Extracting entity information
        RoomObject[] entityDefinitions = extractDefinitions(ENTITY_HEADER);
        String[][] entityGrid = extractGrid(roomScale);

        return new Room(roomScale, tileDefinitions, entityDefinitions, tileGrid, entityGrid);
    }

    /**
     * @return room scale extracted from .drm file
     */
    private Vector2 extractRoomScale() {
        assertHeader(SCALE_HEADER, "Missing scale header in .drm file");
        String[] scale = currentLine.split(DEF_DELIM);
        if (scale.length != 3) {
            logger.error("Scale is missing height or width in .drm file");
        }

        return new Vector2(IntUtils.strToInt(scale[1]), IntUtils.strToInt(scale[2]));
    }

    /**
     * @return object type definitions extracted from .drm file
     */
    private RoomObject[] extractDefinitions(String objectHeader) {
        assertHeader(objectHeader, "Missing object header in .drm file");
        assertHeader(DEFINE_HEADER, "Missing define header in .drm file");

        // Extract object definitions from .drm file
        Array<RoomObject> temp = new Array<>();
        currentLine = nextLine();
        do {
            String[] tokens = currentLine.split(DEF_DELIM);
            if (tokens.length == 2) {
                temp.add(new RoomObject(tokens[0], tokens[1]));
            } else if (tokens.length == 3) {
                temp.add(new RoomObject(tokens[0], tokens[1], tokens[2]));
            } else {
                logger.error("Too many object parameters mentioned in .drm file");
            }
            currentLine = nextLine();
        } while (!currentLine.equals(CLOSE_BRACKET));

        // Convert type
        RoomObject[] definitions = new RoomObject[temp.size];
        for (int i = 0; i < temp.size; i++) {
            definitions[i] = temp.get(i);
        }

        // Done extracting definitions
        if (definitions.length == 0) {
            logger.error("No object definitions in .drm file");
        }
        return definitions;
    }

    /**
     * @param mapScale dimensions of the room
     * @return square matrix defining object grid positions using symbols
     */
    private String[][] extractGrid(Vector2 mapScale) {
        assertHeader(GRID_HEADER, "Missing grid header in .drm file");
        // Take max length to make perfect square matrix
        int max = (int) mapScale.x;
        if (mapScale.x < mapScale.y) {
            max = (int) mapScale.y;
        }

        String[][] xyGrid = new String[max][max];
        // Extracts grid by row, stores in form [x][y]
        for (int y = 0; y < max; y++) {
            currentLine = nextLine();
            String[] tokens = currentLine.split(GRID_DELIM);
            if (currentLine.contains(CLOSE_BRACKET)) {
                if (mapScale.y == max) {
                    // We have reached the close bracket before our guaranteed rows were finished
                    logger.error("Grid dimensions do not match scale in .drm file");
                }
                // Fill missing rows to complete max by max matrix
                for (int ySup = y; ySup < max; ySup++) {
                    // Fill row cells with empty symbols
                    for (int x = 0; x < max; x++) {
                        xyGrid[x][ySup] = "";
                    }
                }
                currentLine = nextLine();
                break;
            }
            // Fill number of row cells equal to the maximum of x and y
            for (int x = 0; x < max; x++) {
                if (x < tokens.length) {
                    // Fill with extracted cell symbol
                    xyGrid[x][y] = tokens[x];
                } else {
                    // Fill missing cells with empty symbol
                    xyGrid[x][y] = "";
                }
            }
        }

        assertFooter("Missing grid close bracket in .drm file");
        assertFooter("Missing object type close bracket in .drm file");

        // Flip vertically grid to match orientation in file
        return MatrixUtils.flipVertically(xyGrid);
    }

    private String nextLine() {
        String line = "";
        int ignoreIndex;

        try {
            do {
                line = reader.readLine().strip();
                ignoreIndex = line.indexOf(IGNORE_REST);
                if (ignoreIndex != -1) {
                    line = line.substring(0, ignoreIndex);
                }
            } while (line.length() == 0);
        } catch (IOException e) {
            logger.error("Error reading .drm file IO");
        }
        // Dictates that close brackets must occur on separate lines or else information will be lost
        if (line.contains(CLOSE_BRACKET) && line.length() != 1) {
            return "";
        } else {
            return line;
        }
    }

    private void assertHeader(String header, String msg) {
        currentLine = nextLine();
        if (!currentLine.startsWith(header)) {
            logger.error(msg);
        }
    }

    private void assertFooter(String msg) {
        currentLine = nextLine();
        if (!currentLine.equals(CLOSE_BRACKET)) {
            logger.error(msg);
        }
    }
}