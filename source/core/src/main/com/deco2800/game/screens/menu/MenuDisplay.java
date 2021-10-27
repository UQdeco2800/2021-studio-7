package com.deco2800.game.screens.menu;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.RetroactiveDisplay;

import java.io.FileWriter;

public class MenuDisplay extends RetroactiveDisplay {
    private static final String[] UI_TEXTURES = {
        "images/main_menu/bgart.png",
        "images/ui/title/RETROACTIVE-large.png",
        "images/ui/elements/menuFrame-LONG.png",
    };
    private static final String[] ARROW_TEXTURES = {
        "images/main_menu/pointer-L.png",
        "images/main_menu/pointer-R.png",
        "images/main_menu/pointer-L-inactive.png",
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
    private Image menuSelectorImage;
    private VerticalGroup menuButtonsContainer;
    private HorizontalGroup characterContainer;
    private int menuButtonIndex = 0;
    private int characterIndex = 0;

    @Override
    protected void addActors() {
        table = new Table();
        table.setFillParent(true);

        Image background = new Image(ServiceLocator.getResourceService().getAsset(UI_TEXTURES[0], Texture.class));
        table.setBackground(background.getDrawable());

        Image title = new Image(ServiceLocator.getResourceService().getAsset(UI_TEXTURES[1], Texture.class));
        title.setAlign(Align.center);

        menuSelectorImage = new Image(ServiceLocator.getResourceService().getAsset(UI_TEXTURES[2], Texture.class));
        menuButtonsContainer = createMenuButtonContainer();
        characterContainer = createCharacterContainer();

        table.add(title).colspan(2).padTop(50f);
        table.row();
        table.add(menuButtonsContainer).expandY().fillY().fillX();
        table.add(characterContainer).expandY().fillY().fill();
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
                    writeAtlas();
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
                    entity.getEvents().trigger("enter_leaderboard");
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
                    entity.getEvents().trigger("enter_settings");
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

    @Override
    protected void keyDown(int keyCode) {
        switch (keyCode) {
            case Keys.UP:
            case Keys.W:
                entity.getEvents().trigger("play_sound", "browse");
                changeMenuButton(-1);
                break;
            case Keys.LEFT:
            case Keys.A:
                entity.getEvents().trigger("play_sound", "browse");
                changeCharacter(-1);
                break;
            case Keys.DOWN:
            case Keys.S:
                entity.getEvents().trigger("play_sound", "browse");
                changeMenuButton(1);
                break;
            case Keys.RIGHT:
            case Keys.D:
                entity.getEvents().trigger("play_sound", "browse");
                changeCharacter(1);
                break;
            case Keys.ENTER:
                ((TextButton) menuButtonsContainer.getChild(menuButtonIndex)).toggle();
            case Keys.ESCAPE:
                entity.getEvents().trigger("exit");
            default:
        }
    }

    private void changeMenuButton(int direction) {
        if ((direction < 0 && menuButtonIndex == 0) ||
            (direction > 0 && menuButtonIndex == menuButtonsContainer.getChildren().size - 1)) {
            direction = 0;
        }

        if (direction != 0) {
            ((TextButton) menuButtonsContainer.getChild(menuButtonIndex)).setBackground((Drawable) null);
            menuButtonIndex = (menuButtonIndex + direction) % menuButtonsContainer.getChildren().size;
            ((TextButton) menuButtonsContainer.getChild(menuButtonIndex)).setBackground(menuSelectorImage.getDrawable());
        }
    }

    private void changeCharacter(int direction) {
        if (direction < 0 && characterIndex == 0 ||
            direction > 0 && characterIndex == characterContainer.getChildren().size - 1) {
            direction = 0;
        }

        if (direction != 0) {
            ResourceService service = ServiceLocator.getResourceService();
            characterIndex = (characterIndex + direction) % characterContainer.getChildren().size;
            ((Image) characterContainer.getChild(1)).setDrawable(
                new Image(service.getAsset(CHARACTER_TEXTURES[characterIndex], Texture.class)).getDrawable());

            if (characterIndex == 0) {
                ((Image) characterContainer.getChild(0)).setDrawable(
                    new Image(service.getAsset(ARROW_TEXTURES[0], Texture.class)).getDrawable());
                ((Image) characterContainer.getChild(2)).setDrawable(
                    new Image(service.getAsset(ARROW_TEXTURES[1], Texture.class)).getDrawable());
            } else if (characterIndex == characterContainer.getChildren().size - 1) {
                ((Image) characterContainer.getChild(0)).setDrawable(
                    new Image(service.getAsset(ARROW_TEXTURES[2], Texture.class)).getDrawable());
                ((Image) characterContainer.getChild(2)).setDrawable(
                    new Image(service.getAsset(ARROW_TEXTURES[3], Texture.class)).getDrawable());
            }
        }
    }

    @Override
    public void show() {
        super.show();
        changeMenuButton(-999);
    }

    /**
     * Updates currentCharacterAtlas.txt
     */
    public void writeAtlas() {
        try (FileWriter writer = new FileWriter("configs/currentCharacterAtlas.txt")) {
            writer.write(CHARACTER_ATLASES[characterIndex]);
            logger.info("Writing new atlas to settings.");
        } catch (Exception e) {
            logger.debug("Could not load the atlas after character change was made.");
        }
    }

    @Override
    public void loadAssets() {
        logger.debug("   Loading menu display assets");
        super.loadAssets();
        ServiceLocator.getResourceService().loadAssets(UI_TEXTURES, Texture.class);
        ServiceLocator.getResourceService().loadAssets(ARROW_TEXTURES, Texture.class);
        ServiceLocator.getResourceService().loadAssets(CHARACTER_TEXTURES, Texture.class);
    }

    @Override
    public void unloadAssets() {
        logger.debug("   Unloading menu display assets");
        super.unloadAssets();
        ServiceLocator.getResourceService().unloadAssets(UI_TEXTURES);
        ServiceLocator.getResourceService().unloadAssets(ARROW_TEXTURES);
        ServiceLocator.getResourceService().unloadAssets(CHARACTER_TEXTURES);
    }
}



