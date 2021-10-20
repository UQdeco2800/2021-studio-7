package com.deco2800.game.screens.endgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
  private static List<TextButton> buttons = new ArrayList<>();
  private static int menuIndex = 0;

  public EndGameDisplay(EndGameScreen screen) {
    super();
    this.screen = screen;
  }

  @Override
  public void create() {
    super.create();
    addActors();
    hoverMenu(buttons.get(menuIndex));
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
    buttons.clear();
    super.dispose();
  }

  public static void buttonLogic(String buttonChoice) {
    TextButton mainMenu;

    if (buttons.size() > 1) {
      mainMenu = buttons.get(1);
    } else {
      mainMenu = buttons.get(0);
    }

    if (buttonChoice.equals("Enter")) {
      buttons.get(menuIndex).toggle();
    } else if (buttonChoice.equals("Escape")) {
      mainMenu.toggle();
    }
    buttons.clear();
  }

  /**
   Emulates mouse hover with keyboard
   **/
  public static void hoverMenu(Actor btn){
    InputEvent event = new InputEvent();
    event.setType(InputEvent.Type.enter);
    event.setPointer(-1);
    btn.fire(event);
  }

  /**
   Emulates mouse unhover with keyboard
   **/
  public static void unhoverMenu(Actor btn){
    InputEvent event = new InputEvent();
    event.setType(InputEvent.Type.exit);
    event.setPointer(-1);
    btn.fire(event);
  }

  /**
   * Reset the hover position to resume button when pause menu is closed
   */
  public static void resetHover() {
    menuIndex = 0;
  }

  /**
   * Ensures that the menuIndex cannot go beyond number of buttons to avoid OutOfIndex Error
   */
  public static boolean notFarDown() {
    return menuIndex < buttons.size() - 1;
  }

  /**
   * Ensures that the menuIndex cannot go below 0 to avoid OutOfIndex Error
   */
  public static boolean notFarUp() {
    return menuIndex > 0;
  }

  /**
   * Moves the button highlight down
   */
  public static void moveDown(){
    if (notFarDown()) {
      EndGameScreen.playButtonSound("browse");
      menuIndex++;
      unhoverMenu(buttons.get(menuIndex - 1));
      hoverMenu(buttons.get(menuIndex));
    }
    logger.info("Menu index is {}", menuIndex);
  }
  /**
   * Moves the button highlight up
   */
  public static void moveUp(){
    if (notFarUp()) {
      EndGameScreen.playButtonSound("browse");
      menuIndex--;
      unhoverMenu(buttons.get(menuIndex + 1));
      hoverMenu(buttons.get(menuIndex));
    }
    logger.info("Menu index is {}", menuIndex);
  }
}
