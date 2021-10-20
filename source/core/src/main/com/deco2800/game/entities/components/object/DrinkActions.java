package com.deco2800.game.entities.components.object;

import com.deco2800.game.chores.ChoreList;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;
import com.deco2800.game.entities.components.SingleUse;
import com.deco2800.game.entities.components.player.KeyboardPlayerInputComponent;
import com.deco2800.game.entities.components.player.PlayerActions;

import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.maingame.MainGameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrinkActions extends InteractionComponent {
    // Note this class requires the addition of the SingleUse component be added to obstacle entity
    private static final Logger logger = LoggerFactory.getLogger(DrinkActions.class);
    private static final String updateAnimation = "update_animation";

    @Override
    public void create() {
        super.create();
        entity.getEvents().trigger(updateAnimation, "energy");
    }

    @Override
    public void onInteraction(Entity target) {
        if (target.getComponent(PlayerActions.class) != null) {
            logger.debug("PLAYER interacted with DRINK, increasing player stamina");
            target.getEvents().trigger("drink_energy_drink");
            entity.getComponent(SingleUse.class).remove();
            String string = "You drank a can of Dountain Mew. Yum!";
            ServiceLocator.getScreen(MainGameScreen.class)
                    .getMainGameEntity().getEvents().trigger("create_textbox", string);
            target.getComponent(KeyboardPlayerInputComponent.class).setBuffed();
            target.getComponent(PlayerActions.class).toggleEnergyDrinkConsumed();
            //add time restriction
            entity.getEvents().trigger("chore_complete", ChoreList.DRINK);
            target.getEvents().trigger(updateAnimation, "standing_south_buffed");
        }
    }

    @Override
    public void toggleHighlight(boolean shouldHighlight) {
        if (shouldHighlight) {
            logger.debug("DRINK started collision with PLAYER");
            entity.getEvents().trigger(updateAnimation, "energy_highlight");
        } else {
            logger.debug("DRINK ended collision with PLAYER");
            entity.getEvents().trigger(updateAnimation, "energy");
        }
    }
}