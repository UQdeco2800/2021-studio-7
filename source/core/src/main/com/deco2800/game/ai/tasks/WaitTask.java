package com.deco2800.game.ai.tasks;

import com.deco2800.game.generic.GameTime;
import com.deco2800.game.generic.ServiceLocator;

/**
 * Task that does nothing other than waiting for a given time. Status is Finished
 * after the time has passed.
 */
public class WaitTask extends DefaultTask {
  private final GameTime timeSource;
  private final float duration;
  private long endTime;

  /**
   * @param duration How long to wait for, in seconds.
   */
  public WaitTask(float duration) {
    timeSource = ServiceLocator.getTimeSource();
    this.duration = duration;
  }

  /**
   * Start waiting from now until duration has passed.
   */
  @Override
  public void start() {
    super.start();
    endTime = timeSource.getTime() + (int)(duration * 1000);
  }

  @Override
  public void update() {
    if (timeSource.getTime() >= endTime) {
      status = Status.FINISHED;
    }
  }
}
