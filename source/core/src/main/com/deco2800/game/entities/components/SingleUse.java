package com.deco2800.game.entities.components;

import com.badlogic.gdx.utils.Disposable;
import com.deco2800.game.generic.Component;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.components.AnimationRenderComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SingleUse extends Component implements Disposable {
    private static final Logger logger = LoggerFactory.getLogger(SingleUse.class);

    public SingleUse(){}

    /*
    @Override
    public void create(){
        entity.getEvents().addListener("interaction",  this::remove);
    }*/


    //Removes objects from map, but not from game
    public void remove(){
        entity.getComponent(AnimationRenderComponent.class).dispose();
        entity.getComponent(PhysicsComponent.class).dispose();
        entity.getComponent(HitboxComponent.class).dispose();
        entity.getComponent(ColliderComponent.class).dispose();
        logger.info("Animation, Physics, Hitbox and Collider components disposed of");
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
