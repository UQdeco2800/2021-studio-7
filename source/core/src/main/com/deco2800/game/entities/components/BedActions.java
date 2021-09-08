package com.deco2800.game.entities.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.Interactable;
import com.deco2800.game.entities.InteractableComponent;
import com.deco2800.game.entities.components.player.PlayerActions;
import com.deco2800.game.generic.Component;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.rendering.components.AnimationRenderComponent;
import com.deco2800.game.screens.maingame.MainGameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BedActions extends InteractableComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainGameScreen.class);

    @Override
    public void onCollisionStart(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            // Not triggered by hitbox, ignore
            return;
        }

        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        if (target.getComponent(PlayerActions.class) != null) {
            entity.getComponent(AnimationRenderComponent.class).startAnimation("bed_highlight");
        }
    }

    @Override
    public void onCollisionEnd(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            // Not triggered by hitbox, ignore
            return;
        }

        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        if (target.getComponent(PlayerActions.class) != null) {
            entity.getComponent(AnimationRenderComponent.class).startAnimation("bed");
        }
    }
}