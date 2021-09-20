package com.deco2800.game.areas.home.rooms;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.home.RoomObject;

public class Bedroom extends Room {
    public Bedroom(Vector2 roomScale, RoomObject[] tileDefinitions, RoomObject[] entityDefinitions, String[][] tileGrid, String[][] entityGrid) {
        super(roomScale, tileDefinitions, entityDefinitions, tileGrid, entityGrid);
    }

    static public class BedroomStyle {

    }
}
