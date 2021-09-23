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

public class BedActions extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(BedActions.class);

    @Override
    public void create() {
        super.create();
        if (animator != null) {
            animator.startAnimation("bed");
        }
    }

    @Override
    public void onCollisionStart(Fixture me, Fixture other) {
        super.onCollisionStart(me, other);
        highlightBed();
    }

    @Override
    public void onCollisionEnd(Fixture me, Fixture other) {
        super.onCollisionEnd(me, other);
        unhighlightBed();
    }

    @Override
    public void onInteraction(Entity target) {
        if (target != null && target.getComponent(PlayerActions.class) != null) {
            triggerWinCondition();
        }
    }

    public void highlightBed() {
        logger.info("BED started collision with PLAYER, highlighting bed");
        if (animator != null) {
            animator.startAnimation("bed_highlight");
        }
    }

    public void unhighlightBed() {
        logger.info("BED ended collision with PLAYER, un-highlighting bed");
        if (animator != null) {
            animator.startAnimation("bed");
        }
    }

    public void triggerWinCondition() {
        logger.info("PLAYER interacted with BED, triggering win");
        ((MainGameScreen) ServiceLocator.getGame().getScreen())
                .getMainGameEntity().getEvents().trigger("win_default");
    }
}