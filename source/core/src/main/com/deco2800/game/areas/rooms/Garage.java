package com.deco2800.game.areas.rooms;

import com.badlogic.gdx.math.Vector2;

public class Garage extends Room {
    public Garage(Vector2 roomScale, RoomObject[] tileDefinitions, RoomObject[] entityDefinitions, String[][] tileGrid, String[][] entityGrid) {
        super(roomScale, tileDefinitions, entityDefinitions, tileGrid, entityGrid);
    }

    static public class GarageStyle {

    }
}
