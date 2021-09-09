package com.deco2800.game.areas.rooms;

import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.HashMap;

@ExtendWith(GameExtension.class)
class MapLevelTest {
    GridPoint2 p0, p1, p2, s0, s1;
    MapRoom r0, r1, r2;
    ArrayList<MapRoom> rooms;

    @BeforeEach
    void setUp() {
        p0 = new GridPoint2(0, 0);
        p1 = new GridPoint2(10, 0);
        p2 = new GridPoint2(0, 10);
        s0 = new GridPoint2(10, 10);
        s1 = new GridPoint2(5, 5);

        r0 = new MapRoom(
            s0, p0, new String[]{"A", "B"},
                new HashMap<>(), new HashMap<>()
        );
        r1 = new MapRoom(
            s1, p1, new String[]{"C", "D"},
                new HashMap<>(), new HashMap<>()
        );
        r2 = new MapRoom(
            s1, p2, new String[]{"E", "F"},
                new HashMap<>(), new HashMap<>()
        );

        rooms = new ArrayList<>();
        rooms.add(r0);
        rooms.add(r1);
        rooms.add(r2);
    }

    @Test
    void getMaxSize() {
        int maxX = s0.x + s1.x;
        int maxY = s0.y + s1.y;
        GridPoint2 max = new GridPoint2(maxX, maxY);

        // Make test level
        MapLevel testLevel = new MapLevel(rooms);

        // Check size
        assert (max.equals(testLevel.getMaxSize()));
    }

    @Test
    void isInBounds() {
        // Make test level
        MapLevel testLevel = new MapLevel(rooms);

        /* Check with gridpoint format as it converts to x/y format */
        // Check in bounds
        assert (testLevel.isInBounds(p0) &&
                testLevel.isInBounds(p1) &&
                testLevel.isInBounds(p2));

        // Check out of bounds
        assert (!testLevel.isInBounds(testLevel.getMaxSize()));
    }

    @Test
    void findRoom() {
        // Make test level
        MapLevel testLevel = new MapLevel(rooms);

        // Check finding rooms
        GridPoint2 pos = r0.getRoomPosition();
        String id = r0.getBaseWallTexture();
        assert (testLevel.findRoom(pos).getBaseWallTexture().equals(id));

        // Check not finding room
        assert (testLevel.findRoom(testLevel.getMaxSize()) == null);
    }
}