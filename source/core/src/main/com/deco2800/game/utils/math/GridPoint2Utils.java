package com.deco2800.game.utils.math;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Contains additional utility constants and functions for common GridPoint2 operations.
 */
public class GridPoint2Utils {
  public static final GridPoint2 ZERO = new GridPoint2(0, 0);

  public static GridPoint2 read(JsonValue jsonData) {
    jsonData = jsonData.child();
    assert jsonData.child() == null;
    GridPoint2 gridPoint2 = new GridPoint2();
    gridPoint2.x = IntUtils.strDigitsToInt(jsonData.name());
    jsonData = jsonData.next();
    assert jsonData.child() == null;
    gridPoint2.y = IntUtils.strDigitsToInt(jsonData.name());
    assert jsonData.next() == null;
    return gridPoint2;
  }

  private GridPoint2Utils() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
