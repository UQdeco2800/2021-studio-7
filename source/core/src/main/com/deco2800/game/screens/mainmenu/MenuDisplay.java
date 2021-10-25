package com.deco2800.game.screens.mainmenu;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.KeyboardMenuDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class MenuDisplay extends KeyboardMenuDisplay {
    private static final Logger logger = LoggerFactory.getLogger(MenuDisplay.class);
    private static final String[] UI_TEXTURES = {
            "images/main_menu/bgart.png",
            "images/ui/title/RETROACTIVE-large.png",
            "images/ui/elements/menuFrame-LONG.png",
    };
    private static final String[] ARROW_TEXTURES = {
            "images/main_menu/pointer-L.png",
            "images/main_menu/pointer-L-inactive.png",
            "images/main_menu/pointer-R.png",
            "images/main_menu/pointer-R-inactive.png"
    };
    private static final String[] CHARACTER_TEXTURES = {
            "images/characters/boy_01/boy_01_menu_preview.png",
            "images/characters/girl_00/girl_00_menu_preview.png",
            "images/characters/boy_00/boy_00_menu_preview.png"
    };
    private static final String[] CHARACTER_ATLASES = {
            "images/characters/boy_01/boy_01.atlas",
            "images/characters/girl_00/girl_00.atlas",
            "images/characters/boy_00/boy_00.atlas"
    };

    private Table table;
    private Image menuSelectorImage;
    private List<Image> characterImages;
    private List<Image> characterButtonImages;
    private VerticalGroup menuButtonsContainer;
    private HorizontalGroup characterContainer;
    private int menuButtonIndex = 0;
    private int characterIndex = 0;

    @Override
    public void create() {
        super.create();
        addActors();
        writeAtlas(); //Stores copy of the first character
    }

    @Override
    protected void addActors() {
        table = new Table(skin);
        table.setFillParent(true);

        Image background = new Image(ServiceLocator.getResourceService().getAsset(UI_TEXTURES[0], Texture.class));
        table.setBackground(background.getDrawable());

        Image title = new Image(ServiceLocator.getResourceService().getAsset(UI_TEXTURES[1], Texture.class));
        title.setAlign(Align.center);

        menuSelectorImage = new Image(ServiceLocator.getResourceService().getAsset(UI_TEXTURES[2], Texture.class));

        characterImages = new ArrayList<>();
        for (String texture : CHARACTER_TEXTURES) {
            characterImages.add(new Image(ServiceLocator.getResourceService().getAsset(texture, Texture.class)));
        }

        characterButtonImages = new ArrayList<>();
        for (String texture : ARROW_TEXTURES) {
            characterButtonImages.add(new Image(ServiceLocator.getResourceService().getAsset(texture, Texture.class)));
        }

        menuButtonsContainer = createMenuButtonContainer();

        characterContainer = createCharacterContainer();

        table.add(title).colspan(2).padTop(50f);
        table.row();
        table.add(menuButtonsContainer).expandY().fillY().fillX();
        table.add(characterContainer).expandY().fillY().fill();
        stage.addActor(table);
    }

    @Override
    public void onMenuKeyPressed(int keyCode) {
        switch (keyCode) {
            case Keys.W:
                changeMenuButton(-1);
                break;
            case Keys.A:
                changeCharacter(-1);
                break;
            case Keys.S:
                changeMenuButton(1);
                break;
            case Keys.D:
                changeCharacter(1);
                break;
            case Keys.ENTER:
                writeAtlas();
                ((TextButton) menuButtonsContainer.getChild(menuButtonIndex)).toggle();
            case Keys.ESCAPE:
                ((TextButton) menuButtonsContainer.getChild(3)).toggle();
            default:
        }
    }

    private VerticalGroup createMenuButtonContainer() {
        VerticalGroup container = new VerticalGroup();
        container.space(50f);

        TextButton startBtn = new TextButton("Start", skin);
        startBtn.setBackground(menuSelectorImage.getDrawable());
        startBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Start button clicked");
                        entity.getEvents().trigger("main_game");
                    }
                });
        container.addActor(startBtn);

        TextButton leaderboardBtn = new TextButton("Leaderboard", skin);
        leaderboardBtn.setBackground(menuSelectorImage.getDrawable());
        leaderboardBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Leaderboard button clicked");
                        entity.getEvents().trigger("leaderboard");
                    }
                });
        container.addActor(leaderboardBtn);

        TextButton settingsBtn = new TextButton("Settings", skin);
        settingsBtn.setBackground(menuSelectorImage.getDrawable());
        settingsBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Settings button clicked");
                        entity.getEvents().trigger("settings");
                    }
                });
        container.addActor(settingsBtn);

        TextButton exitBtn = new TextButton("Exit", skin);
        leaderboardBtn.setBackground(menuSelectorImage.getDrawable());
        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Exit button clicked");
                        entity.getEvents().trigger("exit");
                    }
                });
        container.addActor(exitBtn);

        return container;
    }

    private HorizontalGroup createCharacterContainer() {
        HorizontalGroup container = new HorizontalGroup();
        container.space(5f);

        Image leftArrow = new Image(ServiceLocator.getResourceService().getAsset(ARROW_TEXTURES[0], Texture.class));
        container.addActor(leftArrow);

        Image character = new Image(ServiceLocator.getResourceService().getAsset(CHARACTER_TEXTURES[0], Texture.class));
        container.addActor(character);

        Image rightArrow = new Image(ServiceLocator.getResourceService().getAsset(ARROW_TEXTURES[3], Texture.class));
        container.addActor(rightArrow);

        return container;
    }

    private void changeMenuButton(int direction) {
        if (direction < 0 && menuButtonIndex == 0 ||
                direction > 0 && menuButtonIndex == menuButtonsContainer.getChildren().size - 1) {
            direction = 0;
        }

        if (direction != 0) {
            int newIndex = (menuButtonIndex + direction) % menuButtonsContainer.getChildren().size;
            ((TextButton) menuButtonsContainer.getChild(menuButtonIndex)).setBackground((Drawable) null);
            menuButtonIndex = newIndex;
            ((TextButton) menuButtonsContainer.getChild(menuButtonIndex)).setBackground(menuSelectorImage.getDrawable());
        }
    }

    private void changeCharacter(int direction) {
        if (direction < 0 && characterIndex == 0 ||
                direction > 0 && characterIndex == characterContainer.getChildren().size - 1) {
            direction = 0;
        }

        if (direction != 0) {
            characterIndex = (characterIndex + direction) % characterContainer.getChildren().size;
            ((Image) characterContainer.getChild(1))
                    .setDrawable(characterImages.get(characterIndex).getDrawable());

            if (characterIndex == 0) {
                ((Image) characterContainer.getChild(1))
                        .setDrawable(characterButtonImages.get(1).getDrawable());
                ((Image) characterContainer.getChild(1))
                        .setDrawable(characterButtonImages.get(2).getDrawable());
            } else if (characterIndex == characterContainer.getChildren().size - 1) {
                ((Image) characterContainer.getChild(1))
                        .setDrawable(characterButtonImages.get(0).getDrawable());
                ((Image) characterContainer.getChild(1))
                        .setDrawable(characterButtonImages.get(3).getDrawable());
            }
        }
    }

    /**
     * Updates currentCharacterAtlas.txt
     */
    public void writeAtlas(){
        try (FileWriter writer = new FileWriter("configs/currentCharacterAtlas.txt")) {
            writer.write(CHARACTER_ATLASES[characterIndex]);
            logger.info("Writing new atlas to settings.");
        } catch (Exception e){
            logger.debug("Could not load the atlas after character change was made.");
        }
    }

    public static List<String> getAssets() {
        return getAssets(".png");
    }

    public static List<String> getAssets(String extension) {
        List<String> assetsWithExtension = new ArrayList<>();
        if (extension.equals(".png")) {
            assetsWithExtension.addAll(List.of(UI_TEXTURES));
            assetsWithExtension.addAll(List.of(ARROW_TEXTURES));
            assetsWithExtension.addAll(List.of(CHARACTER_TEXTURES));
        }
        return assetsWithExtension;
    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }
}



