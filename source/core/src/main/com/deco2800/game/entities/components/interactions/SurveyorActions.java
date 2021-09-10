package com.deco2800.game.entities.components.interactions;

import com.deco2800.game.physics.PhysicsLayer;

public class SurveyorActions extends InteractionComponent {

    @Override
    public void create() {
        super.create();
        targetLayer = PhysicsLayer.OBSTACLE;
    }
}
