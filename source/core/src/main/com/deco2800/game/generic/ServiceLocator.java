package com.deco2800.game.generic;

import com.badlogic.gdx.ScreenAdapter;
import com.deco2800.game.GdxGame;
import com.deco2800.game.chores.ChoreController;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.input.InputService;
import com.deco2800.game.maps.Home;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simplified implementation of the Service Locator pattern:
 * https://martinfowler.com/articles/injection.html#UsingAServiceLocator
 *
 * <p>Allows global access to a few core game services. Warning: global access is a trap and should
 * be used extremely sparingly. Read the README for details.
 */
public class ServiceLocator {
  private static final Logger logger = LoggerFactory.getLogger(ServiceLocator.class);
  private static GdxGame game;
  private static Home home;
  private static EntityService entityService;
  private static RenderService renderService;
  private static PhysicsService physicsService;
  private static GameTime timeSource;
  private static InputService inputService;
  private static ResourceService resourceService;
  private static ChoreController choreController;

  public static GdxGame getGame() {
    return game;
  }

  public static <T extends ScreenAdapter> T getScreen(Class<T> clazz) {
    return clazz.cast(game.getScreen());
  }

  public static Home getHome() {
    return home;
  }

  public static EntityService getEntityService() {
    return entityService;
  }

  public static RenderService getRenderService() {
    return renderService;
  }

  public static PhysicsService getPhysicsService() {
    return physicsService;
  }

  public static GameTime getTimeSource() {
    return timeSource;
  }

  public static InputService getInputService() {
    return inputService;
  }

  public static ResourceService getResourceService() {
    return resourceService;
  }

  public static ChoreController getChoreController() {
    return choreController;
  }

  public static void registerGame(GdxGame source) {
    logger.debug("Registering GdxGame {}", source);
    game = source;
  }

  public static void registerHome(Home source) {
    logger.debug("Registering Home {}", source);
    home = source;
  }

  public static void registerEntityService(EntityService service) {
    logger.debug("Registering entity service {}", service);
    entityService = service;
  }

  public static void registerRenderService(RenderService service) {
    logger.debug("Registering render service {}", service);
    renderService = service;
  }

  public static void registerPhysicsService(PhysicsService service) {
    logger.debug("Registering physics service {}", service);
    physicsService = service;
  }

  public static void registerTimeSource(GameTime source) {
    logger.debug("Registering time source {}", source);
    timeSource = source;
  }

  public static void registerInputService(InputService source) {
    logger.debug("Registering input service {}", source);
    inputService = source;
  }

  public static void registerResourceService(ResourceService source) {
    logger.debug("Registering resource service {}", source);
    resourceService = source;
  }

  public static void registerChoreController(ChoreController source) {
    logger.debug("Registering chore controller {}", source);
    choreController = source;
  }

  public static void clear() {
    entityService = null;
    renderService = null;
    physicsService = null;
    timeSource = null;
    inputService = null;
    resourceService = null;
    choreController = null;
  }

  private ServiceLocator() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
