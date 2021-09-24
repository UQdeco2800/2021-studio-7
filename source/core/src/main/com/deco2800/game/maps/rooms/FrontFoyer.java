package com.deco2800.game.maps.rooms;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

public class FrontFoyer extends Room {

    private FrontFoyerInterior interior;

    @Override
    public void create(GridPoint2 offset, Vector2 dimensions) {
        if (!created) {
            super.create(offset, dimensions);
            interior = designateInterior(FrontFoyerInterior.class, dimensions,
                    RoomProperties.ROOM_CLASS_TO_PATH.get(this.getClass()));
        }
    }

    public FrontFoyerInterior getInterior() {
        return interior;
    }

    static public class FrontFoyerInterior extends RoomInterior {
    }
}
