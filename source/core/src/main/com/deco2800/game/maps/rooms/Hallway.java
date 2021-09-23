package com.deco2800.game.maps.rooms;

import com.badlogic.gdx.utils.ObjectMap;

@SuppressWarnings("unused")
public class Hallway extends Room {

    public Hallway(Integer maxDoorways, ObjectMap<Class<? extends Room>, String[]> doorwayRestrictions) {
        super(maxDoorways, doorwayRestrictions);
    }
}
