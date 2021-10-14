package com.deco2800.game.screens.endgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.GdxGame;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.ui.components.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class EndGameDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(EndGameDisplay.class);
  private static final float Z_INDEX = 2f;
  private Table table;
  private final EndGameScreen screen;
  private static List<TextButton> buttons = new ArrayList<TextButton>();

  public EndGameDisplay(EndGameScreen screen) {
    super();
    this.screen = screen;
  }

  @Override
  public void create() {
    super.create();
    addActors();
  }

  private void addActors() {
    table = new Table();

    // Set background to the appropriate texture for the end game condition.
    Image background =
            new Image(
                    ServiceLocator.getResourceService()
                            .getAsset(this.screen.getActiveScreenTextures()[0], Texture.class));
    table.setFillParent(true);
    table.setBackground(background.getDrawable());

    // Add button container to the table.
    // Easily sorts buttons vertically and separates padding settings from table.
    // It is assumed that more buttons will eventually be added.
    VerticalGroup buttonContainer = new VerticalGroup();
    buttonContainer.fill();
    buttonContainer.bottom().right();
    buttonContainer.space(10f);
    table.bottom().right();
    table.padBottom(10f).padRight(10f);
    table.add(buttonContainer);

    // Add button to container. Transitions to the next level (main game screen).
    if (this.screen.getResult() == GdxGame.ScreenType.WIN_DEFAULT) {
      TextButton nextLevelBtn = new TextButton("Next level", skin);
      buttons.add(nextLevelBtn);
      nextLevelBtn.addListener(
              new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                  logger.debug("Next level button clicked");
                  entity.getEvents().trigger("next_level");
                }
              });
      buttonContainer.addActor(nextLevelBtn);

    }

    // Add button to container. Transitions back to the main menu screen.
    TextButton mainMenuBtn = new TextButton("Back to main menu", skin);
    buttons.add(mainMenuBtn);
    mainMenuBtn.addListener(
            new ChangeListener() {
              @Override
              public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Exit button clicked");
                entity.getEvents().trigger("exit");
              }
            });
    buttonContainer.addActor(mainMenuBtn);

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

  public static void buttonLogic(String buttonChoice) {
    TextButton nextLvl = new TextButton("", skin);
    TextButton mainMenu = new TextButton("", skin);

    if (buttons.size() > 1) {
      nextLvl = buttons.get(0);
      mainMenu = buttons.get(1);
    } else {
      mainMenu = buttons.get(0);
    }

    if(buttonChoice == "Enter"){
      nextLvl.toggle();
    } else if (buttonChoice == "Escape") {
      mainMenu.toggle();
    }
  }
}
