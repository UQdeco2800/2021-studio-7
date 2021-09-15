package com.deco2800.game.entities.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.interactions.Actions.BedActions;
import com.deco2800.game.entities.components.interactions.InteractionComponent;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.events.EventHandler;
import com.deco2800.game.events.listeners.EventListener0;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


@ExtendWith(GameExtension.class)
class InteractionComponentTest {

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
    }

    /*
    @Test
    void shouldTriggerCollisionEvent() {
        Entity player = createPlayer();
        Entity object = createObject();

        EventListener0 listener = mock(EventListener0.class);
        object.getEvents().addListener("interaction_start", listener);

        Fixture playerFixture = player.getComponent(HitboxComponent.class).getFixture();
        Fixture objectFixture = object.getComponent(HitboxComponent.class).getFixture();
        object.getEvents().trigger("collision_start", objectFixture, playerFixture);

        verify(listener).handle(); // Check that listener ran when the object was collided with
    }

    @Test
    void shouldEndCollisionEvent() {
        Entity player = createPlayer();
        Entity object = createObject();

        EventListener0 listener = mock(EventListener0.class);
        object.getEvents().addListener("interaction_end", listener);

        Fixture playerFixture = player.getComponent(HitboxComponent.class).getFixture();
        Fixture objectFixture = object.getComponent(HitboxComponent.class).getFixture();
        object.getEvents().trigger("collision_end", objectFixture, playerFixture);

        verify(listener).handle(); // Check that listener ran when the object was not collided
    }
    */

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
                        .addComponent(new InteractionComponent())
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE));
        object.create();
        return object;
    }
}
