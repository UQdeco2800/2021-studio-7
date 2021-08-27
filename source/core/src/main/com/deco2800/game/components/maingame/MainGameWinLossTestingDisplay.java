package com.deco2800.game.components.maingame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class MainGameWinLossTestingDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(MainGameWinLossTestingDisplay.class);
  private static final float Z_INDEX = 2f;
  private Table table;

  @Override
  public void create() {
    super.create();
    addActors();
  }

  private void addActors() {
    table = new Table();
    table.top().right();
    table.setFillParent(true);

    TextButton simWinDefBtn = new TextButton("Simulate default win", skin);
    TextButton simLoseTimedBtn = new TextButton("Simulate timed loss", skin);
    TextButton simLoseCaughtBtn = new TextButton("Simulate caught loss", skin);

    // Triggers an event when the button is pressed.
    simWinDefBtn.addListener(
      new ChangeListener() {
        @Override
        public void changed(ChangeEvent changeEvent, Actor actor) {
          logger.debug("Simulate default win button clicked");
          entity.getEvents().trigger("winDefault");
        }
      });

    // Triggers an event when the button is pressed.
    simLoseTimedBtn.addListener(
            new ChangeListener() {
              @Override
              public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Simulate timed loss button clicked");
                entity.getEvents().trigger("lossTimed");
              }
            });

    // Triggers an event when the button is pressed.
    simLoseCaughtBtn.addListener(
            new ChangeListener() {
              @Override
              public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Simulate caught loss button clicked");
                entity.getEvents().trigger("lossCaught");
              }
            });

    table.add(simWinDefBtn).padTop(500f).padRight(10f);
    table.add(simLoseTimedBtn).padTop(500f).padRight(0f);
    table.add(simLoseCaughtBtn).padTop(500f).padRight(0f);

    stage.addActor(table);
  }

  @Override
  public void draw(SpriteBatch batch) {
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
