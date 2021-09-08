package com.deco2800.game.entities;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.generic.Component;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.components.HitboxComponent;

public class InteractableComponent extends Component {

    protected HitboxComponent hitboxComponent;

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("collision_start", this::onCollisionStart);
        entity.getEvents().addListener("collision_end", this::onCollisionEnd);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }

    public void onCollisionStart(Fixture me, Fixture other) {}

    public void onCollisionEnd(Fixture me, Fixture other) {}
}
