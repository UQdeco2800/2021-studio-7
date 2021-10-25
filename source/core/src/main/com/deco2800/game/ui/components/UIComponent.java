package com.deco2800.game.ui.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.deco2800.game.rendering.components.RenderComponent;
import com.deco2800.game.rendering.Renderable;
import com.deco2800.game.generic.ServiceLocator;

/**
 * A generic component for rendering onto the ui.
 */
public abstract class UIComponent extends RenderComponent implements Renderable {
  private static final int UI_LAYER = 2;
  private final float Z_INDEX;
  protected static final Skin skin =
      new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
  protected Stage stage;

  public UIComponent() {
    this(1f);
  }

  public UIComponent(float zIndex) {
    this.Z_INDEX = zIndex;
  }

  @Override
  public void create() {
    super.create();
    stage = ServiceLocator.getRenderService().getStage();
  }

  @Override
  public int getLayer() {
    return UI_LAYER;
  }

  @Override
  public float getZIndex() {
    return Z_INDEX;
  }
}
