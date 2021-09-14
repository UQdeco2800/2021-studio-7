package com.deco2800.game.input;

import com.deco2800.game.entities.components.player.TouchPlayerInputComponent;
import com.deco2800.game.input.components.InputComponent;
import com.deco2800.game.screens.titlescreen.TouchTitleInputComponent;
import com.deco2800.game.ui.terminal.TouchTerminalInputComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TouchInputFactory extends InputFactory{
  private static final Logger logger = LoggerFactory.getLogger(TouchInputFactory.class);

  /**
   * Creates an input handler for the player
   *
   * @return Player input handler
   */
  @Override
  public InputComponent createForPlayer() {
    logger.debug("Creating player input handler");
    return new TouchPlayerInputComponent();
  }

  /**
   * Creates an input handler for the terminal
   *
   * @return Terminal input handler
   */
  @Override
  public InputComponent createForTerminal() {
    logger.debug("Creating terminal input handler");
    return new TouchTerminalInputComponent();
  }

  @Override
  public InputComponent createForTitle() {
    logger.debug("Creating title input handler");
    return new TouchTitleInputComponent();
  }
}
