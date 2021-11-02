package com.deco2800.game.screens.menu;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.RetroactiveDisplay;

import java.io.*;
import java.util.*;

/**
 * Leader board screen display.
 */
public class LeaderboardDisplay extends RetroactiveDisplay {
    private static final String LEADERBOARD_FILE = "configs/leaderboard.txt";
    private static final String TITLE_TEXTURE = "images/ui/title/leaderboard.png";
    private Map<String, Integer> sortedLeaderboard;

    @Override
    public void create() {
        super.create();

        sortLeaderBoard();

        Image title = new Image(ServiceLocator.getResourceService().getAsset(TITLE_TEXTURE, Texture.class));

        VerticalGroup container = new VerticalGroup();
        container.space(25f);
        container.addActor(title);
        container.addActor(createLeaderboard());
        container.addActor(createButtons());

        table.add(container);
    }

    private VerticalGroup createLeaderboard() {
        VerticalGroup leaderboard = new VerticalGroup();
        leaderboard.space(15f);

        Set<Map.Entry<String, Integer>> set = sortedLeaderboard.entrySet();
        Iterator<Map.Entry<String, Integer>> i = set.iterator();
        String insert;
        int t = 0;
        while (i.hasNext() && t < 11) {
            t++;
            Map.Entry<String, Integer> mp = i.next();
            String score = String.valueOf(mp.getValue());
            insert = mp.getKey() + ":" + score;
            Label label = new Label(insert, skin);
            leaderboard.addActor(label);
        }
        return leaderboard;
    }

    @Override
    protected Table createButtons() {
        buttonTable = new Table();
        traverseBackwards = new int[]{};
        traverseForwards = new int[]{};
        enter = new int[]{Keys.ENTER, Keys.ESCAPE};

        TextButton exitBtn = new TextButton("Exit", skin);
        exitBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    logger.debug("Exit button clicked");
                    entity.getEvents().trigger("exit_leaderboard");
                }
            });
        buttonTable.add(exitBtn);

        triggerHighlight();

        return buttonTable;
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
        Map<K, V> sorted = new TreeMap<>(valueComparator);
        sorted.putAll(map);

        return sorted;
    }

    /**
     * Reads the leaderboard text file and returns the result in a treeMap as it is.
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

    @Override
    public void loadAssets() {
        logger.debug("   Loading leaderboard display assets");
        super.loadAssets();
        ServiceLocator.getResourceService().loadAsset(TITLE_TEXTURE, Texture.class);
    }

    @Override
    public void unloadAssets() {
        logger.debug("   Unloading leaderboard display assets");
        super.unloadAssets();
        ServiceLocator.getResourceService().unloadAsset(TITLE_TEXTURE);
    }
}