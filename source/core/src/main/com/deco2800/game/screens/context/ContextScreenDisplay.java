package com.deco2800.game.screens.context;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.deco2800.game.ui.components.UIComponent;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
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
    private long startTime;
    private Table table;
    private int charCount = 0;
    private String currentText = "";
    private boolean story;
    private boolean escape;

    public ContextScreenDisplay() {
        super();
    }

    @Override
    public void create() {
        super.create();
        addActors();
        story = false;
    }

    @Override
    public void update() {
        print();
        /*if (story && ContextScreen.getSkip()) {
            Label skip = new Label("PRESS ENTER TO SKIP", skin, "title");
            skip.setFontScale(0.5f);
            skip.addAction(Actions.alpha(0));
            skip.addAction(Actions.sequence(Actions.fadeIn(4f)));
        } else if (story) {
            Label cont = new Label("PRESS ENTER TO CONTINUE", skin, "title");
            cont.setFontScale(0.5f);
        }*/
    }

    private void print() {
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
        if (story) {
            ContextScreen.setSkip();
        }
    }


    private void printWait(int timeout) {
        try {
            TimeUnit.MILLISECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            logger.error("Sleep interupted");
        }
    }

    /*private void printCont(boolean escape) {
        if ()
    }*/


    private void addActors() {
        table = new Table();
        table.setFillParent(true);
        int colWidth = Gdx.graphics.getWidth() / 10;
        Label enterName = new Label("PLEASE ENTER YOUR GAMER TAG", skin, "title");
        enterName.setFontScale((colWidth*10f)/1000f);
        enterName.addAction(Actions.alpha(0));
        enterName.addAction(Actions.forever(Actions.sequence(Actions.fadeIn(1f), Actions.fadeOut(1f))));

        this.txtUserName = new TextField("", skin);
        this.txtUserName.setColor(Color.BLACK);
        this.txtUserName.setScale(5f);
        //this.txtUserName.setScale((colWidth*10f)/800f);

        table.add(enterName).padTop(50f);
        table.row();
        table.add(txtUserName).padTop(50f);
        stage.addActor(table);
        stage.setKeyboardFocus(txtUserName);
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);

        /*switch(level++) {
            case 1:

                break;*/
            /*case 2:
                int colWidth = Gdx.graphics.getWidth() / 10;
                this.text = "It's 12:00pm. The year is currently 1982. It's a school night. You've nearly finished your " +
                        "new game, but your mother is awake and she knows that you are too. You have until 2AM to" +
                        " get to bed or else she's going to catch you; and if she does? \n\n" +
                        "Well, it may as well be the end of the world.";
                displayText = new Label("", skin, "large");
                displayText.setSize(colWidth*6f, Gdx.graphics.getHeight()/2f);
                displayText.setPosition(colWidth*2f, Gdx.graphics.getHeight()/2f);
                displayText.setFontScale((colWidth*10f)/1280f); // Scale font to screen size
                displayText.setWrap(true);
                stage.addActor(displayText);
                startTime = ServiceLocator.getTimeSource().getTime();
                break;*/
        /*
        table.setFillParent(true);
        // Add button container to the table.
        // Easily sorts buttons vertically and separates padding settings from table.
        // It is assumed that more buttons will eventually be added.
        VerticalGroup buttonContainer = new VerticalGroup();
        buttonContainer.fill();
        buttonContainer.bottom().right();
        buttonContainer.space(10f);


        HorizontalGroup imageContainer = new HorizontalGroup();
        imageContainer.fill();
        stage.addActor(table);

        table = new Table();
        table.setFillParent(true);

        //This is the image of the mum and the bed, they were combined as a PNG as it is easier to
        //be added rather than build multiple assets for it. This is because there are no functional
        //Actions by them
        Image context =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/context_screen/context_screen.PNG", Texture.class));

        table.add(context);
        table.row();
        table.add(buttonContainer).padTop(30f);
         */

    }

    public TextField getUserName() {
        return this.txtUserName;
    }

    public boolean userNameValid() {
        return !this.txtUserName.getText().isBlank();
    }

    public void displayWarning() {
        Label warningLabel = new Label("Please input a valid username", skin, "large");
        //warningLabel.setFontScale(1.5f);
        stage.addActor(warningLabel);
    }

    public void clearTable() {
        table.clear();
    }

    public void tellStory() {
        story = true;
        int rowHeight = Gdx.graphics.getHeight() / 16;
        int colWidth = Gdx.graphics.getWidth() / 10;
        displayText = new Label("", skin, "large");
        displayText.setSize(colWidth*8f, rowHeight*12f);
        displayText.setPosition(colWidth, rowHeight*4);
        displayText.setFontScale((colWidth*10f)/1280f); // Scale font to screen size
        displayText.setWrap(true);
        stage.addActor(displayText);
        switch (ContextScreen.getScreen()) {
            case 1:
                this.text = "It's 12:00pm. The year is currently 1982. It's a school night. \n\n\n You've nearly " +
                        "finished your" +
                        " new game, but your mother is awake and she knows that you are too.\n\n\n You have until she" +
                        " " +
                        "gets " +
                        "home at 2:00AM to get to bed or else she's going to catch you; and if she does?\n\n\n\n\n " +
                        "Well, it may as well be the end of the world. . .";
        }


    }

    public boolean getStoryStatus() {
        return this.story;
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
        super.dispose();
    }

}
