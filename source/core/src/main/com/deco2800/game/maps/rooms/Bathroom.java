package com.deco2800.game.maps.rooms;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;

public class Bathroom extends Room {

    private BathroomInterior interior = null;

    @Override
    public void create(GridPoint2 offset, Vector2 dimensions) {
        if (!created) {
            super.create(offset, dimensions);
            interior = designateInterior(BathroomInterior.class, dimensions,
                    RoomProperties.ROOM_CLASS_TO_PATH.get(this.getClass()));
        }
        created = true;
    }

    public BathroomInterior getInterior() {
        return interior;
    }

    static public class BathroomInterior extends RoomInterior {
    }
}