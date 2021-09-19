package com.deco2800.game.entities.components.interactions.Actions;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.screens.maingame.MainGameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MumActions extends InteractionComponent {

    private static final Logger logger = LoggerFactory.getLogger(MumActions.class);

    @Override
    public void create() {
        super.create();
        targetLayer = PhysicsLayer.PLAYER;
        animator.startAnimation("standing_south");
    }

    @Override
    public void onCollisionStart(Fixture me, Fixture other) {
        Entity target = preCollisionCheck(me, other);
        if (target != null && target.getComponent(PlayerActions.class) != null) {
            triggerLoseCondition();
        }
    }

    public void triggerLoseCondition() {
        logger.info("MUM started collision with PLAYER, triggering lose condition");
        ((MainGameScreen) ServiceLocator.getGame().getScreen())
                .getMainGameEntity().getEvents().trigger("loss_caught");
    }
}
