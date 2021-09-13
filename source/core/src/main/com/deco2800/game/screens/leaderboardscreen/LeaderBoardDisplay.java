package com.deco2800.game.screens.leaderboardscreen;

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
        addActors();
    }
    private void addActors() {
        Label title = new Label("LeaderBoard", skin, "title");

        Table leaderboardtable = makeLeaderBoardTable();
        Table menuBtns = makeMenuBtns();

        rootTable = new Table();
        rootTable.setFillParent(true);

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

}