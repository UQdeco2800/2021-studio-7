package com.deco2800.game.entities.components.interactions.Actions;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.InteractionComponent;
import com.deco2800.game.entities.components.interactions.SingleUse;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.screens.maingame.MainGameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

public class DrinkActions extends InteractionComponent {

    // Note this class requires the addition of the SingleUse component be added to obstacle entity
    private static final Logger logger = LoggerFactory.getLogger(DrinkActions.class);


    @Override
    public void create() {
        super.create();
        targetLayer = PhysicsLayer.PLAYER;
        animator.startAnimation("energy");
    }


    @Override
    public void onCollisionStart(Fixture me, Fixture other) {
        Entity target = preCollisionCheck(me, other);
        if (target == null) {
            return;
        } else if (target.getComponent(PlayerActions.class) != null) {
            logger.info("DRINK started collision with PLAYER, highlighting " +
                    "drink");
            animator.startAnimation("energy_highlight");
        }
    }

    @Override
    public void onCollisionEnd(Fixture me, Fixture other) {
        Entity target = preCollisionCheck(me, other);
        if (target == null) {
            return;
        } else if (target.getComponent(PlayerActions.class) != null) {
            logger.info("DRINK ended collision with PLAYER, un-highlighting drink");
            animator.startAnimation("energy");
        }
    }

    @Override
    public void onInteraction(Entity target) {
        if (target == null) {
            return;
        } else if (target.getComponent(PlayerActions.class) != null) {
            logger.info("DRINK started interaction with PLAYER, modifying player base speed");
            target.getEvents().trigger("drink_energy_drink");
            entity.getComponent(SingleUse.class).remove();
            //add time restriction
        }
    }
}

/*
class DrinkTimerTask extends TimerTask{

    private Entity target;

    private static final Logger logger = LoggerFactory.getLogger(DrinkTimerTask.class);

    public DrinkTimerTask(Entity target){
        this.target = target;
        logger.info("TimerTask for DrinkActions created, task will run in 10 seconds");
    }

    @Override
    public void run() {
        target.getEvents().trigger("energyDrinkEnd");
        logger.info("TimerTask for Drink item executed, modifying player speed");
    }
}*/
