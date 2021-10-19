package com.deco2800.game.entities.components;

import com.badlogic.gdx.utils.Disposable;
import com.deco2800.game.generic.Component;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.physics.PhysicsEngine;
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
        ServiceLocator.getEntityService().scheduleEntityForRemoval(this.entity);
        logger.info("Object queued to be destroyed by physics engine");
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
