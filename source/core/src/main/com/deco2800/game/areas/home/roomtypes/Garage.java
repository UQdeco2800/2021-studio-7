package com.deco2800.game.areas.home.roomtypes;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.home.RoomObject;

public class Garage extends RoomType {
    public Garage(Vector2 roomScale, RoomObject[] tileDefinitions, RoomObject[] entityDefinitions, String[][] tileGrid, String[][] entityGrid) {
        super(roomScale, tileDefinitions, entityDefinitions, tileGrid, entityGrid);
    }

    static public class GarageStyle {

    }
}
