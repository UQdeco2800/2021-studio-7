package com.deco2800.game.maps.rooms;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

public class Kitchen extends Room {

    private KitchenInterior interior;

    @Override
    public void create(GridPoint2 offset, Vector2 dimensions) {
        if (!created) {
            super.create(offset, dimensions);
            interior = designateInterior(KitchenInterior.class, dimensions,
                    RoomProperties.ROOM_CLASS_TO_PATH.get(this.getClass()));
        }
        created = true;
    }

    public KitchenInterior getInterior() {
        return interior;
    }

    static public class KitchenInterior extends RoomInterior {
    }
}
