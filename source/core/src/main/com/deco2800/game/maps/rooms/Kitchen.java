package com.deco2800.game.maps.rooms;

public class Kitchen extends Room {

    @Override
    public void create() {
        if (!created) {
            super.create();
        }
        created = true;
    }
}
