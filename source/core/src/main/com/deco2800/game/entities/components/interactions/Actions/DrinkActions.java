package com.deco2800.game.entities.components.interactions.Actions;

import com.deco2800.game.chores.ChoreList;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.InteractionComponent;
import com.deco2800.game.entities.components.interactions.SingleUse;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.maingame.MainGameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrinkActions extends InteractionComponent {
    // Note this class requires the addition of the SingleUse component be added to obstacle entity
    private static final Logger logger = LoggerFactory.getLogger(DrinkActions.class);


    @Override
    public void create() {
        super.create();
        animator.startAnimation("energy");
    }

    @Override
    public void onInteraction(Entity target) {
        if (target.getComponent(PlayerActions.class) != null) {
            logger.debug("PLAYER interacted with DRINK, increasing player stamina");
            target.getEvents().trigger("drink_energy_drink");
            entity.getComponent(SingleUse.class).remove();
            String string = "You drank a can of Dountain Mew. Yum!";
            ((MainGameScreen) ServiceLocator.getGame().getScreen())
                    .getMainGameEntity().getEvents().trigger("create_textbox", string);
            //add time restriction
            entity.getEvents().trigger("chore_complete", ChoreList.DRINK);
        }
    }

    @Override
    public void toggleHighlight(boolean shouldHighlight) {
        if (shouldHighlight) {
            logger.debug("DRINK started collision with PLAYER");
            animator.startAnimation("energy_highlight");
        } else {
            logger.debug("DRINK ended collision with PLAYER");
            animator.startAnimation("energy");
        }
    }

    @Override
    public void update() {
        super.update();
    }
}

// You should use update() to do time-related tasks instead

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
