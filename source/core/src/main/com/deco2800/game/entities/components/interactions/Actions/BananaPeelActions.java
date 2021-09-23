package com.deco2800.game.entities.components.interactions.Actions;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BananaPeelActions extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(BananaPeelActions.class);

    //private Vector2 slipstream = Vector2(3,3);

    @Override
    public void create(){
        super.create();
        targetLayer = PhysicsLayer.PLAYER;
        animator.startAnimation("banana_peel");
    }

    @Override
    public void onCollisionStart(Fixture me, Fixture other) {
        super.onCollisionStart(me, other);
        logger.info("PEEL started collision with PLAYER, executing task and removing object");
        onInteraction(((BodyUserData) other.getBody().getUserData()).entity);
    }

    @Override
    public void onCollisionEnd(Fixture me, Fixture other) {
        super.onCollisionEnd(me, other);
        logger.info("PEEL ended collision with PLAYER");
    }


    @Override
    public void onInteraction(Entity target){
        //Play some sort of slip animation
        //alter player Vector2
    }
}
