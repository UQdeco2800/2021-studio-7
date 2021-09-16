package com.deco2800.game.screens.maingame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.ui.components.UIComponent;
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

      simWinDefBtn.getLabel().setColor(0, 0,0, 1f);
      simWinDefBtn.addListener(
              new ChangeListener() {
        @Override
        public void changed(ChangeEvent changeEvent, Actor actor) {
            logger.debug("Simulate default win button clicked");
            entity.getEvents().trigger("win_default");
        }
      });
      buttonContainer.addActor(simWinDefBtn);

      // Triggers an event when the button is pressed.
      TextButton simLoseTimedBtn = new TextButton("Simulate timed loss", skin);

      simLoseTimedBtn.getLabel().setColor(0, 0,0, 1f);
      simLoseTimedBtn.addListener(
              new ChangeListener() {
              @Override
              public void changed(ChangeEvent changeEvent, Actor actor) {
                  logger.debug("Simulate timed loss button clicked");
                  entity.getEvents().trigger("loss_timed");
              }
            });
      buttonContainer.addActor(simLoseTimedBtn);

      // Triggers an event when the button is pressed.
      TextButton simLoseCaughtBtn = new TextButton("Simulate caught loss", skin);

      simLoseCaughtBtn.getLabel().setColor(0, 0,0, 1f);
      simLoseCaughtBtn.addListener(
              new ChangeListener() {
              @Override
              public void changed(ChangeEvent changeEvent, Actor actor) {
                  logger.debug("Simulate caught loss button clicked");
                  entity.getEvents().trigger("loss_caught");
              }
            });
      buttonContainer.addActor(simLoseCaughtBtn);

      // Button for testing the textboxUI
      TextButton textboxBtn = new TextButton("Create textbox", skin);
      String testString = "This is a lengthy test string to slap on the screen and it just keeps" +
              " going on and on and on and on and on";
      textboxBtn.addListener(
              new ChangeListener() {
                  @Override
                  public void changed(ChangeEvent changeEvent, Actor actor) {
                      logger.debug("Toggle textbox button clicked");
                      entity.getEvents().trigger("create_textbox", testString);
                  }
              });
      buttonContainer.addActor(textboxBtn);

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
