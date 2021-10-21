package com.deco2800.game.entities.components.npc;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.maingame.MainGameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MumActions extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(MumActions.class);

    @Override
    public void create() {
        super.create();
        entity.getEvents().trigger("update_animation", "standing_south");
    }

    @Override
    public void onCollisionStart(Entity target) {
        if (target.getComponent(PlayerActions.class) != null) {
            triggerPlayerCaught();
        }
    }

    private void triggerPlayerCaught() {
        logger.debug("MUM started collision with PLAYER, triggering player caught");
        ServiceLocator.getScreen(MainGameScreen.class)
                .getMainGameEntity().getEvents().trigger("player_caught");
    }
}
