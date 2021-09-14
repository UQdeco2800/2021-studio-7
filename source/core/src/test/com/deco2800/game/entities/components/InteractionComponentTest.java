package com.deco2800.game.entities.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.InteractionComponent;
import com.deco2800.game.events.listeners.EventListener2;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.generic.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class InteractionComponentTest {

    Entity player;
    Entity object;
    Fixture playerFixture;
    Fixture objectFixture;
    InteractionListener listener;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        player = createPlayer();
        object = createObject();
        playerFixture = player.getComponent(HitboxComponent.class).getFixture();
        objectFixture = object.getComponent(HitboxComponent.class).getFixture();
        listener = mock(InteractionListener.class);
    }

    @Test
    void shouldTriggerOnCollisionStart() {
        object.getEvents().addListener("collision_start", listener);
        object.getEvents().trigger("collision_start", objectFixture, playerFixture);

        // Check that listener ran when the collision started
        verify(listener).handle(objectFixture, playerFixture);
    }

    @Test
    void shouldEndCollisionEvent() {
        object.getEvents().addListener("collision_end", listener);
        object.getEvents().trigger("collision_end", objectFixture, playerFixture);

        // Check that listener ran when the collision ended
        verify(listener).handle(objectFixture, playerFixture);
    }

    Entity createPlayer() {
        Entity player =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER));
        player.create();
        return player;
    }

    Entity createObject() {
        Entity object =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
                        .addComponent(new InteractionComponent());
        object.create();
        return object;
    }
}

interface InteractionListener extends EventListener2<Fixture, Fixture> {}
