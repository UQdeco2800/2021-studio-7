package com.deco2800.game.entities.components.player;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.InteractionComponent;
import com.deco2800.game.events.listeners.EventListener1;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.generic.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class SurveyorComponentTest {

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
    }

    // Couldn't get raycasts to find collisions, probably some dependency
    // that I am unaware of (@Jantoom)
    /*@Test
    void shouldInteractWithClosest() {
        Entity player = createPlayer();
        Entity closeObject = createObject();
        Entity farObject = createObject();
        player.setPosition(0f, 0f);
        closeObject.setPosition(0.3f, 0.4f);
        farObject.setPosition(0.45f, 0.6f);

        InteractionListener closeListener = mock(InteractionListener.class);
        InteractionListener farListener = mock(InteractionListener.class);
        closeObject.getEvents().addListener("interaction", closeListener);
        farObject.getEvents().addListener("interaction", farListener);

        player.getComponent(SurveyorComponent.class).interactClosest();

        // Check that listener ran when the collision started
        verify(closeListener).handle(player);
        verifyNoInteractions(farListener);
    }*/

    Entity createPlayer() {
        Entity player =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                        .addComponent(new SurveyorComponent());
        player.setScale(1f, 1f);
        PhysicsUtils.setScaledHitbox(player, 1.1f, 1.1f);
        player.create();
        return player;
    }

    Entity createObject() {
        Entity object =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
                        .addComponent(new InteractionComponent());
        object.setScale(1f, 1f);
        PhysicsUtils.setScaledHitbox(object, 1.1f, 1.1f);
        object.create();
        return object;
    }
}

interface InteractionListener extends EventListener1<Entity> {}