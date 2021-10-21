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
    private String configFile = "configs/leaderboard.txt";
    private static TextButton button;
    private Map<String, Integer> sortedLeaderboard;

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
        Table leaderTable = new Table();
        logger.info("Trying to get leader board...");
        logger.info("Got leader board.");
        Set <Map.Entry<String,Integer>> set = sortedLeaderboard.entrySet();
        Iterator <Map.Entry<String,Integer>> i = set.iterator();
        String insert;
        int t = 0;
        while (i.hasNext()) {
            t++;
            if (t == 11){break;}
            leaderTable.row();
            Map.Entry<String,Integer> mp = i.next();
            String score = String.valueOf(mp.getValue());
            insert = mp.getKey() + ":" + score;
            Label label = new Label(insert, skin);
            leaderTable.add(label).padTop(15f);
        }
        return leaderTable;
    }

    private Table makeMenuBtns() {
        TextButton exitBtn1 = new TextButton("Exit", skin);
        setButtonState(exitBtn1);
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

    public static void setButtonState(TextButton buttonState) {
        button = buttonState;
    }

    private void exitMenu() {
        game.setScreen(ScreenType.MAIN_MENU);
    }

    public static void exitLB() {
        button.toggle();
    }

    private void sortLeaderBoard() {
        TreeMap<String, Integer> leaderboard = (TreeMap<String, Integer>) getLeaderBoard();
        sortedLeaderboard = valueSort(leaderboard);

        try (FileWriter clearer = new FileWriter(configFile); FileWriter writer = new FileWriter(configFile, true)) {
            clearer.write("");
            Set<Map.Entry<String, Integer>> set = sortedLeaderboard.entrySet();

            for (Map.Entry<String, Integer> stringIntegerEntry : set) {
                writer.write(stringIntegerEntry.getKey() + ":" + stringIntegerEntry.getValue() + "\n");
                logger.info("Sorted the leaderboard");
            }
        } catch (IOException e) {
            logger.error("IOException when reading leaderboard or attempting to close clearer for" +
                    " configs/leaderboard.txt or attempting to close writer for configs/leaderboard.txt");
        }
    }

    public static <K, V extends Comparable<V>> Map<K, V>
    valueSort(final Map<K, V> map) {
        // Static Method with return type Map and
        // extending comparator class which compares values
        // associated with two keys
        // return comparison results of values of
        // two keys
        Comparator<K> valueComparator = (k1, k2) -> {
            int comp = map.get(k2).compareTo(
                    map.get(k1));
            if (comp == 0)
                return 1;
            else
                return comp;
        };

        // SortedMap created using the comparator
        Map<K,V> sorted = new TreeMap<>(valueComparator);
        sorted.putAll(map);

        return sorted;
    }

    /**
     * Reads the leaderbaord text file and returns the result in a treeMap as it is.
     */
    public Map<String, Integer> getLeaderBoard() {

        TreeMap<String, Integer> leaderboard = new TreeMap<>();
        String currentLine;
        File input = new File(configFile);

        try (BufferedReader br = new BufferedReader(new FileReader(input))) {
            while ((currentLine = br.readLine()) != null) {
                String[] arrOfLine = currentLine.split(":");
                int length = arrOfLine.length;

                if ("".equals(currentLine) || length != 2) {
                    continue;
                }

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
        }
        return leaderboard;
    }

}