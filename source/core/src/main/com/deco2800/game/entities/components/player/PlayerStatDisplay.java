package com.deco2800.game.entities.components.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.entities.components.CombatStatsComponent;
import com.deco2800.game.entities.components.ScoreComponent;
import com.deco2800.game.entities.components.player.PlayerStaminaBar;
import com.deco2800.game.screens.maingame.MainGameScreen;
import com.deco2800.game.ui.components.UIComponent;


/**
 * A ui component for displaying player stats, e.g. health.
 */
public class PlayerStatDisplay extends UIComponent {
  Table table;

  private Label staminaLabel;
  private PlayerStaminaBar playerStaminaBar;
  private Label scoreLabel;

  /**
   * Creates reusable ui styles and adds actors to the stage.
   */
  @Override
  public void create() {
    super.create();
    addActors();

    entity.getEvents().addListener("update_stamina", this::updatePlayerStaminaUI);
    entity.getEvents().addListener("update_score", this::updatePlayerScoreUI);
  }

  /**
   * Creates actors and positions them on the stage using a table.
   * @see Table for positioning options
   */
  private void addActors() {
    table = new Table();
    table.top().left();
    table.setFillParent(true);
    table.padTop(10f).padLeft(10f);

    //display level
    CharSequence levelText = String.format("Level %d", MainGameScreen.getLevel());
    String largeStyle = "large";
    Label levelLabel = new Label(levelText, skin, largeStyle);

    // Score display
    int score = entity.getComponent(ScoreComponent.class).getScore();
    CharSequence scoreText = String.format("Score: %d", score);
    scoreLabel = new Label(scoreText, skin, largeStyle);

    // stamina text
    double stamina = entity.getComponent(CombatStatsComponent.class).getStamina();
    CharSequence staminaText = String.format("Stamina: %.0f", stamina/5);
    staminaLabel = new Label(staminaText, skin, largeStyle);

    // stamina bar
    playerStaminaBar = new PlayerStaminaBar(100, 100);
    playerStaminaBar.setValue((float) stamina);



    table.add(levelLabel).left();
    table.row();
    table.add(scoreLabel).left();
    table.row();
    table.row();
    table.row();
    table.add(playerStaminaBar).size(190,50).left();
    table.row();
    table.add(staminaLabel).left();

    stage.addActor(table);
  }

  @Override
  public void draw(SpriteBatch batch)  {
    // draw is handled by the stage
  }

  /**
   * Updates the player's stamina on the ui.
   * @param stamina player stamina
   */
  public void updatePlayerStaminaUI (int stamina) {
    CharSequence text = String.format("Stamina: %d", stamina/5);
    staminaLabel.setText(text);

    // update stamina bar
    playerStaminaBar.setValue(stamina);

  }

  public void updatePlayerScoreUI(int score){
    StringBuilder sb = new StringBuilder();
    sb.append(score);
    String s = sb.toString();
    CharSequence text = "Score: " + s;
    scoreLabel.setText(text);
  }

  @Override
  public void dispose() {
    super.dispose();
    staminaLabel.remove();
    playerStaminaBar.remove();
  }
}
