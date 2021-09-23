package com.deco2800.game.maps.rooms;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

public class Dining extends Room {

    private DiningInterior interior;

    @Override
    public void create(GridPoint2 offset, Vector2 dimensions) {
        super.create(offset, dimensions);
        interior = designateInterior(DiningInterior.class, dimensions,
                RoomProperties.ROOM_CLASS_TO_PATH.get(this.getClass()));
    }

    public DiningInterior getInterior() {
        return interior;
    }

    static public class DiningInterior extends RoomInterior {
    }
}
