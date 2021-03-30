package com.deco2800.game.input;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
public class InputFactoryTest {

  @Test
  void shouldGiveKeyboardFactoryType() {
    InputFactory keyboardInputFactory = InputFactory.createFromInputType(InputFactory.InputType.KEYBOARD);
    assertTrue(keyboardInputFactory instanceof KeyboardInputFactory);
  }

  @Test
  void shouldGiveNoFactory() {
    InputFactory invalidInputFactory = InputFactory.createFromInputType(null);
    assertNull(invalidInputFactory);
  }
}