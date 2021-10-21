package com.deco2800.game.entities.components.object;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;

public class CatActions extends InteractionComponent {
    private static final String UPDATE_ANIMATION = "update_animation";

    @Override
    public void onCollisionStart(Entity target) {
        if (target.getComponent(PlayerActions.class) != null) {
            entity.getEvents().trigger(UPDATE_ANIMATION, "licking");
        }
    }
}