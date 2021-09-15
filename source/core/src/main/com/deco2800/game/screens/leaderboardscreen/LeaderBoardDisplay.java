package com.deco2800.game.screens.leaderboardscreen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.GdxGame;
import com.deco2800.game.GdxGame.ScreenType;
import com.deco2800.game.files.UserSettings;
import com.deco2800.game.files.UserSettings.DisplaySettings;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.ui.components.UIComponent;
import com.deco2800.game.utils.StringDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.TreeMap;
import java.util.ArrayList;
import java.io.*;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;



/**
 * Leader board screen display.
 */
public class LeaderBoardDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(LeaderBoardDisplay.class);
    private final GdxGame game;

    private Table rootTable;


    public LeaderBoardDisplay(GdxGame game){
    super();
    this.game = game;
    }

    @Override
    public void create() {
        super.create();
        sortLeaderBoard();
        addActors();
    }
    private void addActors() {
        rootTable = new Table();
        rootTable.setFillParent(true);

        Image title = new Image(
                ServiceLocator.getResourceService()
                        .getAsset("images/ui/title/leaderboard.png", Texture.class));

        Table leaderboardtable = makeLeaderBoardTable();
        Table menuBtns = makeMenuBtns();

        rootTable.add(title).expandX().top().padTop(20f);

        rootTable.row().padTop(30f);
        rootTable.add(leaderboardtable).expandX().expandY();

        rootTable.row();
        rootTable.add(menuBtns).fillX();

        stage.addActor(rootTable);

    }


    private Table makeLeaderBoardTable(){
        //TODO
        return new Table();
    }

    private Table makeMenuBtns(){
        TextButton exitBtn = new TextButton("Exit", skin);
        TextButton applyBtn = new TextButton("Apply", skin);

        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Exit button clicked");
                        exitMenu();
                    }
                });

        applyBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Apply button clicked");
                    }
                });

        Table table = new Table();

        table.add(exitBtn).expandX().left().pad(0f, 15f, 15f, 0f);
        table.add(applyBtn).expandX().right().pad(0f, 0f, 15f, 15f);
        return table;
    }



    @Override
    protected void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public void update() {
        stage.act(ServiceLocator.getTimeSource().getDeltaTime());
    }

    @Override
    public void dispose() {
        rootTable.clear();
        super.dispose();
    }


    private void exitMenu() {
        game.setScreen(ScreenType.MAIN_MENU);
    }


    private void sortLeaderBoard() {
        try {
            File input = new File("configs/leaderboard.txt");
            BufferedReader br = new BufferedReader(new FileReader(input));

            TreeMap<String, Integer> leaderboard = new TreeMap<String, Integer>();

            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                if("".equals(currentLine)){
                    continue;
                }
                String[] arrOfLine = currentLine.split(":");
                String username = arrOfLine[0];
                String[] arrOfScores = arrOfLine[1].split(",");

                int totalscore = 0;
                for (String score : arrOfScores) {
                    int levelscore = Integer.parseInt(score);
                    totalscore += levelscore;
                }

                leaderboard.put(username, totalscore);
            }
            Map sortedLeaderboard = valueSort(leaderboard);
            System.out.println(leaderboard);


            FileWriter clearer = new FileWriter("configs/leaderboard.txt");
            clearer.write("");

            FileWriter writer = new FileWriter("configs/leaderboard.txt", true);

            Set set = sortedLeaderboard.entrySet();
            Iterator i = set.iterator();

            while(i.hasNext()) {
                Map.Entry mp = (Map.Entry)i.next();
                writer.write(mp.getKey() + ":" + String.valueOf(mp.getValue()) +"\n");
                System.out.println(mp.getKey() + ":" + String.valueOf(mp.getValue()) +"\n");
                logger.info("Sorted the leaderboard");
            }
            writer.close();

        } catch (IOException e) {
            logger.debug("IOException when reading leaderboard");
        }

    }

    public static <K, V extends Comparable<V> > Map<K, V>
    valueSort(final Map<K, V> map)
    {
        // Static Method with return type Map and
        // extending comparator class which compares values
        // associated with two keys
        Comparator<K> valueComparator = new Comparator<K>() {

            // return comparison results of values of
            // two keys
            public int compare(K k1, K k2)
            {
                int comp = map.get(k1).compareTo(
                        map.get(k2));
                if (comp == 0)
                    return 1;
                else
                    return comp;
            }

        };

        // SortedMap created using the comparator
        Map<K, V> sorted = new TreeMap<K, V>(valueComparator);

        sorted.putAll(map);

        return sorted;
    }

}