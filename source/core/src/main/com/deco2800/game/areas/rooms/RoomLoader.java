package com.deco2800.game.areas.rooms;

import com.badlogic.gdx.math.GridPoint2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Utility class that handles the generation of MapRoom objects based on text
 * file contents.
 *
 * TODO Consider swapping to libGDX FileHandles?
 *
 * TODO Remove throws?
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
    private static final int SYMBOL_INDEX = 0;
    private static final int PATH_INDEX = 1;
    private static final int LABEL_INDEX = 0;

    // Symbols
    private static final char COMMENT = '%';
    private static final char SEP = ',';
    private static final char ALLOC = '=';
    private static final char PAIR = ':';
    private static final char SEC_START = '<';
    private static final char SEC_END = '>';
    private static final char BLANK_CELL = '.';
    private static final char NON_CELL = ' ';

    // Patterns
    private static final Pattern LINE_COORD = Pattern.compile("\\d+,\\d+");
    private static final Pattern LINE_SECTION_START = Pattern.compile(".*<");
    private static final Pattern LINE_SECTION_END = Pattern.compile(">.*");
    private static final Pattern LINE_DEFINE = Pattern.compile(".*:\".*\"");
    private static final Pattern LINE_PLACE = Pattern.compile("\\d+,\\d+:.*");

    // Sections
    private static final String SECTION_TEXTURE = "TEXTURE";
    private static final String SECTION_MAP = "MAP";

    private static final String SUBSEC_DEFINE = "DEFINE";
    private static final String SUBSEC_PLACE = "PLACE";
    private static final String SUBSEC_ENTITIES = "ENTITIES";
    private static final String SUBSEC_GRID = "GRID";

    private static final HashMap<String, HashMap<String, String>> SECTIONS;

    // Defaults
    private static final String VAL_BASE_WALL = "BASE_WALL";
    private static final String VAL_BASE_FLOOR = "BASE_FLOOR";

    /* Values */
    private static final Logger logger =
            LoggerFactory.getLogger(RoomLoader.class);

    static {
        /*
         Track start and stop positions of each section's subsections. Key is
         the starting title, value is the ending title. Both key AND value must
         be unique.
         */
        HashMap<String, String> textureSubBounds = new HashMap<>();
        textureSubBounds.put(SUBSEC_DEFINE, SUBSEC_PLACE);
        textureSubBounds.put(SUBSEC_PLACE, SEC_END + SECTION_TEXTURE);

        HashMap<String, String> mapSubBounds = new HashMap<>();
        mapSubBounds.put(SUBSEC_ENTITIES, SUBSEC_GRID);
        mapSubBounds.put(SUBSEC_GRID, SEC_END + SECTION_MAP);

        // Track each of the sections
        SECTIONS = new HashMap<>();
        SECTIONS.put(SECTION_TEXTURE, textureSubBounds);
        SECTIONS.put(SECTION_MAP, mapSubBounds);
    }

    /* ~~~~~ Private methods ~~~~~ */
    /* ~~~ Getters ~~~ */
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
            logger.error("Was provided a path that did not contain any files.");
            throw new InvalidPathException(path,
                    "Provided path does not contain files.");
        }

        return roomFiles;
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
        ArrayList<File> drmFiles = new ArrayList<>();

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

        return drmFiles.toArray(new File[0]);
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
     * The stop condition for each subsection is defined in the class.
     * TODO extend for non-unique section names
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
        String stopSubsec = SECTIONS.get(section).get(subsec);

        // Check for invalid section
        if (stopSubsec == null) return null;

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
     * TODO DOCUMENT
     * @return
     */
    private static HashMap<GridPoint2, String> getSymbolsToPaths(
            HashMap<String, String> symToPath,
            HashMap<GridPoint2, String> locationToSym
    ) {
        HashMap<GridPoint2, String> locationToPaths = new HashMap<>();

        locationToSym.forEach((pos, symbol) ->
                locationToPaths.put(pos, symToPath.get(symbol)));

        return locationToPaths;
    }

    /* ~~~ Validity Checking ~~~ */
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
        return path.endsWith(DRM_FILE_EXT);
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
     * @throws FileNotFoundException
     * If the given file has invalid lines or formatting
     */
    private static void checkFileLineValidity(ArrayList<String> lines)
            throws FileNotFoundException {

        // Check file length
        if (lines.size() < MIN_LINES) return;

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
    }

    /* ~~~ Line Processing ~~~ */
    /**
     * Preprocesses file lines. Preprocessing involves trimming lines of leading
     * and trailing whitespace, and removes lines that are blank or comments.
     *
     * Currently, comments are only valid on lines with no other content. They
     * cannot be used at the end of an existing line.
     *
     * TODO Handle comments in strings?
     *
     * @param lines
     * A list of all raw lines from a DRM file.
     *
     * @return
     * A list of lines that are ready to be parsed for room creation.
     */
    private static ArrayList<String> processFileLines(
            ArrayList<String> lines) {
        boolean inGrid = false;

        // Construct new arraylist for processed lines
        ArrayList<String> newLines = new ArrayList<>();

        // TODO find way of handling tab and space
        for (String line : lines) {
            // Strip trailing whitespace line
            line = line.stripTrailing();

            // Detect map section
            if (line.contains(SUBSEC_GRID)) inGrid = true;
            else if (line.contains(SEC_END + SECTION_MAP)) inGrid = false;

            // Only strip leading whitespace for non-map sections
            if (!inGrid) line = line.stripLeading();

            // Remove all text after comment symbol
            line = line.split(String.valueOf(COMMENT))[0].stripTrailing();

            // Ignore comment lines and newlines
            if (line.length() != 0 && line.charAt(0) != COMMENT) {
                newLines.add(line);
            }
        }

        return newLines;
    }

    /* ~~~ File Information Extraction ~~~ */
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
            // TODO make more generic

            if (line.contains(VAL_BASE_WALL)) {
                // Pull the texture path. Don't need symbol val
                textures[BASE_WALL_INDEX] = line
                        .split(String.valueOf(ALLOC))[PATH_INDEX]
                        .replace("\"", "");
            }
            else if (line.contains(VAL_BASE_FLOOR)) {
                // Pull the texture path. Don't need symbol val
                textures[BASE_FLOOR_INDEX] = line
                        .split(String.valueOf(ALLOC))[PATH_INDEX]
                        .replace("\"", "");
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

            String[] lineArgs = line.split(String.valueOf(ALLOC));

            String symbol = lineArgs[SYMBOL_INDEX];
            // Replace quotes in path
            String relPath = lineArgs[PATH_INDEX].replace("\"", "");

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
        assert plcBound != null;
        for (String line : roomLines.subList(plcBound[0] + 1, plcBound[1])) {
            String[] lineArgs = line.split(String.valueOf(ALLOC));
            String[] posArgs = lineArgs[0].split(String.valueOf(SEP));

            GridPoint2 position = new GridPoint2(
                    Integer.parseInt(posArgs[0]),
                    Integer.parseInt(posArgs[1])
            );
            String symbol = lineArgs[1];

            texturePositions.put(position, symbol);
        }

        return texturePositions;
    }

    private static RoomGrid extractGrid(
            ArrayList<String> roomLines,
            String baseFloor,
            HashMap<GridPoint2, String> locationToPath
    ) {
        // Get the lines corresponding to the grid
        int[] gridBounds = getSubsecBounds(roomLines, SECTION_MAP, SUBSEC_GRID);
        ArrayList<String> gridLines = new ArrayList<>(roomLines
                .subList(gridBounds[0], gridBounds[1]));
        gridLines.remove(LABEL_INDEX);    // Remove subsection title

        // Null pointer safety
        assert (gridLines.size() > 1);

        // Get grid dimensions
        int height, width;
        height = gridLines.size();  // Subtract 1 for the grid title
        width = 0;
        // Iterate over grid lines. Start at 1 to skip grid section title
        for (int i = 0; i < height; i++) {
            if (gridLines.get(i).length() > width) {
                width = gridLines.get(i).length();
            }
        }

        // Fill grid
        RoomGrid grid = new RoomGrid(width, height);
        for (int i = 0; i < gridLines.size(); i++) { // Height
            String line = gridLines.get(i);
            for (int j = 0; j < line.length(); j++) { // Width
                String texturePath;
                switch (line.charAt(j)) {
                    case NON_CELL:
                        // No cell here
                        continue;
                    case BLANK_CELL:
                        // Place blank texture
                        texturePath = baseFloor; break;
                    default:
                        // Place from texture lookup
                        texturePath = locationToPath.get(new GridPoint2(j, i));
                        break;
                }

                grid.setGridCell(j, i, texturePath);
            }
        }

        return grid;
    }

    private static EntityHolder extractEntityHolder(String entityLine) {
        String[] entityArgs = entityLine.split(String.valueOf(ALLOC));

        // TODO make code better - magic values
        // Pull symbol and event
        char sym = entityArgs[0].charAt(0);
        String event = entityArgs[1].replace("\"", "");;

        // If there are no invocation arguments, return entity
        if (entityArgs.length == 2) return new EntityHolder(sym, event);

        // Remove wrapping braces
        String invocation = entityArgs[2];
        invocation = invocation.substring(1, invocation.length() - 1);

        // Get the invocation argument key value pairs
        HashMap<String, String> invocationArgs = new HashMap<>();

        // Split the invocation args based on key/value pair strings
        String[] invocationPairs = invocation.split(String.valueOf(SEP));

        // Split each of the key/value pairs
        for (String keyValPair : invocationPairs) {
            String[] pairArgs = keyValPair.split(String.valueOf(PAIR));
            // Add new invocation argument
            invocationArgs.put(pairArgs[0], pairArgs[1]);
        }

        return new EntityHolder(sym, event, invocationArgs);
    }

    private static void extractEntities(ArrayList<String> roomLines) {
        // Get info bounds
        int[] mapSectionBounds = getSectionBounds(roomLines, SECTION_MAP);
        int[] entityBounds =
                getSubsecBounds(roomLines, SECTION_MAP, SUBSEC_ENTITIES);
        int[] gridBounds =
                getSubsecBounds(roomLines, SECTION_MAP, SUBSEC_GRID);

        // Get entity lines
        ArrayList<String> entityLines = new ArrayList<>(roomLines
                .subList(entityBounds[0], entityBounds[1]));
        entityLines.remove(LABEL_INDEX);    // Remove subsection title

        // Create entity objects
        ArrayList<EntityHolder> entities = new ArrayList<>();
        for (String line : entityLines) {
            System.out.println(line);
            entities.add(extractEntityHolder(line));
        }
        System.out.println(entities);

        // Iterate over map
            // If there's a symbol in entities on map, add its position to hash
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

        // Get room dimensions TODO Remove
        String dimLine = roomLines.get(DIMENSION_INDEX);
        String[] dims = dimLine.split(String.valueOf(SEP));
        GridPoint2 dimensions = new GridPoint2(
                Integer.parseInt(dims[0]),
                Integer.parseInt(dims[1])
        );

        // Get room offset TODO Remove
        String offLine = roomLines.get(OFFSET_INDEX);
        String[] off = offLine.split(String.valueOf(SEP));
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

        // Extract map grid
        RoomGrid grid = extractGrid(roomLines, baseTextures[BASE_FLOOR_INDEX],
                getSymbolsToPaths(textureSymbols, textureLocations));
        System.out.println(grid);

        // TODO 

        // Extract entities
        extractEntities(roomLines);

        // Extract anchors

        // Create new room
        return new MapRoom(
                dimensions,
                offset,
                baseTextures,
                textureSymbols,
                textureLocations
        );
    }

    /* ~~~ Loading ~~~ */
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
            logger.error("Attempted to load a DRM file that does not exist.");
            throw new FileNotFoundException();
        }
        catch (IOException e) {
            throw new IOException("Given file is invalid");
        }

        // Preprocess lines
        fileLines = processFileLines(fileLines);

        // Check file validity
        checkFileLineValidity(fileLines);

        return fileLines;
    }

    /* ~~~~~ Public methods ~~~~~ */
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
        return parseRoomInfo(roomLines);
    }

    /**
     * Loads all of the rooms in a map directory.
     *
     * @param directoryPath
     * Path to directory containing all of a map's rooms.
     *
     * @return
     * A list of all MapRoom objects that were generated.
     *
     * @throws IOException
     * If the DRM file cannot be loaded.
     * @throws FileNotFoundException
     * If given a bad DRM file.
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

// TODO should this be in a new file?
class EntityHolder {
    private char sym;
    private String event;
    private HashMap<String, String> invocationArgs;

    public EntityHolder(char sym, String event) {
        this.sym = sym;
        this.event = event;
        this.invocationArgs = new HashMap<>();
    }

    public EntityHolder(
            char sym,
            String event,
            HashMap<String, String> invocationArgs
    ) {
        this.sym = sym;
        this.event = event;
        this.invocationArgs = invocationArgs;
    }

    public HashMap<String, String> getInvocationArgs() {
        return invocationArgs;
    }

    public void addArgs(String attr, String value) {
        this.invocationArgs.put(attr, value);
    }

    @Override
    public String toString() {
        return "EntityHolder{" +
                "sym=" + sym +
                ", event='" + event + '\'' +
                ", invocationArgs=" + invocationArgs +
                '}';
    }
}