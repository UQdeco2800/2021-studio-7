package com.deco2800.game.screens.menu;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.deco2800.game.generic.ResourceService;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.RetroactiveDisplay;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

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
    private static final String[] CHARACTER_ATLASES = {
        "images/characters/boy_01/boy_01.atlas",
        "images/characters/girl_00/girl_00.atlas",
        "images/characters/boy_00/boy_00.atlas"
    };
    private static final String ANIMATION_NAME = "standing_south_normal";
    private Table characterContainer;
    private int characterIndex = 0;

    @Override
    public void create() {
        super.create();

        Image background = new Image(ServiceLocator.getResourceService().getAsset(UI_TEXTURES[0], Texture.class));
        table.setBackground(background.getDrawable());

        Image title = new Image(ServiceLocator.getResourceService().getAsset(UI_TEXTURES[1], Texture.class));
        table.add(title).colspan(2).pad(stage.getWidth() * 0.05f).row();

        table.add(createButtons())
            .bottom().padBottom(stage.getHeight() * 0.19f).growY()
            .left().padLeft(stage.getHeight() * 0.15f).width(stage.getWidth() * 0.20f);
        table.add(createCharacterContainer()).grow()
            .bottom().padTop(stage.getHeight() * 0.125f)
            .right().padRight(stage.getWidth() * 0.10f).width(stage.getWidth() * 0.30f);
    }

    @Override
    protected Table createButtons() {
        buttonTable = new Table();
        traverseBackwards = new int[]{Keys.UP, Keys.W};
        traverseForwards = new int[]{Keys.DOWN, Keys.S};
        enter = new int[]{Keys.ENTER};

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
        buttonTable.add(startBtn).growX().padBottom(stage.getHeight() * 0.075f).row();

        TextButton leaderboardBtn = new TextButton("Leaderboard", skin);
        leaderboardBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    logger.debug("Leaderboard button clicked");
                    entity.getEvents().trigger("enter_leaderboard");
                }
            });
        buttonTable.add(leaderboardBtn).growX().padBottom(stage.getHeight() * 0.075f).row();

        TextButton settingsBtn = new TextButton("Settings", skin);
        settingsBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    logger.debug("Settings button clicked");
                    entity.getEvents().trigger("enter_settings");
                }
            });
        buttonTable.add(settingsBtn).growX().padBottom(stage.getHeight() * 0.075f).row();

        TextButton exitBtn = new TextButton("Exit", skin);
        exitBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    logger.debug("Exit button clicked");
                    entity.getEvents().trigger("exit");
                }
            });
        buttonTable.add(exitBtn).growX().row();

        triggerHighlight();

        return buttonTable;
    }

    private Table createCharacterContainer() {
        characterContainer = new Table();

        Image leftArrow = new Image(ServiceLocator.getResourceService().getAsset(ARROW_TEXTURES[2], Texture.class));
        leftArrow.setScaling(Scaling.fit);
        characterContainer.add(leftArrow).width(stage.getWidth() * 0.03f).grow();

        CharacterActor character = new CharacterActor();
        for (String atlas : CHARACTER_ATLASES) {
            character.addAnimation(
                ANIMATION_NAME, ServiceLocator.getResourceService().getAsset(atlas, TextureAtlas.class));
        }
        character.startAnimation(characterIndex);
        character.setScaling(Scaling.fit);
        characterContainer.add(character).grow().bottom().padLeft(5f).padRight(5f);

        Image rightArrow = new Image(ServiceLocator.getResourceService().getAsset(ARROW_TEXTURES[1], Texture.class));
        rightArrow.setScaling(Scaling.fit);
        characterContainer.add(rightArrow).width(stage.getWidth() * 0.03f).grow();

        return characterContainer;
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
            entity.getEvents().trigger("play_sound", "confirm");
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
            characterIndex = (characterIndex + direction) % characterContainer.getChildren().size;
            ((CharacterActor) characterContainer.getChild(1)).startAnimation(characterIndex);

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

            ResourceService service = ServiceLocator.getResourceService();
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
        ServiceLocator.getResourceService().loadAssets(CHARACTER_ATLASES, TextureAtlas.class);
    }

    @Override
    public void unloadAssets() {
        logger.debug("   Unloading menu display assets");
        super.unloadAssets();
        ServiceLocator.getResourceService().unloadAssets(UI_TEXTURES);
        ServiceLocator.getResourceService().unloadAssets(ARROW_TEXTURES);
        ServiceLocator.getResourceService().unloadAssets(CHARACTER_ATLASES);
    }

    private static class CharacterActor extends Image {

        List<Animation<TextureRegion>> animations = new ArrayList<>();
        Animation<TextureRegion> currentAnimation;
        float time = 0f;

        public void addAnimation(String name, TextureAtlas atlas) {
            Array<TextureAtlas.AtlasRegion> regions = atlas.findRegions(name);
            if (regions.size == 0) {
                return;
            }
            animations.add(new Animation<>(0.1f, regions, Animation.PlayMode.LOOP));
        }

        public void startAnimation(int index) {
            currentAnimation = animations.get(index);
            time = 0f;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            if (currentAnimation == null) {
                return;
            }
            setDrawable(new TextureRegionDrawable(currentAnimation.getKeyFrame(time)));
            time += ServiceLocator.getTimeSource().getDeltaTime();
            super.draw(batch, parentAlpha);
        }
    }
}



