package com.deco2800.game.utils.math;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class RandomUtils {

  private static final Random seed = new Random();

  public static Random getSeed() {
    return seed;
  }

  public static Vector2 random(Vector2 start, Vector2 end) {
    return new Vector2(MathUtils.random(start.x, end.x), MathUtils.random(start.y, end.y));
  }

  public static GridPoint2 random(GridPoint2 start, GridPoint2 end) {
    return new GridPoint2(MathUtils.random(start.x, end.x), MathUtils.random(start.y, end.y));
  }

  private RandomUtils() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
