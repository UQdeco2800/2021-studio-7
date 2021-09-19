package com.deco2800.game.areas.rooms;

import com.badlogic.gdx.math.Vector2;

public class Living extends Room {
    public Living(Vector2 roomScale, RoomObject[] tileDefinitions, RoomObject[] entityDefinitions, String[][] tileGrid, String[][] entityGrid) {
        super(roomScale, tileDefinitions, entityDefinitions, tileGrid, entityGrid);
    }

    static public class LivingStyle {

    }
}
