package com.deco2800.game.maps;

import com.badlogic.gdx.utils.Array;
import com.deco2800.game.maps.floor.Floor;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.player.CameraComponent;

public class Home {
    private Array<Floor> floors;
    private CameraComponent cameraComponent;
    public Entity player;

    public Home(CameraComponent cameraComponent) {
        this.floors = new Array<>();
        this.cameraComponent = cameraComponent;
    }

    public void create() {
        floors.add(new Floor(cameraComponent));
        floors.get(0).create();
    }
}
