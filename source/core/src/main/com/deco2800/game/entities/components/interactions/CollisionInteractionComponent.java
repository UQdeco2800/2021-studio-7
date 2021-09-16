package com.deco2800.game.entities.components.interactions;


import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.Actions.DrinkActions;
import com.deco2800.game.entities.components.player.PlayerActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollisionInteractionComponent extends InteractionComponent {
    private static final Logger logger = LoggerFactory.getLogger(CollisionInteractionComponent.class);

    public void create(){
        super.create();
    }

    @Override
    public void onCollisionStart(Fixture me, Fixture other) {
        Entity target = preCollisionCheck(me, other);
        if (target == null) {
            return;
        } else if (target.getComponent(PlayerActions.class) != null) {
            logger.info("onCollisionStart called on CollisionInteraction between Player and {}", entity);
            onInteraction(target);
        }
    }

    @Override
    public void onCollisionEnd(Fixture me, Fixture other) {
        Entity target = preCollisionCheck(me, other);
        if (target == null) {
            return;
        } else if (target.getComponent(PlayerActions.class) != null) {
            logger.info("onCollisionEnd called on CollisionInteraction between Player and {}", entity);
        }
    }

    @Override
    public void onInteraction(Entity target) {}
}
