package com.deco2800.game.screens.context;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.ui.components.UIComponent;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A ui component for displaying the Context screen.
 */
public class ContextScreenDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ContextScreenDisplay.class);
    private static final float Z_INDEX = 2f;
    private TextField txtUserName;
    private Label displayText;
    private String text;
    private long startTime;
    private Table table;
    private static int level = 1;
    private int charCount = 0;
    private String currentText = "";

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
        /*long currentTime = ServiceLocator.getTimeSource().getTime();

        // Gradually display text across the textbox
        if (level == 2 && charCount < text.length()) {
            currentText += text.charAt(charCount);
            displayText.setText(currentText);
            charCount += 1;
        }*/
    }


    private void addActors() {
        table = new Table();
        table.setFillParent(true);
        Label enterName = new Label("PLEASE ENTER YOUR NAME", skin, "title");
        enterName.addAction(Actions.alpha(0));
        enterName.addAction(Actions.forever(Actions.sequence(Actions.fadeIn(1f), Actions.fadeOut(1f))));

        this.txtUserName = new TextField("", skin);

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
        System.out.println("invalid name");
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
