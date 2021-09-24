package com.deco2800.game.maps.rooms;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

public class Garage extends Room {

    private GarageInterior interior;

    @Override
    public void create(GridPoint2 offset, Vector2 dimensions) {
        if (!created) {
            super.create(offset, dimensions);
            interior = designateInterior(GarageInterior.class, dimensions,
                    RoomProperties.ROOM_CLASS_TO_PATH.get(this.getClass()));
        }
        created = true;
    }

    public GarageInterior getInterior() {
        return interior;
    }

    static public class GarageInterior extends RoomInterior {
    }
}
