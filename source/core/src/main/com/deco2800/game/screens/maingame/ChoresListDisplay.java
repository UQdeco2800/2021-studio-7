package com.deco2800.game.screens.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.ui.components.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.*;
import java.io.IOException;

/**
 * Creates a toggle-able and variable text box display at the bottom of the screen.
 *
 * By default, will not display anything, but can call MainGameTextDisplay.display to display a
 * box with text (and optional image). Can then call remove to remove it.
 */
public class ChoresListDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ChoresListDisplay.class);

    @Override
    public void create() {
        super.create();
        // Divide screen into a more manageable grid
        int rowHeight = Gdx.graphics.getHeight() / 16;
        int colWidth = Gdx.graphics.getWidth() / 10;

        // Read through text file
        ArrayList<String> choreList = getChoreList();
        String[] choreStrings = new String[30];
        String[] choreEntities = new String[30];
        // format: [count, "location:Entity:description:textbox string"]
        for (int i = 0; i < choreList.size(); i++) {
            String[] list = choreList.get(i).split(":");
            choreStrings[i] = list[0] + ": " + list[2];
            choreEntities[i] = list[1];
        }

        // Format string list
        StringBuilder chores = new StringBuilder();
        chores.append("Things I need to do:\n");
        for (String choreString : choreStrings) {
            if (choreString == null) {
                break;
            }
            chores.append(choreString + "\n");
        }

        // Display Text
        Label displayText = new Label(chores, skin, "large");
        displayText.setSize(colWidth*6, rowHeight*6);
        displayText.setPosition((float) colWidth/12, (float) rowHeight*6);
        displayText.setFontScale((float) (colWidth*10)/1280); // Scale font to screen size
        displayText.setAlignment(Align.topLeft);
        //displayText.setWrap(true);

        stage.addActor(displayText);
    }

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

    @Override
    protected void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}