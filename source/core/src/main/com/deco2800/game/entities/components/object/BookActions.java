package com.deco2800.game.entities.components.object;

import com.deco2800.game.chores.ChoreList;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;
import com.deco2800.game.entities.components.SingleUse;
import com.deco2800.game.entities.components.player.KeyboardPlayerInputComponent;
import com.deco2800.game.entities.components.player.PlayerActions;

import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.rendering.components.AnimationRenderComponent;
import com.deco2800.game.screens.maingame.MainGameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookActions extends InteractionComponent {
    // Note this class requires the addition of the SingleUse component be added to obstacle entity
    private static final Logger logger = LoggerFactory.getLogger(BookActions.class);
    private static final String UPDATE_ANIMATION = "update_animation";
    private long startTime;
    private boolean hasInteracted = false;

    @Override
    public void create() {
        super.create();
        entity.getEvents().trigger(UPDATE_ANIMATION, "dropped_book");
    }

    @Override
    public void onInteraction(Entity target) {
        if (target.getComponent(PlayerActions.class) != null) {
            logger.debug("PLAYER interacted with Book");
            startTime = ServiceLocator.getTimeSource().getTime();
            hasInteracted = true;
//            target.getEvents().trigger("drink_energy_drink");
            entity.getEvents().trigger(UPDATE_ANIMATION, "dust1");
        }
    }

    @Override
    public void toggleHighlight(boolean shouldHighlight) {
    }

    @Override
    public void update(){
        long currentTime = ServiceLocator.getTimeSource().getTime();
        if (currentTime - startTime >= 1000L && hasInteracted){
            entity.getComponent(SingleUse.class).remove();
//            String string = "You drank a can of Dountain Mew. Yum!";
//            ServiceLocator.getScreen(MainGameScreen.class)
//                    .getMainGameEntity().getEvents().trigger("create_textbox", string);
//            target.getComponent(KeyboardPlayerInputComponent.class).setBuffed();
//            target.getComponent(PlayerActions.class).toggleEnergyDrinkConsumed();
            //add time restriction
            entity.getEvents().trigger("chore_complete", ChoreList.BOOKS);
        }

    }
}