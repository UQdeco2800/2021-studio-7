package com.deco2800.game.ai.tasks;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.deco2800.game.ai.components.AITaskComponent;
import com.deco2800.game.ai.tasks.WanderTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.events.listeners.EventListener0;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.utils.math.Vector2Utils;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.generic.GameTime;
import com.deco2800.game.generic.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class WanderTaskTest {
  @Mock
  GameTime gameTime;

  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerTimeSource(gameTime);
  }

  @Test
  void shouldTriggerEvent() {
    WanderTask wanderTask = new WanderTask(Vector2Utils.ONE, 1f);

    AITaskComponent aiTaskComponent = new AITaskComponent().addTask(wanderTask);
    Entity entity = new Entity().addComponent(aiTaskComponent).addComponent(new PhysicsMovementComponent());
    entity.create();

    // Register callbacks
    EventListener0 callback = mock(EventListener0.class);
    entity.getEvents().addListener("wander_start", callback);

    wanderTask.start();

    verify(callback).handle();
  }
}