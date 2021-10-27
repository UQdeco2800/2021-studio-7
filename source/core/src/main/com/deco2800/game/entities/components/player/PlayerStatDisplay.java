package com.deco2800.game.entities.components.player;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.entities.components.CombatStatsComponent;
import com.deco2800.game.entities.components.ScoreComponent;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.RetroactiveWidget;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class PlayerStatDisplay extends RetroactiveWidget {
    private Label staminaLabel;
    private PlayerStaminaBar playerStaminaBar;
    private Label scoreLabel;

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("update_stamina", this::updatePlayerStaminaUI);
        entity.getEvents().addListener("update_score", this::updatePlayerScoreUI);
    }

    @Override
    protected void addActors() {
        table = new Table();
        table.top().left().pad(30f).setFillParent(true);

        //display level
        CharSequence levelText = String.format("Level %d", ServiceLocator.getGame().getLevel());
        String largeStyle = "large";
        Label levelLabel = new Label(levelText, skin, largeStyle);

        // Score display
        int score = entity.getComponent(ScoreComponent.class).getScore();
        CharSequence scoreText = String.format("Score: %d", score);
        scoreLabel = new Label(scoreText, skin, largeStyle);

        // stamina text
        double stamina = entity.getComponent(CombatStatsComponent.class).getStamina();
        CharSequence staminaText = String.format("Stamina: %.0f", stamina / 5);
        staminaLabel = new Label(staminaText, skin, largeStyle);

        // stamina bar
        playerStaminaBar = new PlayerStaminaBar(100, 100);
        playerStaminaBar.setValue((float) stamina);

        table.add(levelLabel).left();
        table.row();
        table.add(scoreLabel).left();
        table.row();
        table.add(new Label("", skin, largeStyle));
        table.row();
        table.add(playerStaminaBar).size(190, 50).left();
        table.row();
        table.add(staminaLabel).left();
    }

    /**
     * Updates the player's stamina on the ui.
     *
     * @param stamina player stamina
     */
    public void updatePlayerStaminaUI(int stamina) {
        CharSequence text = String.format("Stamina: %d", stamina / 5);
        staminaLabel.setText(text);

        // update stamina bar
        playerStaminaBar.setValue(stamina);

    }

    public void updatePlayerScoreUI(int score) {
        StringBuilder sb = new StringBuilder();
        sb.append(score);
        String s = sb.toString();
        CharSequence text = "Score: " + s;
        scoreLabel.setText(text);
    }

    @Override
    public void loadAssets() {
        logger.debug("    Loading stats widget assets");
        super.loadAssets();
    }

    @Override
    public void unloadAssets() {
        logger.debug("    Unloading stats widget assets");
        super.unloadAssets();
    }
}
