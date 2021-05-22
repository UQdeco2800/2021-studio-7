package com.deco2800.game.terminal;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.deco2800.game.ui.UIComponent;

/** A ui component for displaying the debug terminal. */
public class TerminalDisplay extends UIComponent {
  private static final float zIndex = 10f;
  private Terminal terminal;
  private Label label;

  @Override
  public void create() {
    super.create();
    addActors();
    terminal = entity.getComponent(Terminal.class);
  }

  private void addActors() {
    // label at bottom of screen with background
    String message = "";
    label = new Label("> " + message, skin);
    label.setPosition(5f, 0);
    stage.addActor(label);
  }

  @Override
  public void draw(SpriteBatch batch) {
    if (terminal.isOpen()) {
      label.setVisible(true);
      String message = terminal.getEnteredMessage();
      label.setText("> " + message);
    } else {
      label.setVisible(false);
    }
  }

  @Override
  public float getZIndex() {
    return zIndex;
  }

  @Override
  public void dispose() {
    super.dispose();
    label.remove();
  }
}
