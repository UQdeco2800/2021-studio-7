package com.deco2800.game.entities.components.player;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.generic.Component;

public class CameraComponent extends Component {
  private static final float HEIGHT = 0f;
  private final Camera camera;
  private Entity target;
  private Vector2 lastPosition;

  public CameraComponent() {
    this(new OrthographicCamera());
  }

  public CameraComponent(Camera camera) {
    this.camera = camera;
    lastPosition = Vector2.Zero.cpy();
  }

  @Override
  public void create() {
    super.create();
    target = entity;
    camera.position.set(entity.getPosition(), 0f);
    ((OrthographicCamera) camera).zoom += HEIGHT;
  }

  @Override
  public void update() {
    Vector3 targetPos = new Vector3(target.getPosition(), 0f);
    Vector3 cameraPos = camera.position;
    if (!lastPosition.epsilonEquals(target.getPosition())) {
      final float speed = 0.1f, ispeed = 1.0f - speed;
      cameraPos.scl(ispeed);
      targetPos.scl(speed);
      cameraPos.add(targetPos);

      camera.position.set(cameraPos);
      lastPosition = new Vector2(cameraPos.x, cameraPos.y);
      camera.update();
    }
  }

  public Matrix4 getProjectionMatrix() {
    return camera.combined;
  }

  public Camera getCamera() {
    return camera;
  }

  public Vector2 getLastPosition() {
    return lastPosition;
  }

  public void setTarget(Entity target) {
    this.target = target;
  }

  public void resize(int screenWidth, int screenHeight, float gameWidth) {
    float ratio = (float) screenHeight / screenWidth;
    camera.viewportWidth = gameWidth;
    camera.viewportHeight = gameWidth * ratio;
    camera.update();
  }
}
