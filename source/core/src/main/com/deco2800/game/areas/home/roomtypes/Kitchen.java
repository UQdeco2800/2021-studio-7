package com.deco2800.game.areas.home.roomtypes;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.home.RoomObject;

public class Kitchen extends RoomType {
    public Kitchen(Vector2 roomScale, RoomObject[] tileDefinitions, RoomObject[] entityDefinitions, String[][] tileGrid, String[][] entityGrid) {
        super(roomScale, tileDefinitions, entityDefinitions, tileGrid, entityGrid);
    }

    static public class KitchenStyle {

    }
}
