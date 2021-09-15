package com.deco2800.game.entities.components.player;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.entities.components.CombatStatsComponent;
import com.deco2800.game.entities.components.ScoreComponent;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.ui.components.UIComponent;

import java.util.concurrent.TimeUnit;


/**
 * A ui component for displaying player stats, e.g. health.
 */
public class PlayerStatsDisplay extends UIComponent {
  Table table;

  private Label staminaLabel;
  private PlayerStaminaBar playerStaminaBar;
  private Image heartImage;
  private Label healthLabel;
  private Label staminaLabel;
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
    table.padTop(45f).padLeft(10f);


    // stamina text
    double stamina = entity.getComponent(CombatStatsComponent.class).getStamina();
    CharSequence staminaText = String.format("Stamina: %.0f", stamina/5);
    staminaLabel = new Label(staminaText, skin, "large");

    // stamina bar
    playerStaminaBar = new PlayerStaminaBar(100, 100);
    playerStaminaBar.setValue((float) stamina);

    int score = entity.getComponent(ScoreComponent.class).getScore();
    CharSequence scoreText = String.format("Score: %d", score);
    scoreLabel = new Label(scoreText, skin, "large");


    table.add(staminaLabel).left();
    table.row();
    table.add(scoreLabel).left();
    table.row();
    table.add(playerStaminaBar).size(190,50).left();

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
    CharSequence text = String.format("Score: "+ s);
    scoreLabel.setText(text);
  }

  @Override
  public void dispose() {
    super.dispose();
    staminaLabel.remove();
    playerStaminaBar.remove();
  }
}
