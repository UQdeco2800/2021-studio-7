package com.deco2800.game.utils.math;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Contains additional utility constants and functions for common GridPoint2 operations.
 */
public class GridPoint2Utils {
  public static final GridPoint2 ZERO = new GridPoint2(0, 0);

  public static GridPoint2 read(JsonValue jsonData) {
    int[] points = jsonData.asIntArray();
    return new GridPoint2(points[0], points[1]);
  }

  private GridPoint2Utils() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
