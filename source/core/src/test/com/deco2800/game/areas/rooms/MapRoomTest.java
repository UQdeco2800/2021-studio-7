package com.deco2800.game.areas.rooms;

import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;

@ExtendWith(GameExtension.class)
class MapRoomTest {
    GridPoint2 g0, g1, g2, g3;
    String t0, t1;

    HashMap<String, String> bm0;
    HashMap<GridPoint2, String> bm1;

    @BeforeEach
    void setUp() {
        g0 = new GridPoint2(0, 0);
        g1 = new GridPoint2(2, 2);
        g2 = new GridPoint2(5, 5);
        g3 = new GridPoint2(10, 10);

        String t0 = "path/to/file0";
        String t1 = "path/to/file1";

        bm0= new HashMap<>();
        bm1= new HashMap<>();
    }

    @Test
    void getRoomBounds() {

        // Create room with filler details
        MapRoom testRoom = new MapRoom(
                g3, g2, new String[]{t0, t1}, bm0, bm1
        );

        // Calculate bounds
        GridPoint2 c0 = g2;
        GridPoint2 c1 = g3.add(g2);
        GridPoint2[] bounds = new GridPoint2[]{c0, c1};

        // Check bounds
        int i;
        for (i = 0; i < testRoom.getRoomBounds().length; i++) {
            assert (testRoom.getRoomBounds()[i] == bounds[i]);
        }

        // Ensure correct bound count
        assert (i == bounds.length);
    }

    @Test
    void isInBounds() {
        // Create test room
        MapRoom testRoom = new MapRoom(
                g2, g3, new String[]{t0, t1}, bm0, bm1
        );

        // Check bounds - use gridpoint method as it converts to x/y method
        assert (!(testRoom.isInBounds(g0) && testRoom.isInBounds(g3)));
        assert (testRoom.isInBounds(g2));
    }

    @Test
    void getSymbolsToPaths() {
        // Construct test values
        String symbol, path;
        GridPoint2 pos;
        HashMap<String, String> symbolsToPaths = new HashMap<>();
        HashMap<GridPoint2, String> positionsToSymbols = new HashMap<>();
        HashMap<GridPoint2, String> positionsToPaths = new HashMap<>();

        symbol = "texture";
        path = "path/to/texture";
        pos = new GridPoint2(1, 1);

        // Room construction values
        symbolsToPaths.put(symbol, path);
        positionsToSymbols.put(pos, symbol);
        // Map to test against
        positionsToPaths.put(pos, path);

        // Create a room
        MapRoom testRoom = new MapRoom(
                g1, g0, new String[]{t0, t1}, symbolsToPaths, positionsToSymbols
        );

        // Check method
        assert (testRoom.getSymbolsToPaths().equals(positionsToPaths));
    }

    @Test
    void testToString() {
        bm0.put("Symbol", "another/texture/path");
        String[] base = new String[]{t0, t1};

        // Create test room
        MapRoom testRoom = new MapRoom(
                g1, g0, base, bm0, bm1
        );

        // Construct comparison string
        String sep = "; ";
        int numTextures = base.length + bm0.size();
        String str = g1.toString() + sep +
                g0.toString() + sep +
                "Textures: " + numTextures;

        assert (str.equals(testRoom.toString()));
    }
}