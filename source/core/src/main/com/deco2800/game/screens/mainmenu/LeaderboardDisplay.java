package com.deco2800.game.screens.mainmenu;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.KeyboardMenuDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.TreeMap;
import java.io.*;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Leader board screen display.
 */
public class LeaderboardDisplay extends KeyboardMenuDisplay {
    private static final Logger logger = LoggerFactory.getLogger(LeaderboardDisplay.class);
    private static final String LEADERBOARD_FILE = "configs/leaderboard.txt";
    private static final String TITLE_TEXTURE = "images/ui/title/leaderboard.png";
    private Table table;
    private HorizontalGroup menuButtons;
    private Map<String, Integer> sortedLeaderboard;

    @Override
    public void create() {
        super.create();
        logger.info("Trying to sort leader board...");
        sortLeaderBoard();
        logger.info("Sorted leaderboard.");
        addActors();
    }

    @Override
    protected void addActors() {
        table = new Table();
        table.setFillParent(true);
        Image title = new Image(ServiceLocator.getResourceService().getAsset(TITLE_TEXTURE, Texture.class));
        table.add(title).expandX().top().padTop(20f);
        table.row().padTop(30f);
        table.add(createLeaderboard()).expandX().expandY();
        table.row();
        table.add(createMenuButtons()).fillX();
        stage.addActor(table);
    }

    @Override
    public void onMenuKeyPressed(int keyCode) {
        switch (keyCode) {
            case Keys.ENTER:
            case Keys.ESCAPE:
                ((TextButton) menuButtons.getChild(0)).toggle();
                break;
            default:
        }
    }

    private VerticalGroup createLeaderboard() {
        VerticalGroup leaderboard = new VerticalGroup();
        leaderboard.space(15f);

        Set <Map.Entry<String,Integer>> set = sortedLeaderboard.entrySet();
        Iterator <Map.Entry<String,Integer>> i = set.iterator();
        String insert;
        int t = 0;
        while (i.hasNext()) {
            t++;
            if (t == 11){break;}
            Map.Entry<String,Integer> mp = i.next();
            String score = String.valueOf(mp.getValue());
            insert = mp.getKey() + ":" + score;
            Label label = new Label(insert, skin);
            leaderboard.addActor(label);
        }
        return leaderboard;
    }

    private HorizontalGroup createMenuButtons() {
        TextButton exitBtn = new TextButton("Exit", skin);
        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Exit button clicked");
                        entity.getEvents().trigger("main_menu");
                    }
                });

        menuButtons = new HorizontalGroup();

        menuButtons.addActor(exitBtn);
        return menuButtons;
    }

    @Override
    public void update() {
        stage.act(ServiceLocator.getTimeSource().getDeltaTime());
    }

    public static List<String> getAssets() {
        return getAssets(".png");
    }

    public static List<String> getAssets(String extension) {
        List<String> assetsWithExtension = new ArrayList<>();
        if (extension.equals(".png")) {
            assetsWithExtension.add(TITLE_TEXTURE);
        }
        return assetsWithExtension;
    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }

    private void sortLeaderBoard() {
        TreeMap<String, Integer> leaderboard = (TreeMap<String, Integer>) getLeaderBoard();
        sortedLeaderboard = valueSort(leaderboard);

        try (FileWriter clearer = new FileWriter(LEADERBOARD_FILE); FileWriter writer = new FileWriter(LEADERBOARD_FILE, true)) {
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
        File input = new File(LEADERBOARD_FILE);

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