package com.deco2800.game.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.RetroactiveDisplay;

/**
 * UI component for displaying the Context screen.
 */
public class ContextDisplay extends RetroactiveDisplay {
    private static final String[] CONTEXT_MESSAGES = {
        "It's 11:00pm. The year is currently 1982. It's a school night.\n\n" +
            "You've nearly finished your new game, but your mother is awake and she knows that you are too.\n\n" +
            "You have until she gets home at 2:00AM to get to bed or else she's going to catch you; and if she does?\n\n\n" +
            "Well, it may as well be the end of the world...\n\n" +
            "    \n" +
            "Movement: WASD\n\nInteract: E\n\nSprint: shift",
        "You've escaped with your life this time, but the odds are against you tonight.\n\n\n" +
            "You've put off the chores in favor of finishing the new Exhale of the City (TM) game.\n\n\n" +
            "T-minus two hours until Mum gets home, complete your chores and head to bed!"
    };
    private static final String[] PROMPT_MESSAGES = {
        "PLEASE ENTER YOUR USERNAME",
        "PRESS ENTER TO SKIP",
        "PRESS ENTER TO CONTINUE"
    };
    private static final long NORMAL_TICK_RATE = 40L;
    private long lastTime = 0L;
    private ContextPhase phase = ContextPhase.USERNAME;
    private TextField username;
    private Label context;
    private Label prompt;
    private Label warning;
    private String currentText = "";
    private int index = 0;

    @Override
    public void create() {
        super.create();

        Pixmap backgroundMap = new Pixmap(1, 1, Pixmap.Format.RGB565);
        backgroundMap.setColor(Color.BLACK);
        backgroundMap.fill();
        TextureRegionDrawable background = new TextureRegionDrawable(new TextureRegion(new Texture(backgroundMap)));
        table.setBackground(background);

        if (phase == ContextPhase.USERNAME) {
            table.add(createUsernameContainer());
            stage.setKeyboardFocus(username);
        } else {
            createContextContainer();
        }
    }

    @Override
    protected Group createButtons() {
        return null;
    }

    private VerticalGroup createUsernameContainer() {
        VerticalGroup usernameContainer = new VerticalGroup();
        usernameContainer.space(50f);

        int colWidth = Gdx.graphics.getWidth() / 10;
        prompt = new Label(PROMPT_MESSAGES[0], skin, "title");
        prompt.setFontScale((colWidth * 10f) / 1000f);
        prompt.addAction(Actions.alpha(0));
        prompt.addAction(Actions.forever(Actions.sequence(Actions.fadeIn(1f), Actions.fadeOut(1f))));
        usernameContainer.addActor(prompt);

        username = new TextField("", skin);
        username.setScale(5f);
        usernameContainer.addActor(username);

        warning = new Label("Please input a valid username", skin, "large");
        warning.setVisible(false);
        usernameContainer.addActor(warning);

        return usernameContainer;
    }

    private void createContextContainer() {
        context = new Label("", skin, "large");
        context.setAlignment(Align.topLeft);
        context.setWrap(true);
        table.add(context).grow()
            .size(table.getWidth() * 0.75f, table.getHeight() * 0.50f)
            .pad(table.getHeight() * 0.20f, table.getWidth() * 0.10f,
                table.getHeight() * 0.20f, table.getWidth() * 0.10f);

        prompt = new Label("", skin, "title");

        table.row();
        table.add(prompt).padBottom(table.getHeight() * 0.10f);
    }

    @Override
    protected void keyDown(int keyCode) {
        switch (keyCode) {
            case Keys.ENTER:
                entity.getEvents().trigger("play_sound", "confirm");
                if (phase == ContextPhase.USERNAME) {
                    String usernameString = username.getText();
                    if (!usernameString.isBlank()) {
                        ServiceLocator.getGame().setUsername(usernameString);
                        updatePhase();
                    } else {
                        warning.setVisible(true);
                    }
                    break;
                } else {
                    entity.getEvents().trigger("exit_context");
                }
                break;
            case Keys.ESCAPE:
                entity.getEvents().trigger("play_sound", "confirm");
                entity.getEvents().trigger("queue_main_menu");
                break;
            default:
                if (phase == ContextPhase.USERNAME) {
                    stage.keyDown(keyCode);
                }
        }
    }

    @Override
    public void update() {
        long currentTime = ServiceLocator.getTimeSource().getTime();
        if (phase != ContextPhase.USERNAME && index < CONTEXT_MESSAGES[phase.ordinal() - 1].length() &&
            currentTime - lastTime >= NORMAL_TICK_RATE) {
            lastTime = currentTime;
            updateContext();
            updatePrompt();
        }
    }

    private void updateContext() {
        currentText += CONTEXT_MESSAGES[phase.ordinal() - 1].charAt(index);
        context.setText(currentText);
        if (currentText.charAt(index) == '.') {
            lastTime += 500L;
        } else if (currentText.charAt(index) == '?') {
            lastTime += 2000L;
        }
        index += 1;
    }

    private void updatePrompt() {
        if (index == 10) {
            prompt.setText(PROMPT_MESSAGES[1]);
            prompt.addAction(Actions.forever(Actions.fadeIn(10f)));
        } else if (index == CONTEXT_MESSAGES[phase.ordinal() - 1].length()) {
            prompt.setText(PROMPT_MESSAGES[2]);

            prompt.addAction(Actions.alpha(0));
            prompt.addAction(Actions.forever(Actions.fadeIn(5f)));
        }
    }

    private void updatePhase() {
        table.clear();
        currentText = "";
        if (ServiceLocator.getGame().getLevel() == 1) {
            phase = ContextPhase.STORY_ONE;
        } else {
            phase = ContextPhase.STORY_TWO;
        }
        createContextContainer();
    }

    @Override
    public void loadAssets() {
        logger.debug("   Loading context display assets");
        super.loadAssets();
    }

    @Override
    public void unloadAssets() {
        logger.debug("   Unloading context display assets");
        super.unloadAssets();
    }

    private enum ContextPhase {
        USERNAME, STORY_ONE, STORY_TWO
    }
}