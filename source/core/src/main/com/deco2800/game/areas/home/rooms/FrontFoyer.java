package com.deco2800.game.areas.home.rooms;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.deco2800.game.areas.home.Room;
import com.deco2800.game.areas.home.RoomObject;
import com.deco2800.game.files.FileLoader;

public class FrontFoyer extends Room {

    private static final String directory = "maps/frontfoyer";
    private FrontFoyerInterior interior;

    public FrontFoyer(Integer maxDoorways, ObjectMap<Class<Room>, String[]> doorwayRestrictions) {
        super(maxDoorways, doorwayRestrictions);
    }

    public FrontFoyerInterior getInterior() {
        return interior;
    }

    public void setInterior(FrontFoyerInterior interior) {
        this.interior = interior;
    }

    public void setInterior(Vector2 dimensions) {
        this.interior = FileLoader.loadRandomRoomInterior(FrontFoyerInterior.class, dimensions, directory);
    }

    static public class FrontFoyerInterior extends RoomInterior {

        public FrontFoyerInterior(ObjectMap<Character, RoomObject> tileMappings,
                              ObjectMap<Character, RoomObject> entityMappings,
                              Character[][] tileGrid, Character[][] entityGrid) {
            super(tileMappings, entityMappings, tileGrid, entityGrid);
        }
    }
}
