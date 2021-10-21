package com.deco2800.game.entities.components.player;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.components.InteractionComponent;
import com.deco2800.game.events.listeners.EventListener1;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsLayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class InteractionControllerComponentTest {

    Entity entity;
    Entity target;
    InteractionControllerComponent component;

    @BeforeEach
    void beforeEach() {
        entity = new Entity();
        target = new Entity();
        component = new InteractionControllerComponent();
        entity.addComponent(component).create();
        target.addComponent(new InteractionComponent()).create();
    }

    @Test
    void shouldCreate() {
        assertEquals(PhysicsLayer.OBSTACLE, component.getTargetLayer());
        assertTrue(entity.getEvents().hasListener("toggle_interacting"));
    }

    @Test
    void shouldAddInteractableOnCollisionStart() {
        component.onCollisionStart(target);
        assertEquals(1, component.getInteractables().size());
        component.onCollisionStart(target);
        assertEquals(1, component.getInteractables().size());
    }

    @Test
    void shouldRemoveInteractableOnCollisionEnd() {
        component.onCollisionStart(target);
        component.onCollisionEnd(target);
        assertEquals(0, component.getInteractables().size());
        component.onCollisionEnd(target);
        assertEquals(0, component.getInteractables().size());
    }

    @Test
    void shouldToggleInteracting() {
        entity.getEvents().trigger("toggle_interacting", true);
        assertTrue(component.isInteracting());
        entity.getEvents().trigger("toggle_interacting", false);
        assertFalse(component.isInteracting());
    }

    @Test
    void shouldUpdateInteractableHighlights() {
        entity.setPosition(0, 0);
        target.setPosition(1, 1);
        component.onCollisionStart(target);
        component.updateInteractableHighlights();
        assertEquals(component.getHighlightedInteractable(), target);

        Entity target2 = new Entity().addComponent(new InteractionComponent());
        target2.create();
        target2.setPosition(1, 0);
        component.onCollisionStart(target2);
        component.updateInteractableHighlights();
        assertEquals(component.getHighlightedInteractable(), target2);

        component.onCollisionEnd(target2);
        component.updateInteractableHighlights();
        assertEquals(component.getHighlightedInteractable(), target);

        component.onCollisionEnd(target);
        component.updateInteractableHighlights();
        assertNull(component.getHighlightedInteractable());
    }

    @Test
    void shouldTriggerInteraction() {
        EventListener1<Entity> listener = mock(InteractionListener.class);
        target.getEvents().addListener("interaction", listener);

        entity.setPosition(0, 0);
        target.setPosition(0, 1);
        component.onCollisionStart(target);
        component.updateInteractableHighlights();
        assertEquals(component.getHighlightedInteractable(), target);
        component.triggerInteraction();

        verify(listener).handle(entity);
    }

    interface InteractionListener extends EventListener1<Entity> {}
}
