package com.deco2800.game.entities.components;

import com.deco2800.game.generic.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileWriter;
import java.io.IOException;

public class ScoreComponent extends Component {

  private static final Logger logger = LoggerFactory.getLogger(ScoreComponent.class);
  private int score;

  public ScoreComponent(int initialscore){
      this.score = initialscore;
  }

  @Override
  public void create() {
    super.create();
    entity.getEvents().addListener("change_score", this::changeScore);
    entity.getEvents().addListener("write_score", this::writeScoreToLeaderBoard);
  }

  public int getScore(){
    return this.score;
  }


  public void changeScore(int change){
      logger.info("Incrementing score by {}", change);
      this.score += change;
      entity.getEvents().trigger("update_score", score);
  }

  public void tickScore() {
       this.score--;
  }

  public void writeScoreToLeaderBoard(){
      StringBuilder sb = new StringBuilder();
      sb.append(score);
      sb.append(",");
      String s = sb.toString();
      logger.info("Trying to write to leaderboard...");

      try (FileWriter writer = new FileWriter("configs/leaderboard.txt", true)) {
          writer.write(s);
          logger.info("Sucessfully wrote to leaderboard...");
      } catch (IOException e) {
          logger.error("IOException in writing to leaderboad.");
      }
  }
}