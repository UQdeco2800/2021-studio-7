package com.deco2800.game.screens.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.ui.components.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileWriter;
import java.lang.Math;

import static com.badlogic.gdx.Gdx.graphics;


/**
 * A ui component for displaying the Main menu.
 */
public class MainMenuDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainMenuDisplay.class);
    private static final float Z_INDEX = 2f;
    private static TextField txtUsername;
    private Table tableMain;
    private Table tableLeft;
    private Table tableRight;
    private String playablecharcters[] = {
            "images/characters/boy_01/boy_01_menu_preview.png",
            "images/characters/girl_00/girl_00_menu_preview.png",
            "images/characters/boy_00/boy_00_menu_preview.png"

    };
    private  String playableAtlas[]={
            "images/characters/boy_01/boy_01.atlas",
            "images/characters/girl_00/girl_00.atlas",
            "images/characters/boy_00/boy_00.atlas"
    };
    private int characterIndex = 0;

    private static int menuIndex = 0;
    private static List<TextButton> buttons = new ArrayList<TextButton>();
    private static List<ImageButton> imageButtons = new ArrayList<ImageButton>();
    private static Image menuIndicator;

    private Texture leftTexture;
    private TextureRegion leftTextureRegion;
    private TextureRegionDrawable leftTextureRegionDrawable;
    private Texture rightTexture;
    private TextureRegion rightTextureRegion;
    private TextureRegionDrawable rightTextureRegionDrawable;
    private Image leftBtnGrey;
    private Image rightBtnGrey;

    @Override
    public void create() {
        super.create();
        addActors();
        updateMenuFrame();
    }

    private void addActors() {

        tableLeft = new Table();
        tableRight = new Table();
        tableMain = new Table();

        menuIndex = 0;

        Image background = new Image(ServiceLocator.getResourceService()
                .getAsset("images/main_menu/bgart.png", Texture.class));

        tableMain.setFillParent(true);
        tableMain.setBackground(background.getDrawable());

        Image title =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/ui/title/RETROACTIVE-large.png", Texture.class));

        title.setAlign(Align.center);

        writeAtlas(); //Stores copy of the first character

        TextButton startBtn = new TextButton("Start", skin);
        startBtn.setTransform(true);
        buttons.add(startBtn);
        TextButton leaderboardBtn = new TextButton("LeaderBoard", skin);
        leaderboardBtn.setTransform(true);
        leaderboardBtn.setSize(2f, 2f);
        buttons.add(leaderboardBtn);
        TextButton settingsBtn = new TextButton("Settings", skin);
        buttons.add(settingsBtn);
        TextButton exitBtn = new TextButton("Exit", skin);
        buttons.add(exitBtn);

        leftTexture = new Texture("images/main_menu/pointer-L.png");
        leftTextureRegion = new TextureRegion(leftTexture);
        leftTextureRegionDrawable = new TextureRegionDrawable(leftTextureRegion);
        ImageButton leftBtn = new ImageButton(leftTextureRegionDrawable);

        rightTexture = new Texture("images/main_menu/pointer-R.png");
        rightTextureRegion = new TextureRegion(rightTexture);
        rightTextureRegionDrawable = new TextureRegionDrawable(rightTextureRegion);
        ImageButton rightBtn = new ImageButton(rightTextureRegionDrawable);

        imageButtons.add(leftBtn);
        imageButtons.add(rightBtn);


        /** USERNAME
        this.txtUsername = new TextField("", skin);
        txtUsername.setMessageText("Username:");
         */

        Image character = new Image(ServiceLocator.getResourceService()
                .getAsset(playablecharcters[characterIndex], Texture.class));

        menuIndicator = new Image(ServiceLocator.getResourceService()
                .getAsset("images/ui/elements/menuFrame-LONG.png", Texture.class));

        rightBtnGrey = new Image(ServiceLocator.getResourceService()
                .getAsset("images/main_menu/pointer-R-inactive.png", Texture.class));

        leftBtnGrey = new Image(ServiceLocator.getResourceService()
                .getAsset("images/main_menu/pointer-L-inactive.png", Texture.class));


        // Triggers an event when the button is pressed
        startBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Start button clicked");
                        writeUsername();
                        entity.getEvents().trigger("start");
                    }
                });

        leaderboardBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("LeaderBoard button clicked");
                        entity.getEvents().trigger("leaderboard");
                    }
                });

        settingsBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Settings button clicked");
                        entity.getEvents().trigger("settings");
                    }
                });

        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {

                        logger.debug("Exit button clicked");
                        entity.getEvents().trigger("exit");
                    }
                });

        leftBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        changeCharacterLeft();
                        writeAtlas();
                        logger.info("Change Character button clicked. ");
                        entity.getEvents().trigger("change_character");
                    }
                });

        rightBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        changeCharacterRight();
                        writeAtlas();
                        logger.info("Change Character button clicked. ");
                        entity.getEvents().trigger("change_character");
                    }
                });


        tableLeft.add(startBtn).align(Align.center);
        tableLeft.row();
        tableLeft.add(leaderboardBtn).padTop(50f);
        tableLeft.row();
        tableLeft.add(settingsBtn).padTop(50f);
        tableLeft.row();
        tableLeft.add(exitBtn).padTop(50f);

        tableRight.add().colspan(3);
        tableRight.row();
        tableRight.add(leftBtn).padRight(5f);
        tableRight.add(character);
        tableRight.add(rightBtn).padLeft(5f);

        tableMain.add(title).colspan(2).padTop(50f);
        tableMain.row();
        tableMain.add(tableLeft).expandY().fillY().fillX();
        tableMain.add(tableRight).expandY().fillY().fill();
        stage.addActor(tableMain);

        menuIndicator.setTouchable(Touchable.disabled);
        stage.addActor(menuIndicator);
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
        tableMain.clear();
        super.dispose();
    }

    /**
     * Used to toggle the left arrow button for character selection
     */
    public static void toggleLeftBtn() {
        imageButtons.get(0).toggle();
    }

    public static void toggleRightBtn() {
        imageButtons.get(1).toggle();
    }

    /**
     * Ensures that the characterIndex cannot go below 0 to avoid OutOfIndex Error
     */
    public boolean notFarLeft() {
        return characterIndex > 0;
    }

    /**
     * Changes the character to the previous skin
     * Also plays the 'b e e p'
     */
    public void changeCharacterLeft(){
        if (notFarLeft()){
            MainMenuScreen.playButtonSound("browse");
            characterIndex--;
            buttons.clear();
            imageButtons.clear();
            dispose();
            create();
        } else {

        }
    }

    /**
     * Ensures that the characterIndex cannot go above 2 to avoid OutOfIndex Error
     */
    public boolean notFarRight() {
        return characterIndex < 2;
    }

    /**
     * Changes the character to the next skin
     * Also plays the 'b e e p'
     */
    public void changeCharacterRight(){
        if (notFarRight()){
            MainMenuScreen.playButtonSound("browse");
            characterIndex++;
            buttons.clear();
            imageButtons.clear();
            dispose();
            create();
        } else {

        }
    }

    /**
     * Updates currentCharacterAtlas.txt
     */
//    public void writeAtlas(){
//        try {
//            FileWriter writer = new FileWriter("configs/currentCharacterAtlas.txt");
//            writer.write(this.playableAtlas[this.characterIndex]);
//            writer.close();
//            logger.info("Writing new atlas to settings.");
//        } catch (Exception e){
//
//            logger.debug("Could not load the atlas after character change was made.");
//        }
//    }

    public void writeAtlas(){
        FileWriter writer = null;
        try {
            writer = new FileWriter("configs/currentCharacterAtlas.txt");
            writer.write(this.playableAtlas[this.characterIndex]);
            writer.close();
            logger.info("Writing new atlas to settings.");
        } catch (Exception e){

            logger.debug("Could not load the atlas after character change was made.");
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    logger.debug("Failed to close writer.");
                }
            }
        }
    }

    public void writeUsername(){
        FileWriter writer = null;
        try {
            writer = new FileWriter("configs/leaderboard.txt",true);
            String username;

            if (this.txtUsername.getText().length()<2){
                username = "DirtyDefault"+ getRandomNum();
            }else{
                username = this.txtUsername.getText();
            }

            StringBuilder sb = new StringBuilder();
            sb.append("\n");
            sb.append(username);
            sb.append(":");
            String s = sb.toString();
            writer.write(s);
            writer.close();
            logger.info("Wrote username to leaderboard.");
        } catch (Exception e){
            logger.debug("Could not write username to leaderboard.");
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                }catch (IOException e) {
                    logger.debug("Failed to close writer.");
                }
            }
        }
    }

    public int getRandomNum(){
        return (int)(Math.random()*100000);
    }

    /**
     * Moves the menuFrame up
     */
    public static void moveUp(){
        if (notAtTop()) {
            menuIndex--;
            updateMenuFrame();
        }
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
     * Checks to ensure the menuIndex doesn't go below 0
     */
    private static boolean notAtTop() {
        return menuIndex > 0;
    }

    /**
     * Moves the menuFrame down
     */
    public static void moveDown(){
        if (notAtBottom()) {
            menuIndex++;
            updateMenuFrame();
        }
        logger.info("Menu Index is " + Integer.toString(menuIndex));
    }

    /**
     * Checks to ensure the menuIndex cannot exceed 3
     */
    private static boolean notAtBottom() {
        return menuIndex < 3;
    }

    /**
     * Changes the position of the menuFrame corresponding to the menuIndex
     * Also changes the required buttons to hover/unhover
     * Also plays the 'b e e p'
     */
    public static void updateMenuFrame() {
        MainMenuScreen.playButtonSound("browse");

        TextButton startBtn = buttons.get(0);
        TextButton LeadBtn = buttons.get(1);
        TextButton SetBtn = buttons.get(2);
        TextButton ExitBtn = buttons.get(3);
        float buttonXLocation = (float) graphics.getWidth() / 2 - 430;
        switch (menuIndex) {
            case 0: //Start Button (height of title image + 15f)
                menuIndicator.setPosition(buttonXLocation, (float) graphics.getHeight() / 2);
                hoverMenu(startBtn);
                unhoverMenu(LeadBtn);
                unhoverMenu(SetBtn);
                unhoverMenu(ExitBtn);
                break;
            case 1: //Leaderboard Button (height start btn + 15f)
                menuIndicator.setPosition(buttonXLocation, LeadBtn.getY()-10);
                unhoverMenu(startBtn);
                hoverMenu(LeadBtn);
                unhoverMenu(SetBtn);
                unhoverMenu(ExitBtn);
                break;
            case 2: //Settings Button
                menuIndicator.setPosition(buttonXLocation,SetBtn.getY()-10);
                unhoverMenu(startBtn);
                unhoverMenu(LeadBtn);
                hoverMenu(SetBtn);
                unhoverMenu(ExitBtn);
                break;
            case 3: //Exit Button
                menuIndicator.setPosition(buttonXLocation,ExitBtn.getY()-10);
                unhoverMenu(startBtn);
                unhoverMenu(LeadBtn);
                unhoverMenu(SetBtn);
                hoverMenu(ExitBtn);
                break;
        }
    }

    /**
     * Function used to toggle each respective button and trigger their respective listeners
     */
    public static void pressMenu() {
        logger.info("Enter key is pressed");
        switch (menuIndex) {
            case 0: //Start Button
                TextButton startBtn = buttons.get(0);
                buttons.clear();
                imageButtons.clear();
                startBtn.toggle();
                break;
            case 1: //Leaderboard Button
                TextButton LeadBtn = buttons.get(1);
                buttons.clear();
                imageButtons.clear();
                LeadBtn.toggle();
                break;
            case 2: //Settings Button
                TextButton SetBtn = buttons.get(2);
                buttons.clear();
                imageButtons.clear();
                SetBtn.toggle();
                break;
            case 3: //Exit Button
                TextButton ExitBtn = buttons.get(3);
                ExitBtn.toggle();
                break;
        }
    }
}



