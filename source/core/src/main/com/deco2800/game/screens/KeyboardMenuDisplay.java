package com.deco2800.game.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.game.ui.components.UIComponent;

/**
 * Settings menu display and logic. If you bork the settings, they can be changed manually in
 * DECO2800Game/settings.json under your home directory (This is C:/users/[username] on Windows).
 */
public abstract class KeyboardMenuDisplay extends UIComponent {
  protected float Z_INDEX = 2f;

  @Override
  public void create() {
    super.create();
    entity.getEvents().addListener("menu_key_pressed", this::onMenuKeyPressed);
    entity.getEvents().addListener("non_menu_key_pressed", this::onNonMenuKeyPressed);
  }

  protected void addActors() {
    // Create UI actors and place them into the stage here
  }

  @Override
  protected void draw(SpriteBatch batch) {
    // draw is handled by the stage
  }

  public void onMenuKeyPressed(int keyCode) {
    // Menu key presses by default do nothing
  }

  public void onNonMenuKeyPressed(int keyCode) {
    // Non menu key presses by default do nothing
  }

  @Override
  public float getZIndex() {
    return Z_INDEX;
  }
}
