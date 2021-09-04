package com.deco2800.game.physics.components;

import static org.mockito.Mockito.*;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
class PhysicsMovementComponentTest {

    @Test
    void getCurrentDirectionCode() {
        PhysicsMovementComponent test1 = mock(PhysicsMovementComponent.class);
        doNothing().when(test1).getcurrentDirectionCode();
        test1.getcurrentDirectionCode();
        verify(test1, times(1)).getcurrentDirectionCode();
    }

    @Test
    void movementEvent() {
        PhysicsMovementComponent test2 = mock(PhysicsMovementComponent.class);
        doNothing().when(test2).movementEvents();
        test2.movementEvents();
        verify(test2, times(1)).movementEvents();
    }

}
