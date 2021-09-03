package com.deco2800.game.components.maingame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
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
      table.bottom().right().padBottom(10f).padRight(10f);
      table.setFillParent(true);

      // Add button container to the table.
      // Easily sorts buttons vertically and separates padding settings from table.
      // It is assumed that more buttons will eventually be added.
      VerticalGroup buttonContainer = new VerticalGroup();
      buttonContainer.fill().bottom().right().space(10f);
      table.add(buttonContainer);

    // Triggers an event when the button is pressed.
      TextButton simWinDefBtn = new TextButton("Simulate default win", skin);
      simWinDefBtn.addListener(
              new ChangeListener() {
        @Override
        public void changed(ChangeEvent changeEvent, Actor actor) {
            logger.debug("Simulate default win button clicked");
            entity.getEvents().trigger("winDefault");
        }
      });
      buttonContainer.addActor(simWinDefBtn);

      // Triggers an event when the button is pressed.
      TextButton simLoseTimedBtn = new TextButton("Simulate timed loss", skin);
      simLoseTimedBtn.addListener(
              new ChangeListener() {
              @Override
              public void changed(ChangeEvent changeEvent, Actor actor) {
                  logger.debug("Simulate timed loss button clicked");
                  entity.getEvents().trigger("lossTimed");
              }
            });
      buttonContainer.addActor(simLoseTimedBtn);

      // Triggers an event when the button is pressed.
      TextButton simLoseCaughtBtn = new TextButton("Simulate caught loss", skin);
      simLoseCaughtBtn.addListener(
              new ChangeListener() {
              @Override
              public void changed(ChangeEvent changeEvent, Actor actor) {
                  logger.debug("Simulate caught loss button clicked");
                  entity.getEvents().trigger("lossCaught");
              }
            });
      buttonContainer.addActor(simLoseCaughtBtn);

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
