package com.deco2800.game.screens.menu;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.RetroactiveDisplay;

import java.io.FileWriter;
import java.security.Key;

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
    private HorizontalGroup characterContainer;
    private int characterIndex = 0;

    @Override
    public void create() {
        super.create();

        Image background = new Image(ServiceLocator.getResourceService().getAsset(UI_TEXTURES[0], Texture.class));
        table.setBackground(background.getDrawable());

        Image title = new Image(ServiceLocator.getResourceService().getAsset(UI_TEXTURES[1], Texture.class));
        table.add(title).center().colspan(2).padTop(50f);

        characterContainer = createCharacterContainer();

        table.row();
        table.add(createButtons()).expandY().fillY().fillX();
        table.add(characterContainer).expandY().fillY().fill();
    }

    @Override
    protected Group createButtons() {
        buttonContainer = new VerticalGroup();
        traverseBackwards = new int[]{Keys.UP, Keys.W};
        traverseForwards = new int[]{Keys.DOWN, Keys.S};
        enter = new int[]{Keys.ENTER};

        ((VerticalGroup) buttonContainer).space(50f);

        TextButton startBtn = new TextButton("Start", skin);
        startBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    logger.debug("Start button clicked");
                    writeAtlas();
                    entity.getEvents().trigger("queue_main_game");
                }
            });
        buttonContainer.addActor(startBtn);

        TextButton leaderboardBtn = new TextButton("Leaderboard", skin);
        leaderboardBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    logger.debug("Leaderboard button clicked");
                    entity.getEvents().trigger("enter_leaderboard");
                }
            });
        buttonContainer.addActor(leaderboardBtn);

        TextButton settingsBtn = new TextButton("Settings", skin);
        settingsBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    logger.debug("Settings button clicked");
                    entity.getEvents().trigger("enter_settings");
                }
            });
        buttonContainer.addActor(settingsBtn);

        TextButton exitBtn = new TextButton("Exit", skin);
        exitBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    logger.debug("Exit button clicked");
                    entity.getEvents().trigger("exit");
                }
            });
        buttonContainer.addActor(exitBtn);

        triggerHighlight();

        return buttonContainer;
    }

    private HorizontalGroup createCharacterContainer() {
        HorizontalGroup container = new HorizontalGroup();
        container.space(5f);

        Image leftArrow = new Image(ServiceLocator.getResourceService().getAsset(ARROW_TEXTURES[2], Texture.class));
        container.addActor(leftArrow);

        Image character = new Image(ServiceLocator.getResourceService().getAsset(CHARACTER_TEXTURES[0], Texture.class));
        container.addActor(character);

        Image rightArrow = new Image(ServiceLocator.getResourceService().getAsset(ARROW_TEXTURES[1], Texture.class));
        container.addActor(rightArrow);

        return container;
    }

    @Override
    protected void keyDown(int keycode) {
        super.keyDown(keycode);
        if (keycode == Keys.LEFT || keycode == Keys.A) {
            traverseCharacter(-1);
        } else if (keycode == Keys.RIGHT || keycode == Keys.D) {
            traverseCharacter(1);
        }
    }

    @Override
    protected void keyUp(int keycode) {
        super.keyUp(keycode);
        if (keycode == Keys.ESCAPE) {
            entity.getEvents().trigger("exit");
        }
    }

    private void traverseCharacter(int direction) {
        entity.getEvents().trigger("play_sound", "browse");

        if (direction < 0 && characterIndex == 0 ||
            direction > 0 && characterIndex == characterContainer.getChildren().size - 1) {
            direction = 0;
        }

        if (direction != 0) {
            ResourceService service = ServiceLocator.getResourceService();
            characterIndex = (characterIndex + direction) % characterContainer.getChildren().size;
            ((Image) characterContainer.getChild(1)).setDrawable(
                new Image(service.getAsset(CHARACTER_TEXTURES[characterIndex], Texture.class)).getDrawable());

            int leftIndex;
            int rightIndex;

            if (characterIndex == 0) {
                leftIndex = 2;
                rightIndex = 1;
            } else if (characterIndex == characterContainer.getChildren().size - 1) {
                leftIndex = 0;
                rightIndex = 3;
            } else {
                leftIndex = 0;
                rightIndex = 1;
            }

            ((Image) characterContainer.getChild(0)).setDrawable(
                new Image(service.getAsset(ARROW_TEXTURES[leftIndex], Texture.class)).getDrawable());
            ((Image) characterContainer.getChild(2)).setDrawable(
                new Image(service.getAsset(ARROW_TEXTURES[rightIndex], Texture.class)).getDrawable());
        }
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



