package com.deco2800.game.chores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.ui.components.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Creates a toggle-able and variable text box display at the bottom of the screen.
 *
 * By default, will not display anything, but can call MainGameTextDisplay.display to display a
 * box with text (and optional image). Can then call remove to remove it.
 */
public class ChoreUI extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ChoreUI.class);
    private String text;

    @Override
    public void create() {
        super.create();
    }

    /**
     * Displays the list of chores to the screen.
     */
    public void display() {
        // Divide screen into a more manageable grid
        int rowHeight = Gdx.graphics.getHeight() / 16;
        int colWidth = Gdx.graphics.getWidth() / 10;

        /*
        // Read through text file
        ArrayList<String> choreList = getChoreList();
        ArrayList<String> choreStrings = new ArrayList<>();
        ArrayList<String> choreEntities = new ArrayList<>();
        // format: [count, "location:Entity:description:textbox string"]
        for (String s : choreList) {
            String[] list = s.split(":");
            choreStrings.add(list[0] + ": " + list[2]);
            choreEntities.add(list[1]);
        }

        // Format string list
        StringBuilder chores = new StringBuilder();
        chores.append("Things I need to do:\n");
        for (String choreString : choreStrings) {
            if (choreString == null) {
                break;
            }
            chores.append(choreString).append("\n");
        }
        */

        // Get the list of chores from the ChoreController
        ArrayList<Chore> chores = ServiceLocator.getChoreController().getChores();
        String[] choreDescriptions = new String[chores.size()];
        for (int i = 0; i < chores.size(); i++) {
            choreDescriptions[i] = chores.get(i).getDescription();
        }

        // Format the output text
        StringBuilder choreText = new StringBuilder();
        choreText.append("Things I need to do:\n");
        for (String choreDescription : choreDescriptions) {
            choreText.append(choreDescription).append("\n");
        }

        // Display Text
        Label displayText = new Label(choreText, skin, "large");
        displayText.setSize(colWidth*6f, rowHeight*6f);
        displayText.setPosition(colWidth/12f, rowHeight*6f);
        displayText.setFontScale((colWidth*10f)/1280f); // Scale font to screen size
        displayText.setAlignment(Align.topLeft);
        //displayText.setWrap(true);

        stage.addActor(displayText);
    }

    /*
    private ArrayList<String> getChoreList() {
        File input = new File("configs/chores.txt");
        BufferedReader br = null;
        ArrayList<String> choreList = new ArrayList<String>();
        int lineCount = 0;
        String currentLine;

        try {
            br = new BufferedReader(new FileReader(input));
            while ((currentLine = br.readLine()) != null) {
                if ("".equals(currentLine)) {
                    // Current line is blank, assume EOF
                    break;
                }
                choreList.add(lineCount, currentLine);
                lineCount++;
            }
        } catch (IOException e) {
            logger.error("IOException in reading configs/testChores.txt");
        } finally {
            if (br != null){
                try {
                    br.close();
                } catch (IOException e){
                    logger.error("IOException in closing reader for configs/testChores.txt");
                }
            }
        }
        return choreList;
    }
     */

    /**
     * Removes all current visual components from the screen (but doesn't do a full cleanup)
     */
    private void hide() {
        //TODO Have objectives toggle on/ off at keypress
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}