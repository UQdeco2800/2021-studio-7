package com.deco2800.game.entities.components.object;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.maingame.MainGameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CatActions extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(CatActions.class);
    private static final String UPDATE_ANIMATION = "update_animation";


    @Override
    public void create() {
        super.create();

    }

    @Override
    public void onCollisionStart(Entity target) {
        if (target.getComponent(PlayerActions.class) != null) {
            entity.getEvents().trigger(UPDATE_ANIMATION, "licking");
        }
    }
}