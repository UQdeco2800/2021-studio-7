package com.deco2800.game.entities.components.interactions.Actions;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.InteractionComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.physics.PhysicsLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlaceableBoxActions extends InteractionComponent {

    private static final Logger logger = LoggerFactory.getLogger(PlaceableBoxActions.class);
    private boolean boxHighlighted = false;
    private boolean boxMoved = false;

    @Override
    public void create() {
        super.create();
        targetLayer = PhysicsLayer.PLAYER;
        if (animator != null) {
            animator.startAnimation("box");
        }
    }

    @Override
    public void onCollisionStart(Entity target) {
        if (target != null && target.getComponent(PlayerActions.class) != null) {
            logger.info("BOX started collision with PLAYER");
            highlightBox();
        }
    }

    @Override
    public void onCollisionEnd(Entity target) {
        if (target != null && target.getComponent(PlayerActions.class) != null) {
            logger.info("BOX ended collision with PLAYER");
            deHighlightBox();
        }
    }

    @Override
    public void onInteraction(Entity target) {
    if (target != null && target.getComponent(PlayerActions.class) != null) {
            logger.info("BOX started interaction with SURVEYOR");
        }
    }

    private void highlightBox() {
        boxHighlighted = true;
        handleBoxHighlight();
    }

    private void deHighlightBox() {
        boxHighlighted = false;
        handleBoxHighlight();
    }

    private void toggleBoxHighlight() {
        boxHighlighted = !boxHighlighted;
        handleBoxHighlight();
    }

    private void handleBoxHighlight() {
        if (animator == null) {
            return;
        }

        if (boxHighlighted) {
            animator.startAnimation("box_highlight");
        }
        else {
            animator.startAnimation("box");
        }
    }
}
