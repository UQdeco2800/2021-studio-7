package com.deco2800.game.screens.maingame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.ui.components.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class MainGamePauseMenuDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainGamePauseMenuDisplay.class);
    private static final String PAUSE_BACKGROUND = "images/ui/screens/paused_screen.png";
    private Table table;
    private static final float Z_INDEX = 2f;
    private boolean isVisible = false;
    private static int menuIndex = 0;
    private static List<TextButton> buttons = new ArrayList<>();

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("toggle_pause_visibility", this::toggleVisibility);
        entity.getEvents().addListener("pressed_enter_in_pause", this::pressButton);
        addActors();
        hoverMenu(buttons.get(menuIndex));
    }

    public void toggleVisibility() {
        isVisible = !isVisible;
        table.setVisible(isVisible);
        if (isVisible) {
            entity.getEvents().trigger("pause");
        } else {
            entity.getEvents().trigger("resume");
        }
    }

    private void addActors() {
        table = new Table(skin);
        table.setFillParent(true);
        table.bottom().padBottom(20f);
        table.setVisible(isVisible);

        // Set background to the appropriate texture for the end game condition.
        ServiceLocator.getResourceService().loadTexture(PAUSE_BACKGROUND);
        ServiceLocator.getResourceService().loadAll();
        Image background =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset(PAUSE_BACKGROUND, Texture.class));
        table.setBackground(background.getDrawable());

        // Add button container to the table.
        // Easily sorts buttons vertically and separates padding settings from table.
        // It is assumed that more buttons will eventually be added.
        HorizontalGroup buttonContainer = new HorizontalGroup();
        buttonContainer.space(10f);
        table.add(buttonContainer);

        // Add button to container. Resumes the game.
        TextButton resumeBtn = new TextButton("Resume", skin);
        buttons.add(resumeBtn);
        resumeBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    logger.debug("Resume button clicked");
                    toggleVisibility();
                }
            });
        buttonContainer.addActor(resumeBtn);

        // Add button to container. Restarts the game.
        TextButton restartBtn = new TextButton("Restart", skin);
        buttons.add(restartBtn);
        restartBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        logger.debug("Restart button clicked");
                        entity.getEvents().trigger("restart");
                    }
                });
        buttonContainer.addActor(restartBtn);

        // Add button to container. Goes to the settings display.
        TextButton settingsBtn = new TextButton("Settings", skin);
        buttons.add(settingsBtn);
        settingsBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        logger.debug("Settings button clicked");
                        entity.getEvents().trigger("settings");
                    }
                });
        buttonContainer.addActor(settingsBtn);

        // Add button to container. Goes back to the main menu.
        TextButton mainMenuBtn = new TextButton("Main Menu", skin);
        buttons.add(mainMenuBtn);
        mainMenuBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        logger.debug("Main menu button clicked");
                        entity.getEvents().trigger("main_menu");
                    }
                });
        buttonContainer.addActor(mainMenuBtn);

        table.setTouchable(Touchable.disabled);
        stage.addActor(table);
    }

    /**
     * Ensures that the menuIndex cannot go above 3 to avoid OutOfIndex Error
     */
    public static boolean notFarRight() {
        return menuIndex < 3;
    }

    /**
     * Ensures that the menuIndex cannot go below 0 to avoid OutOfIndex Error
     */
    public static boolean notFarLeft() {
        return menuIndex > 0;
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
     * Moves the button highlight right
     */
    public static void moveRight(){
        if (notFarRight()) {
            MainGameScreen.playButtonSound("browse");
            menuIndex++;
            unhoverMenu(buttons.get(menuIndex - 1));
            hoverMenu(buttons.get(menuIndex));
        }
        logger.info("Menu Index is {}", menuIndex);
    }
    /**
     * Moves the button highlight left
     */
    public static void moveLeft(){
        if (notFarLeft()) {
            MainGameScreen.playButtonSound("browse");
            menuIndex--;
            unhoverMenu(buttons.get(menuIndex + 1));
            hoverMenu(buttons.get(menuIndex));
        }
        logger.info("Menu Index is {}", menuIndex);
    }

    /**
     * Reset the hover position to resume button when pause menu is closed
     */
    public static void resetHover() {
        unhoverMenu(buttons.get(menuIndex));
        menuIndex = 0;
        hoverMenu(buttons.get(menuIndex));
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

    /**
     * Function used to toggle each respective button and trigger their respective listeners
     */
    public void pressButton() {
        logger.info("Enter key is pressed in paused menu");
        buttons.get(menuIndex).toggle();
        switch (menuIndex) {
            case 0: //Resume Button
                logger.info("Resumed from pause menu");
                break;
            case 1: //Restart Button
                logger.info("Restarted game from pause menu");
                break;
            case 2: //Settings Button
                logger.info("Entered settings screen from pause menu");
                break;
            case 3: //Main Menu Button
                logger.info("Entered main menu from pause menu");
                break;
            default:
                logger.info("Default case menu index {}", menuIndex);
                break;
        }
    }
}
