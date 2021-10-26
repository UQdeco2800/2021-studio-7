package com.deco2800.game.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.ui.components.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class RetroactiveDisplay extends UIComponent {
  protected static final Logger logger = LoggerFactory.getLogger(RetroactiveDisplay.class);
  protected float Z_INDEX = 2f;
  protected Table table = new Table();
  protected List<String> textures = new ArrayList<>();
  protected List<String> atlases = new ArrayList<>();

  public RetroactiveDisplay() {
    creationPriority = 100;
  }

  @Override
  public void create() {
    super.create();
    entity.getEvents().addListener("key_down", this::onPreKeyDown);
    addActors();
    table.setVisible(false);
    stage.addActor(table);
  }

  protected abstract void addActors();

  protected abstract void keyDown(int keycode);

  private void onPreKeyDown(int keyCode) {
    if (table.isVisible()) {
      keyDown(keyCode);
    }
  }

  protected int changeSelectedButton(Group container, int index, int direction) {
    if ((direction < 0 && index == 0) ||
        (direction > 0 && index == container.getChildren().size - 1)) {
      direction = 0;
    }

    if (direction != 0) {
      container.getChild(index).fire(getUnhighlightEvent());
      index = (index + direction) % container.getChildren().size;
      container.getChild(index).fire(getHighlightEvent());
    }

    return index;
  }

  protected InputEvent getHighlightEvent() {
    InputEvent highlight = new InputEvent();
    highlight.setType(InputEvent.Type.exit);
    highlight.setPointer(-1);
    return highlight;
  }

  protected InputEvent getUnhighlightEvent() {
    InputEvent unhighlight = new InputEvent();
    unhighlight.setType(InputEvent.Type.enter);
    unhighlight.setPointer(-1);
    return unhighlight;
  }

  public void loadAssets() {
    ResourceService service = ServiceLocator.getResourceService();
    for (String asset : textures) {
      if (!service.containsAsset(asset, Texture.class)) {
        service.loadTexture(asset);
      }
    }
    for (String asset : atlases) {
      if (!service.containsAsset(asset, TextureAtlas.class)) {
        service.loadTextureAtlas(asset);
      }
    }
  }

  public void unloadAssets() {
    ResourceService service = ServiceLocator.getResourceService();
    for (String asset : textures) {
      if (service.containsAsset(asset, Texture.class)) {
        service.unloadAsset(asset);
      }
    }
    for (String asset : atlases) {
      if (service.containsAsset(asset, TextureAtlas.class)) {
        service.unloadAsset(asset);
      }
    }
  }

  public void hide() {
    table.setVisible(false);
  }

  public void show() {
    table.setVisible(true);
  }

  @Override
  protected void draw(SpriteBatch batch) {
    // draw is handled by the stage
  }

  @Override
  public float getZIndex() {
    return Z_INDEX;
  }

  @Override
  public void dispose() {
    table.clear();
    super.dispose();
  }
}