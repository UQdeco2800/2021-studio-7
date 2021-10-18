package com.deco2800.game.entities.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class InteractionComponentTest {

    Entity object;
    Fixture objectFixture;
    InteractionComponent component;

    Entity player;
    Fixture playerFixture;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        component = spy(new InteractionComponent());
        player = createPlayer();
        object = createObject(component);
        playerFixture = player.getComponent(HitboxComponent.class).getFixture();
        objectFixture = object.getComponent(HitboxComponent.class).getFixture();
    }

    @Test
    void shouldReturnNullOnPreCollisionCheckFailure() {
        assertNull(component.preCollisionCheck(playerFixture, objectFixture));

        Fixture object2Fixture = createObject(new InteractionComponent())
                .getComponent(HitboxComponent.class).getFixture();
        assertNull(component.preCollisionCheck(objectFixture, object2Fixture));
    }

    @Test
    void shouldReturnEntityOnPreCollisionCheckSuccess() {
        assertEquals(component.preCollisionCheck(objectFixture, playerFixture), player);
    }

    @Test
    void shouldTriggerOnPreCollisionStart() {
        object.getEvents().trigger("pre_collision_start", objectFixture, playerFixture);
        verify(component).onPreCollisionStart(objectFixture, playerFixture);
    }

    @Test
    void shouldTriggerOnPreCollisionEnd() {
        object.getEvents().trigger("pre_collision_end", objectFixture, playerFixture);
        verify(component).onPreCollisionEnd(objectFixture, playerFixture);
    }

    @Test
    void shouldTriggerOnCollisionStart() {
        object.getEvents().trigger("collision_start", player);
        verify(component).onCollisionStart(player);
    }

    @Test
    void shouldTriggerOnCollisionEnd() {
        object.getEvents().trigger("collision_end", player);
        verify(component).onCollisionEnd(player);
    }

    @Test
    void shouldTriggerOnInteraction() {
        object.getEvents().trigger("interaction", player);
        verify(component).onInteraction(player);
    }

    @Test
    void shouldTriggerToggleHighlight() {
        object.getEvents().trigger("toggle_highlight", true);
        verify(component).toggleHighlight(true);
    }

    Entity createPlayer() {
        Entity player =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER));
        player.create();
        return player;
    }

    Entity createObject(InteractionComponent component) {
        Entity object =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
                        .addComponent(component);
        object.create();
        return object;
    }
}
