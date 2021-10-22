package com.deco2800.game.screens.context;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.deco2800.game.ui.components.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * A ui component for displaying the Context screen.
 */
public class ContextScreenDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ContextScreenDisplay.class);
    private static final float Z_INDEX = 2f;
    private TextField txtUserName;
    private Label displayText;
    private String text = "";
    private Table table;
    private int charCount = 0;
    private String currentText = "";
    private boolean story = false;
    private boolean printed = false;
    private boolean start = false;
    private static final String TITLE = "title";

    public ContextScreenDisplay() {
        super();
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    @Override
    public void update() {
        print();
    }

    private void print() {
        int rowHeight = Gdx.graphics.getHeight() / 16;
        int colWidth = Gdx.graphics.getWidth() / 10;
        if (charCount < this.text.length()) {
            currentText += text.charAt(charCount);
            displayText.setText(currentText);
            if (charCount != 0 && text.charAt(charCount - 1) == '.') {
                printWait(500);
            } else if (charCount != 0 && text.charAt(charCount - 1) == '?') {
                printWait(2000);
            }
            printWait(50);
            charCount += 1;
        }
        if (charCount == 10 && ContextScreen.getSkip() && !printed) {
            printed = true;
            Label skip = new Label("PRESS ENTER TO SKIP", skin, TITLE);
            skip.setFontScale(1f);
            skip.setPosition(colWidth, rowHeight);
            skip.addAction(Actions.alpha(0));
            skip.addAction(Actions.forever(Actions.fadeIn(10f)));
            stage.addActor(skip);
        } else if (charCount != 0 && charCount == text.length() &&!printed) {
            printed = true;
            ContextScreen.setSkip();
            Label cont = new Label("PRESS ENTER TO CONTINUE", skin, TITLE);
            cont.setFontScale(1f);
            cont.setPosition(colWidth, rowHeight);
            cont.addAction(Actions.alpha(0));
            cont.addAction(Actions.forever(Actions.fadeIn(5f)));
            stage.addActor(cont);
        }
        if (charCount == 4) {
            this.start = true;
        }
    }


    private void printWait(int timeout) {
        try {
            TimeUnit.MILLISECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            logger.error("Sleep interrupted");
            Thread.currentThread().interrupt();
        }
    }

    private void addActors() {
        switch(ContextScreen.getScreen()) {
            case 1:
                table = new Table();
                table.setFillParent(true);
                int colWidth = Gdx.graphics.getWidth() / 10;
                Label enterName = new Label("PLEASE ENTER YOUR USERNAME", skin, TITLE);
                enterName.setFontScale((colWidth*10f)/1000f);
                enterName.addAction(Actions.alpha(0));
                enterName.addAction(Actions.forever(Actions.sequence(Actions.fadeIn(1f), Actions.fadeOut(1f))));

                this.txtUserName = new TextField("", skin);
                this.txtUserName.setScale(5f);

                table.add(enterName).padTop(50f);
                table.row();
                table.add(txtUserName).padTop(50f);
                stage.addActor(table);
                stage.setKeyboardFocus(txtUserName);
                stage.addActor(table);
                Gdx.input.setInputProcessor(stage);
                break;
            case 2:
                tellStory();
                break;
            default:
                break;
        }

    }

    public TextField getUserName() {
        return this.txtUserName;
    }

    public boolean userNameValid() {
        return !this.txtUserName.getText().isBlank();
    }

    public void displayWarning() {
        Label warningLabel = new Label("Please input a valid username", skin, "large");
        stage.addActor(warningLabel);
    }

    public void clearTable() {
        table.clear();
        stage.clear();
    }

    public void tellStory() {
        this.story = true;
        int rowHeight = Gdx.graphics.getHeight() / 16;
        int colWidth = Gdx.graphics.getWidth() / 10;
        displayText = new Label("", skin, "large");
        displayText.setSize(colWidth*8f, rowHeight*12f);
        displayText.setPosition(colWidth, (float) rowHeight * 3);
        displayText.setFontScale((colWidth*10f)/1280f); // Scale font to screen size
        displayText.setWrap(true);
        stage.addActor(displayText);
        switch (ContextScreen.getScreen()) {
            case 1:
                this.text = "It's 11:00pm. The year is currently 1982. It's a school night. \n\n\n You've nearly " +
                        "finished your" +
                        " new game, but your mother is awake and she knows that you are too.\n\n\n You have until she" +
                        " gets home at 2:00AM to get to bed or else she's going to catch you; and if she does?\n\n\n\n\n " +
                        "Well, it may as well be the end of the world. . .\n\n\n\n Movement: ASDW\n\n Interact: " +
                        "E\n\n Sprint: shift" ;
                break;
            case 2:
                this.text = "You've escaped with your life this time, but the odds are against you tonight" +
                        ".\n\n\n You've put off the chores in favor of finishing the new Exhale of the City (TM) game. " +
                        "\n\n\n T-minus two hours until Mum gets home, complete your chores and head to bed!";
                break;
            default:
                break;
        }
    }

    public boolean getStoryStatus() {
        return this.story;
    }

    public boolean getPrintStatus() {
        return this.start;
    }

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

}
