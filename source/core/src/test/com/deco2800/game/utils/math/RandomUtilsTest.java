package com.deco2800.game.utils.math;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(GameExtension.class)
class RandomUtilsTest {

    @Test
    void shouldReturnSeed() {
        assertNotNull(RandomUtils.getSeed());
    }
}
