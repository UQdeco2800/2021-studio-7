package com.deco2800.game.areas.rooms;

import com.badlogic.gdx.math.GridPoint2;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Utility class that handles the generation of MapRoom objects based on text
 * file contents.
 *
 * TODO Need to add a method to convert '\' or '/' symbols in texture paths to
 *      the system-based directory separator (File.separator)
 */
public class RoomLoader {
    /* drm file constants */
    // General
    public static final String DRM_FILE_EXT = ".drm";
    private static final int MIN_LINES = 8;
    private static final int BASE_WALL_INDEX = 0;
    private static final int BASE_FLOOR_INDEX = 1;
    private static final int DIMENSION_INDEX = 0;
    private static final int OFFSET_INDEX = 1;

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

    /**
     * Gets all files in a provided directory.
     *
     * @param path
     * String of the path pointing to a directory.
     *
     * @return
     * An array of the files contained within the provided directory.
     *
     * @throws InvalidPathException
     * If the given directory is not a directory, or does not contain any files.
     */
    private static File[] getPathFiles(String path)
            throws InvalidPathException
    {
        File mapDirectory = new File(path);

        File[] roomFiles = mapDirectory.listFiles();

        if(roomFiles == null) {
            throw new InvalidPathException(path,
                    "Provided path does not contain files.");
        }

        return roomFiles;
    }

    /**
     * Checks if a given path is a DRM file. That is, it checks for a '.drm'
     * filetype extension.
     *
     * @param path
     * The path to a file to check.
     *
     * @return
     * `true` if the path points to a DRM file, `false` otherwise.
     */
    private static boolean checkForDRM(String path) {
        // Check if the files at the given path end in ".drm"
        if (path.endsWith(DRM_FILE_EXT)) {
            return true;
        }

        return false;
    }

    /**
     * Gets all files in a directory that are DRM files.
     *
     * @param path
     * The directory to check for DRM files.
     *
     * @return
     * An array of files that end in ".drm". If none exist, it will return null.
     */
    private static File[] getDRMFiles(String path) throws InvalidPathException {
        ArrayList<File> drmFiles = new ArrayList<File>();

        // Get all files in path
        File[] allFiles = getPathFiles(path);

        // Check all files in path for .drm extension
        for (File file : allFiles) {
            if (checkForDRM(file.toString())) {
                drmFiles.add(file);
            }
        }

        // Check for empty list
        if (drmFiles.isEmpty()) {
            return null;
        }

        return (File[]) drmFiles.toArray(new File[drmFiles.size()]);
    }

    /**
     * Preprocesses file lines. Preprocessing involves trimming lines of leading
     * and trailing whitespace, and removes lines that are blank or comments.
     *
     * Currently, comments are only valid on lines with no other content. They
     * cannot be used at the end of an existing line.
     * TODO Allow comments on content lines
     *
     * @param lines
     * A list of all raw lines from a DRM file.
     *
     * @return
     * A list of lines that are ready to be parsed for room creation.
     */
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

    /**
     * Checks if the given lines are in a valid DRM file format. Checks are NOT
     * exhaustive, and serve as a rough guideline. Checks for:
     *      - File length (has mandatory lines)
     *      - Room dimensions and offset existence and format
     *      - Existence of texture section with 'DEFINE' and 'PLACE' subsections
     *      - Existence of mandatory 'BASE_...' texture definitions.
     *
     * The current implementation prioritises some form of readability over
     * performance.
     *
     * TODO Remake method uses newer getX() functions.
     * TODO Create helper methods to reduce method length.
     *
     * @param lines
     * The preprocessed lines from a DRM file.
     *
     * @return
     * Returns `true` if the list of lines is in a valid
     * @throws FileNotFoundException
     */
    private static boolean checkFileLines(ArrayList<String> lines)
            throws FileNotFoundException {

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

    /**
     * Gets indexes associated with the beginning and end of a given section.
     *
     * @param lines
     * The DRM file lines to find section indexes on.
     * @param sectionTitle
     * The name of the section to find indexes for. Section is specified with
     * defined section constants.
     *
     * @return
     * An array of two integers, corresponding to the lines that the section
     * starts and finishes on. Index is 0-based. Of the form '[start, stop]'.
     */
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

    /**
     * Gets indexes associated with the beginning and end of a given subsection.
     * A subsection is found solely inside a parent section.
     *
     * The stop condition for a 'DEFINE' subsection is the start of the 'PLACE'
     * subsection. The stop condition for a 'PLACE' subsection is the end of the
     * given section.
     *
     * @param lines
     * The DRM file lines to find subsection indexes on.
     * @param section
     * The name of the section to find indexes for. Section is specified with
     * defined section constants.
     * @param subsec
     * The name of the subsection to find indexes for. Subsection is specified
     * with defined subsection constants.
     *
     * @return
     * An array of two integers, corresponding to the lines that the subsection
     * starts and finishes on. Index is 0-based. Of the form '[start, stop]'.
     */
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

    /**
     * Loads a DRM file and returns all the lines inside it that are usable. The
     * lines will be preprocessed and checked for some validity.
     *
     * @param drmFile
     * The DRM file to get the information lines from.
     *
     * @return
     * A list of all usable/informative lines in the given file.
     *
     * @throws IOException
     * If the given file cannot be read from.
     * @throws FileNotFoundException
     * If the given file cannot be found. This should not be possible in normal
     * use-case.
     */
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
            throw new FileNotFoundException();
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

    /**
     * Takes the base texture information from a list of valid DRM file lines.
     *
     * @param roomLines
     * List of informative lines from a DRM file.
     *
     * @return
     * A String array with two values. Each value represents a relative path to
     * either the base wall or floor texture.
     */
    private static String[] extractBaseTextures(ArrayList<String> roomLines) {
        String[] textures = new String[2];

        // Get definition bounds
        int[] defBound = getSubsecBounds(roomLines,
                SECTION_TEXTURE, SUBSEC_DEFINE);

        // Find base wall and floor
        for (String line : roomLines.subList(defBound[0], defBound[1])) {
            if (line.contains(VAL_BASE_WALL)) {
                // Pull the texture path. Don't need symbol val
                textures[BASE_WALL_INDEX] =
                        line.split(":")[1].replace("\"", "");
            }
            else if (line.contains(VAL_BASE_FLOOR)) {
                // Pull the texture path. Don't need symbol val
                textures[BASE_FLOOR_INDEX] =
                        line.split(":")[1].replace("\"", "");
            }
        }

        return textures;
    }

    /**
     * Takes additional texture information (textures barring the base wall and
     * floor) from a list of valid DRM file lines. Maintains the texture and
     * symbol association.
     *
     * @param roomLines
     * List of informative lines from a DRM file.
     *
     * @return
     * A HashMap, mapping texture symbols to their relative texture paths. Here,
     * symbol is the key and texture path is the value.
     */
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
            //      not just the default symbols.
            //      Could try to use .startsWith()? Maybe with `+ ":"`
            if (line.contains(VAL_BASE_WALL) || line.contains(VAL_BASE_FLOOR)) {
                continue;
            }

            String[] lineArgs = line.split(":");

            String symbol = lineArgs[0];
            // Replace quotes in path
            String relPath = lineArgs[1].replace("\"", "");

            textureSymbols.put(symbol, relPath);
        }

        return textureSymbols;
    }

    /**
     * Takes the texture placement information from a list of valid DRM file
     * lines. Textures are identified by the symbols defined inside the given
     * DRM file lines.
     *
     * Does not handle missing symbol definitions or out-of-bounds textures.
     *
     * @param roomLines
     * List of informative lines from a DRM file.
     *
     * @return
     * HashMap of a 2d grid point representing the location inside the room that
     * the texture belongs mapped with the symbol used to define the desired
     * texture. The grid point is the key and the symbol the value.
     */
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

    /**
     * Generates a MapRoom object, based on a list of DRM file lines.
     *
     * @param roomLines
     * List of informative lines from a DRM file.
     *
     * @return
     * A MapRoom object constructed with the details found in the associated DRM
     * file.
     */
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
    /**
     * Generates a MapRoom object, based on the information from a valid DRM
     * file.
     * @param drmFile
     * The file to load information from, for room generation.
     *
     * @return
     * The generated MapRoom object.
     *
     * @throws IOException
     * If the DRM file cannot be read.
     * @throws FileNotFoundException
     * If given a bad DRM file. Should not happen in normal use.
     */
    public static MapRoom loadRoom(File drmFile)
            throws IOException, FileNotFoundException {

        // Load room info. Will throw FileNotFoundException if given a bad drm
        ArrayList<String> roomLines = loadRoomInfo(drmFile);

        // Parse lines and generate MapRoom object
        MapRoom newRoom = parseRoomInfo(roomLines);

        return newRoom;
    }

    /**
     * Loads all of the rooms in a map directory.
     *
     * @param directoryPath
     * Path to directory containing all of a map's rooms.
     *
     * @return
     * A list of all of the MapRoom objects that were generated.
     *
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static ArrayList<MapRoom> loadAllRooms(String directoryPath)
            throws IOException, FileNotFoundException
    {
        ArrayList<MapRoom> rooms = new ArrayList<>();

        // Load all room .drm files
        File[] files = getDRMFiles(directoryPath);

        // Finish if no rooms
        if (files == null) {
            return null;
        }

        // Convert each room file into a MapRoom
        for (File file : files) {
            rooms.add(loadRoom(file));
        }

        // Return null if there are no rooms
        if (rooms.isEmpty()) {
            return null;
        }

        return rooms;
    }
}