package com.deco2800.game.screens.leaderboard;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.GdxGame;
import com.deco2800.game.GdxGame.ScreenType;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.ui.components.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.TreeMap;
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
    private Table leaderTable;
    private static TextButton button;

    public LeaderBoardDisplay(GdxGame game) {
        super();
        this.game = game;
        logger.info("Trying to build leader board...");
    }

    @Override
    public void create() {
        super.create();
        logger.info("Trying to sort leader board...");
        sortLeaderBoard();
        logger.info("Sorted leaderboard.");
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
        logger.info("Trying to create leader board...");
        rootTable.add(leaderboardtable).expandX().expandY();
        rootTable.row();
        rootTable.add(menuBtns).fillX();
        stage.addActor(rootTable);

    }


    private Table makeLeaderBoardTable() {
        leaderTable = new Table();
        logger.info("Trying to get leader board...");
        TreeMap<String, Integer> leaderboard = getLeaderBoard();
        logger.info("Got leader board.");
        Set <Map.Entry<String,Integer>> set = leaderboard.entrySet();
        Iterator <Map.Entry<String,Integer>> i = set.iterator();
        String insert;
        int t = 0;
        while (i.hasNext()) {
            t++;
            if (t == 11){break;}
            leaderTable.row();
            Map.Entry<String,Integer> mp = (Map.Entry) i.next();
            String score = String.valueOf(mp.getValue());
            insert = mp.getKey() + ":" + score;
            Label label = new Label(insert, skin);
            leaderTable.add(label).padTop(15f);
        }
        return leaderTable;
    }

    private Table makeMenuBtns() {
        TextButton exitBtn1 = new TextButton("Exit", skin);
        button = exitBtn1;
        exitBtn1.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Exit button clicked");
                        exitMenu();
                    }
                });

        Table table = new Table();

        table.add(exitBtn1).expandX().left().pad(0f, 15f, 15f, 0f);
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

    public static void exitLB() {
        button.toggle();
    }

    private void sortLeaderBoard() {
        TreeMap<String, Integer> leaderboard = getLeaderBoard();
        Map sortedLeaderboard = valueSort(leaderboard);
        FileWriter clearer = null;
        FileWriter writer = null;
        try {
            clearer = new FileWriter("configs/leaderboard.txt");
            clearer.write("");
            writer = new FileWriter("configs/leaderboard.txt", true);
            Set set = sortedLeaderboard.entrySet();
            Iterator i = set.iterator();
            while (i.hasNext()) {
                Map.Entry mp = (Map.Entry) i.next();
                writer.write(mp.getKey() + ":" + String.valueOf(mp.getValue()) + "\n");
                logger.info("Sorted the leaderboard");
            }
        } catch (IOException e) {
            logger.error("IOException when reading leaderboard");
        } finally {
            if (clearer != null){
                try {
                    clearer.close();
                } catch (IOException e) {
                    logger.error("IOException attempting to close clearer for configs/leaderboard.txt");
                }
            } if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    logger.error("IOException attempting to close writer for configs/leaderboard.txt");
                }
            }
        }
    }

    public static <K, V extends Comparable<V>> Map<K, V>
    valueSort(final Map<K, V> map) {
        // Static Method with return type Map and
        // extending comparator class which compares values
        // associated with two keys
        Comparator<K> valueComparator = new Comparator<K>() {

            // return comparison results of values of
            // two keys
            public int compare(K k1, K k2) {
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

    /**
     * Reads the leaderbaord text file and returns the result in a treeMap as it is.
     */
    public TreeMap<String, Integer> getLeaderBoard() {
        File input = new File("configs/leaderboard.txt");
        BufferedReader br = null;
        TreeMap<String, Integer> leaderboard = new TreeMap<String, Integer>();
        String currentLine;
        try {
            br = new BufferedReader(new FileReader(input));
            while ((currentLine = br.readLine()) != null) {
                if ("".equals(currentLine)) {
                    continue;
                }
                String[] arrOfLine = currentLine.split(":");
                int length = arrOfLine.length;
                if(length != 2){continue;}
                String username = arrOfLine[0];
                String[] arrOfScores = arrOfLine[1].split(",");

                int totalscore = 0;
                for (String score : arrOfScores) {
                    int levelscore = Integer.parseInt(score);
                    totalscore += levelscore;
                }
                leaderboard.put(username, totalscore);
            }
        } catch (IOException e) {
            logger.error("IOException in reading configs/leaderboard.txt");
        } finally {
            if (br != null){
                try {
                    br.close();
                } catch (IOException e){
                    logger.error("IOException in closing reader for configs/leaderboard.txt");
                }
            }
        }
        return leaderboard;
    }

}