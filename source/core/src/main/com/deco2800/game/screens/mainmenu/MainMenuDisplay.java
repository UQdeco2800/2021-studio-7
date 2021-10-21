package com.deco2800.game.screens.mainmenu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.ui.components.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.Gdx.graphics;


/**
 * A ui component for displaying the Main menu.
 */
public class MainMenuDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainMenuDisplay.class);
    private static final float Z_INDEX = 2f;
    private static final String BROWSE = "browse";
    private Table tableMain;
    private String[] playablecharcters = {
            "images/characters/boy_01/boy_01_menu_preview.png",
            "images/characters/girl_00/girl_00_menu_preview.png",
            "images/characters/boy_00/boy_00_menu_preview.png"

    };
    private String[] playableAtlas ={
            "images/characters/boy_01/boy_01.atlas",
            "images/characters/girl_00/girl_00.atlas",
            "images/characters/boy_00/boy_00.atlas"
    };
    private int characterIndex = 0;

    private static int menuIndex = 0;
    private static List<TextButton> buttons = new ArrayList<>();
    private static List<ImageButton> imageButtons = new ArrayList<>();
    private static Image menuIndicator;


    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        //resetMenuIndex();

        Table tableLeft = new Table();
        Table tableRight = new Table();
        tableMain = new Table();

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

        Texture leftTexture = new Texture("images/main_menu/pointer-L.png");
        TextureRegion leftTextureRegion = new TextureRegion(leftTexture);
        TextureRegionDrawable leftTextureRegionDrawable = new TextureRegionDrawable(leftTextureRegion);
        ImageButton leftBtn = new ImageButton(leftTextureRegionDrawable);

        Texture rightTexture = new Texture("images/main_menu/pointer-R.png");
        TextureRegion rightTextureRegion = new TextureRegion(rightTexture);
        TextureRegionDrawable rightTextureRegionDrawable = new TextureRegionDrawable(rightTextureRegion);
        ImageButton rightBtn = new ImageButton(rightTextureRegionDrawable);

        imageButtons.add(leftBtn);
        imageButtons.add(rightBtn);

        this.menuIndicator = createMenuIndicator();

        Image character = new Image(ServiceLocator.getResourceService()
                .getAsset(playablecharcters[characterIndex], Texture.class));

        // Triggers an event when the button is pressed
        startBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Start button clicked");
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

        stage.addActor(menuIndicator);
        menuIndicator.setPosition(-100,-100);
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
            MainMenuScreen.playButtonSound(BROWSE);
            characterIndex--;
            buttons.clear();
            imageButtons.clear();
            dispose();
            create();
        } else {
            characterIndex = 2;
            MainMenuScreen.playButtonSound(BROWSE);
            buttons.clear();
            imageButtons.clear();
            dispose();
            create();
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
            MainMenuScreen.playButtonSound(BROWSE);
            characterIndex++;
            buttons.clear();
            imageButtons.clear();
            dispose();
            create();
        } else {
            characterIndex = 0;
            MainMenuScreen.playButtonSound(BROWSE);
            buttons.clear();
            imageButtons.clear();
            dispose();
            create();
        }
    }

    /**
     * Updates currentCharacterAtlas.txt
     */
    public void writeAtlas(){
        try (FileWriter writer = new FileWriter("configs/currentCharacterAtlas.txt")) {
            writer.write(this.playableAtlas[this.characterIndex]);
            logger.info("Writing new atlas to settings.");
        } catch (Exception e){
            logger.debug("Could not load the atlas after character change was made.");
        }
    }


    /**
     * Moves the menuFrame up
     */
    public static void moveUp(){
        if (notAtTop()) {
            menuIndex--;
            updateMenuFrame();
            MainMenuScreen.playButtonSound(BROWSE);
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
            MainMenuScreen.playButtonSound(BROWSE);
        }
        logger.info("Menu Index is {}", menuIndex);
    }

    private static Image createMenuIndicator() {
        return new Image(ServiceLocator.getResourceService()
                .getAsset("images/ui/elements/menuFrame-LONG.png", Texture.class));
    }

    private static void resetMenuIndex() {
        menuIndex = 0;
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
        TextButton startBtn = buttons.get(0);
        TextButton leadBtn = buttons.get(1);
        TextButton setBtn = buttons.get(2);
        TextButton exitBtn = buttons.get(3);
        float buttonXLocation = (float) graphics.getWidth() / 2 - 430;
        switch (menuIndex) {
            case 0: //Start Button (height of title image + 15f)
                menuIndicator.setPosition(buttonXLocation, (float) graphics.getHeight() / 2);
                hoverMenu(startBtn);
                unhoverMenu(leadBtn);
                unhoverMenu(setBtn);
                unhoverMenu(exitBtn);
                break;
            case 1: //Leaderboard Button (height start btn + 15f)
                menuIndicator.setPosition(buttonXLocation, leadBtn.getY()-10);
                unhoverMenu(startBtn);
                hoverMenu(leadBtn);
                unhoverMenu(setBtn);
                unhoverMenu(exitBtn);
                break;
            case 2: //Settings Button
                menuIndicator.setPosition(buttonXLocation,setBtn.getY()-10);
                unhoverMenu(startBtn);
                unhoverMenu(leadBtn);
                hoverMenu(setBtn);
                unhoverMenu(exitBtn);
                break;
            case 3: //Exit Button
                menuIndicator.setPosition(buttonXLocation,exitBtn.getY()-10);
                unhoverMenu(startBtn);
                unhoverMenu(leadBtn);
                unhoverMenu(setBtn);
                hoverMenu(exitBtn);
                break;
            default:
                logger.debug("Invalid menu button.");
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
                TextButton leadBtn = buttons.get(1);
                buttons.clear();
                imageButtons.clear();
                leadBtn.toggle();
                break;
            case 2: //Settings Button
                TextButton setBtn = buttons.get(2);
                buttons.clear();
                imageButtons.clear();
                setBtn.toggle();
                break;
            case 3: //Exit Button
                TextButton exitBtn = buttons.get(3);
                exitBtn.toggle();
                break;
            default:
                logger.debug("Invalid menu button.");
                break;
        }
    }
}



