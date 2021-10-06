package com.deco2800.game.entities.components.interactions.Actions;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.ai.components.AITaskComponent;
import com.deco2800.game.ai.tasks.MovementTask;
import com.deco2800.game.ai.tasks.SlipTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
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
        Entity target = preCollisionCheck(me, other);
        if (target == null) {
            return;
        } else if (target.getComponent(PlayerActions.class) != null) {
            logger.info("PEEL started collision with PLAYER, executing task");
            onInteraction(target);
        }
    }

    @Override
    public void onCollisionEnd(Fixture me, Fixture other) {
        Entity target = preCollisionCheck(me, other);
        if (target == null) {
            return;
        } else if (target.getComponent(PlayerActions.class) != null) {
            logger.info("PEEL ended collision with PLAYER");
        }
    }


    @Override
    public void onInteraction(Entity target){
        try {
            SlipTask slipTask = (SlipTask)
                    target.getComponent(AITaskComponent.class).getPriorityTask(SlipTask.class);
            slipTask.changePriority(1);
        } catch (NullPointerException e){
            logger.debug("SlipTask does not exist in player AIComponent");
        }
    }
}
