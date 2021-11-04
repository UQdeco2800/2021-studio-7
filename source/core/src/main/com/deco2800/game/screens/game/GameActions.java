package com.deco2800.game.screens.game;

import com.deco2800.game.GdxGame;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.input.components.KeyboardPlayerInputComponent;
import com.deco2800.game.screens.RetroactiveActions;
import com.deco2800.game.screens.SettingsDisplay;

/**
 * This class listens to events relevant to the Main Game Screen and does something when one of the
 * events is triggered.
 */
public class GameActions extends RetroactiveActions {
    private final GameScreen screen;
    private ContextDisplay contextDisplay;
    private PauseDisplay pauseDisplay;
    private SettingsDisplay settingsDisplay;

    public GameActions(GameScreen screen) {
        this.screen = screen;
    }

    @Override
    public void create() {
        super.create();
        contextDisplay = entity.getComponent(ContextDisplay.class);
        pauseDisplay = entity.getComponent(PauseDisplay.class);
        settingsDisplay = entity.getComponent(SettingsDisplay.class);

        onEnterContextDisplay();
        entity.getEvents().addListener("exit_context", this::onExitContextDisplay);

        entity.getEvents().addListener("enter_pause", this::onEnterPauseDisplay);
        entity.getEvents().addListener("exit_pause", this::onExitPauseDisplay);

        entity.getEvents().addListener("enter_settings", this::onEnterSettingsDisplay);
        entity.getEvents().addListener("exit_settings", this::onExitSettingsDisplay);

        entity.getEvents().addListener("queue_main_game", this::onQueueMainGame);
        entity.getEvents().addListener("queue_main_menu", this::onQueueMainMenu);
        entity.getEvents().addListener("bed_interacted", this::onBedInteracted);
        entity.getEvents().addListener("player_caught", this::onPlayerCaught);
        entity.getEvents().addListener("timer_ended", this::onTimerEnded);
    }

    protected void onEnterContextDisplay() {
        screen.pause();
        contextDisplay.show();
    }

    protected void onExitContextDisplay() {
        screen.resume();
        contextDisplay.hide();
    }

    protected void onEnterPauseDisplay() {
        screen.pause();
        pauseDisplay.show();
    }

    protected void onExitPauseDisplay() {
        pauseDisplay.hide();
        screen.resume();
    }

    protected void onEnterSettingsDisplay() {
        pauseDisplay.hide();
        settingsDisplay.show();
    }

    protected void onExitSettingsDisplay() {
        settingsDisplay.hide();
        pauseDisplay.show();
    }

    protected void onQueueMainGame() {
        logger.debug("Queueing main game screen transition");
        ServiceLocator.getGame().setLevel(0);
        screen.queueNextScreen(GdxGame.ScreenType.MAIN_GAME);
    }

    public void onQueueMainMenu() {
        logger.debug("Queueing main menu screen transition");
        screen.queueNextScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    public void onBedInteracted() {
        logger.debug("Queueing win screen transition");
        screen.queueNextScreen(GdxGame.ScreenType.WIN_DEFAULT);
    }

    public void onPlayerCaught() {
        logger.debug("Queuing player caught lose screen transition");
        screen.queueNextScreen(GdxGame.ScreenType.LOSS_CAUGHT);
    }

    public void onTimerEnded() {
        logger.debug("Queueing timer ended lose screen transition");
        screen.getPlayer().getComponent(KeyboardPlayerInputComponent.class).setEnabled(false);
        screen.getHome().getFloor().createMum();
    }

    @Override
    public void loadAssets() {
        logger.debug("    Loading game actions assets");
        super.loadAssets();
    }

    @Override
    public void unloadAssets() {
        logger.debug("    Unloading game actions assets");
        super.unloadAssets();
    }
}
