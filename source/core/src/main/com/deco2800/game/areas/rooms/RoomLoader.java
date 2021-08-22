package com.deco2800.game.areas.rooms;

import com.badlogic.gdx.math.GridPoint2;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

public class RoomLoader {
    /* drm file constants */
    private static final int MIN_LINES = 8;
    private static final int BASE_WALL_INDEX = 0;
    private static final int BASE_FLOOR_INDEX = 1;
    private static final int DIMENSION_INDEX = 0;
    private static final int OFFSET_INDEX = 0;


    // Symbols
    private static final char SEP = ',';
    private static final char ALLOC = ':';
    private static final char SEC_START = '<';
    private static final char SEC_END = '>';

    // Patterns
    private static final Pattern LINE_COORD = Pattern.compile("\\d+,\\d+");
    private static final Pattern LINE_SECTION_START = Pattern.compile(".*<");
    private static final Pattern LINE_SECTION_END = Pattern.compile(">.*");
    private static final Pattern LINE_DEFINE = Pattern.compile(".*:\".*\"");
    private static final Pattern LINE_PLACE = Pattern.compile("\\d+,\\d+:.*");

    // Sections
    private static final String SECTION_TEXTURE = "TEXTURE";

    private static final String SUBSEC_DEFINE = "DEFINE";
    private static final String SUBSEC_PLACE = "PLACE";

    // Other
    private static final String VAL_BASE_WALL = "BASE_WALL";
    private static final String VAL_BASE_FLOOR = "BASE_FLOOR";

    /* Private methods */
    private File[] getPathFiles(String path) throws InvalidPathException {
        File mapDirectory = new File(path);

        File[] roomFiles = mapDirectory.listFiles();

        if(roomFiles == null) {
            throw new InvalidPathException(path,
                    "Provided path does not contain files.");
        }

        return roomFiles;
    }


    private static boolean checkForDRM(String path) {
        // Check if the files at the given path end in ".drm"
        // TODO
        return true;
    }


    private static ArrayList<String> preprocessFileLines(
            ArrayList<String> lines) {

        // Construct new arraylist for processed lines
        ArrayList<String> newLines = new ArrayList<String>();

        for (String line : lines) {
            // Trim line
            line = line.trim();

            // Ignore comment lines and newlines
            if (line.length() != 0 && line.charAt(0) != '#') {
                newLines.add(line);
            }
        }

        return newLines;
    }


    private static boolean checkFileLines(ArrayList<String> lines)
            throws FileNotFoundException {
        // TODO: Remake with the get... functions
        // NOTE: Need to check the best way to format this function

        // Check file length
        if (lines.size() < MIN_LINES) {
            return false;
        }

        // Check for dimension and position lines
        for (String line : lines.subList(DIMENSION_INDEX, OFFSET_INDEX + 1)) {
            // Check the lines are comma separated ints -> Coordinates
            if (!LINE_COORD.matcher(line).matches()) {
                throw new FileNotFoundException("Did not start with co-ords");
            }
        }

        // Check for a valid texture section
        int textureStartIndex, textureEndIndex;
        boolean textureStarted, textureEnded;

        textureStartIndex = textureEndIndex = 0;
        textureStarted =  textureEnded = false;

        for (String line : lines) {
            if (line.contains(SECTION_TEXTURE)) {
                // Check for opening
                if (!textureStarted &&
                        LINE_SECTION_START.matcher(line).matches())
                {
                    textureStarted = true;
                    textureStartIndex = lines.indexOf(line);
                }
                // Check for closing. Must have been opened
                else if (textureStarted &&
                        !textureEnded &&
                        LINE_SECTION_END.matcher(line).matches())
                {
                    textureEnded = true;
                    textureEndIndex = lines.indexOf(line);
                }
            }
        }
        if (!(textureStarted && textureEnded)) {
            throw new FileNotFoundException("Could not find a TEXTURE section");
        }

        // Check for valid texture section contents
        boolean hasDefine, hasPlace, hasWall, hasFloor;
        hasDefine = hasPlace = hasWall = hasFloor = false;

        for (String line : lines.subList(textureStartIndex, textureEndIndex)) {
            // Check for DEFINE subsection
            if (line.matches(SUBSEC_DEFINE)) {
                hasDefine = true;
            }
            // Check for PLACE subsection
            else if (line.matches(SUBSEC_PLACE)) {
                hasPlace = true;
            }

            // Check for BASE_WALL
            if (hasDefine && line.contains(VAL_BASE_WALL)) {
                hasWall = true;
            }

            // Check for BASE_FLOOR
            if (hasDefine && line.contains(VAL_BASE_FLOOR)) {
                hasFloor = true;
            }

        }
        if (!(hasDefine && hasPlace && hasWall && hasFloor)) {
            throw new FileNotFoundException("Missing a TEXTURE element");
        }

        return true;
    }


    private static int[] getSectionBounds(ArrayList<String> lines,
                                          String sectionTitle) {

        int sectionStartIndex, sectionEndIndex;
        sectionStartIndex = sectionEndIndex = 0;

        for (String line : lines) {
            // Find starting position
            if (line.contains(sectionTitle) &&
                    LINE_SECTION_START.matcher(line).matches()) {
                sectionStartIndex = lines.indexOf(line);
            }

            // Find ending position
            if (line.contains(sectionTitle) &&
                    LINE_SECTION_END.matcher(line).matches()) {
                sectionEndIndex = lines.indexOf(line);
            }
        }

        if (sectionStartIndex > 0 && sectionEndIndex > 0) {
            return new int[]{sectionStartIndex, sectionEndIndex};
        } else {
            return null;
        }
    }


    private static int[] getSubsecBounds(ArrayList<String> lines,
                                         String section, String subsec) {

        int subsecStartIndex, subsecEndIndex;
        subsecStartIndex = subsecEndIndex = 0;

        // Get containing section
        int[] bounds = getSectionBounds(lines, section);
        if (bounds == null) {
            return null; // Return null if given an invalid section
        }

        // Get the ending condition for the subsection
        String stopSubsec = null;
        if (subsec == SUBSEC_DEFINE) {
            stopSubsec = SUBSEC_PLACE;
        }
        else if (subsec == SUBSEC_PLACE) {
            stopSubsec = SEC_END + SECTION_TEXTURE;
        }
        else {
            return null; // Given an invalid subsection
        }

        for (String line : lines.subList(bounds[0], bounds[1] + 1)) {
            // Find starting position
            if (line.contains(subsec)) {
                subsecStartIndex = lines.indexOf(line);
            }

            // Find ending position
            if (line.contains(stopSubsec)) {
                subsecEndIndex = lines.indexOf(line);
            }
        }

        if (subsecStartIndex > 0 && subsecEndIndex > 0) {
            return new int[]{subsecStartIndex, subsecEndIndex};
        } else {
            return null;
        }
    }


    private static ArrayList<String> loadRoomInfo(File drmFile)
            throws IOException, FileNotFoundException {
        ArrayList<String> fileLines = new ArrayList<>();

        // Read all the lines in the file
        try (BufferedReader br = new BufferedReader(new FileReader(drmFile))){
            while (br.ready()) {
                fileLines.add(br.readLine());
            }
        }
        catch (FileNotFoundException e) {
            // This should not happen
            System.out.println(e.toString());
        }
        catch (IOException e) {
            throw new IOException("Given file is invalid");
        }

        // Preprocess lines
        fileLines = preprocessFileLines(fileLines);

        // Check file validity
        checkFileLines(fileLines);

        return fileLines;
    }


    private static String[] extractBaseTextures(ArrayList<String> roomLines) {
        String[] textures = new String[2];

        // Get definition bounds
        int[] defBound = getSubsecBounds(roomLines,
                SECTION_TEXTURE, SUBSEC_DEFINE);

        // Find base wall and floor
        for (String line : roomLines.subList(defBound[0], defBound[1])) {
            if (line.contains(VAL_BASE_WALL)) {
                // Pull the texture path. Don't need symbol
                textures[BASE_WALL_INDEX] = line.split(":")[1];
            }
            else if (line.contains(VAL_BASE_FLOOR)) {
                // Pull the texture path. Don't need symbol
                textures[BASE_FLOOR_INDEX] = line.split(":")[1];
            }
        }

        return textures;
    }


    private static HashMap<String, String> extractTextureSymbols(
            ArrayList<String> roomLines
    ) {
        HashMap<String, String> textureSymbols = new HashMap<>();

        // Get definition bounds
        int[] defBound = getSubsecBounds(roomLines,
                SECTION_TEXTURE, SUBSEC_DEFINE);

        // Find all textures bar base wall and floor
        for (String line : roomLines.subList(defBound[0] + 1, defBound[1])) {
            // FIXME This implementation stops any lines that contain the text,
            // not just the default symbols.
            if (line.contains(VAL_BASE_WALL) || line.contains(VAL_BASE_FLOOR)) {
                continue;
            }

            String[] lineArgs = line.split(":");

            String symbol = lineArgs[0];
            String relPath = lineArgs[1];

            textureSymbols.put(symbol, relPath);
        }

        return textureSymbols;
    }


    private static HashMap<GridPoint2, String> extractTextureLocations(
            ArrayList<String> roomLines
    ) {
        HashMap<GridPoint2, String> texturePositions = new HashMap<>();

        // Get place bounds
        int[] plcBound = getSubsecBounds(roomLines,
                SECTION_TEXTURE, SUBSEC_PLACE);

        // Find all textures and their corresponding positions
        for (String line : roomLines.subList(plcBound[0] + 1, plcBound[1])) {
            String[] lineArgs = line.split(":");
            String[] posArgs = lineArgs[0].split(",");

            GridPoint2 position = new GridPoint2(
                    Integer.parseInt(posArgs[0]),
                    Integer.parseInt(posArgs[1])
            );
            String symbol = lineArgs[1];

            texturePositions.put(position, symbol);
        }

        return texturePositions;
    }


    private static MapRoom parseRoomInfo(ArrayList<String> roomLines) {

        // Get room dimensions
        String[] dims = roomLines.get(DIMENSION_INDEX).split(",");
        GridPoint2 dimensions = new GridPoint2(
                Integer.parseInt(dims[0]),
                Integer.parseInt(dims[1])
        );

        // Get room offset
        String[] off = roomLines.get(OFFSET_INDEX).split(",");
        GridPoint2 offset = new GridPoint2(
                Integer.parseInt(off[0]),
                Integer.parseInt(off[1])
        );

        // Get base textures
        String[] baseTextures = extractBaseTextures(roomLines);

        // Get texture to symbol map
        HashMap<String, String> textureSymbols =
                extractTextureSymbols(roomLines);

        // Get texture positions
        HashMap<GridPoint2, String> textureLocations =
                extractTextureLocations(roomLines);

        // Create new room
        MapRoom newRoom = new MapRoom(
                dimensions,
                offset,
                baseTextures,
                textureSymbols,
                textureLocations
        );

        return newRoom;
    }


    /* Public methods */
    public static MapRoom loadRoom(File drmFile)
            throws IOException, FileNotFoundException {

        // Load room info. Will throw FileNotFoundException if given a bad drm
        ArrayList<String> roomLines = loadRoomInfo(drmFile);

        // Parse lines and generate MapRoom object
        MapRoom newRoom = parseRoomInfo(roomLines);

        return newRoom;
    }

    public static void main(String[] args) throws Exception {
        // Testing
        System.out.println("Well Howdy!");
        File f = new File("C:\\Users\\Ethan\\Documents\\_Uni\\DECO2800\\Game\\2021-studio-7\\source\\maps\\m1\\r1.drm");
        try {
            MapRoom r = loadRoom(f);
        } catch (Exception e) {
            throw e;
        }
    }
}