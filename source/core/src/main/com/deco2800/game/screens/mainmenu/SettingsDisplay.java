package com.deco2800.game.screens.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.files.UserSettings;
import com.deco2800.game.files.UserSettings.DisplaySettings;
import com.deco2800.game.generic.ServiceLocator;
import com.deco2800.game.screens.KeyboardMenuDisplay;
import com.deco2800.game.utils.StringDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Settings menu display and logic. If you bork the settings, they can be changed manually in
 * DECO2800Game/settings.json under your home directory (This is C:/users/[username] on Windows).
 */
public class SettingsDisplay extends KeyboardMenuDisplay {
  private static final Logger logger = LoggerFactory.getLogger(SettingsDisplay.class);

  private Table rootTable;
  private TextField fpsText;
  private CheckBox fullScreenCheck;
  private CheckBox vsyncCheck;
  private Slider uiScaleSlider;
  private SelectBox<StringDecorator<DisplayMode>> displayModeSelect;
  private HorizontalGroup settingsButtons;
  private int settingsButtonIndex = 0;

  @Override
  public void create() {
    super.create();
    addActors();
  }

  @Override
  protected void addActors() {
    Label title = new Label("Settings", skin, "title");
    Table settingsTable = makeSettingsTable();
    settingsButtons = createSettingsButtons();

    rootTable = new Table();
    rootTable.setFillParent(true);

    rootTable.add(title).expandX().top().padTop(20f);

    rootTable.row().padTop(30f);
    rootTable.add(settingsTable).expandX().expandY();

    rootTable.row();
    rootTable.add(settingsButtons).fillX();

    stage.addActor(rootTable);
  }

  @Override
  public void onMenuKeyPressed(int keyCode) {
    InputEvent hover = new InputEvent();
    hover.setType(InputEvent.Type.enter);
    hover.setPointer(-1);

    switch (keyCode) {
      case Keys.A:
        //SettingsScreen.playButtonSound();
        settingsButtonIndex = 0;
        settingsButtons.getChild(settingsButtonIndex).fire(hover);
        break;
      case Keys.D:
        //SettingsScreen.playButtonSound();
        settingsButtonIndex = 1;
        settingsButtons.getChild(settingsButtonIndex).fire(hover);
        break;
      case Keys.ENTER:
        //SettingsScreen.playButtonSound();
        ((TextButton) settingsButtons.getChild(settingsButtonIndex)).toggle();
        break;
      case Keys.ESCAPE:
        //SettingsScreen.playButtonSound();
        ((TextButton) settingsButtons.getChild(0)).toggle();
        break;
      default:
    }
  }

  private Table makeSettingsTable() {
    // Get current values
    UserSettings.Settings settings = UserSettings.get();

    // Create components
    Label fpsLabel = new Label("FPS Cap:", skin);
    fpsText = new TextField(Integer.toString(settings.getFps()), skin);

    Label fullScreenLabel = new Label("Fullscreen:", skin);
    fullScreenCheck = new CheckBox("", skin);
    fullScreenCheck.setChecked(settings.isFullscreen());

    Label vsyncLabel = new Label("VSync:", skin);
    vsyncCheck = new CheckBox("", skin);
    vsyncCheck.setChecked(settings.isVsync());

    Label uiScaleLabel = new Label("ui Scale (Unused):", skin);
    uiScaleSlider = new Slider(0.2f, 2f, 0.1f, false, skin);
    uiScaleSlider.setValue(settings.getUiScale());
    Label uiScaleValue = new Label(String.format("%.2fx", settings.getUiScale()), skin);

    Label displayModeLabel = new Label("Resolution:", skin);
    displayModeSelect = new SelectBox<>(skin);
    Monitor selectedMonitor = Gdx.graphics.getMonitor();
    displayModeSelect.setItems(getDisplayModes(selectedMonitor));
    displayModeSelect.setSelected(getActiveMode(displayModeSelect.getItems()));

    // Position Components on table
    Table table = new Table();

    table.add(fpsLabel).right().padRight(15f);
    table.add(fpsText).width(100).left();

    table.row().padTop(10f);
    table.add(fullScreenLabel).right().padRight(15f);
    table.add(fullScreenCheck).left();

    table.row().padTop(10f);
    table.add(vsyncLabel).right().padRight(15f);
    table.add(vsyncCheck).left();

    table.row().padTop(10f);
    Table uiScaleTable = new Table();
    uiScaleTable.add(uiScaleSlider).width(100).left();
    uiScaleTable.add(uiScaleValue).left().padLeft(5f).expandX();

    table.add(uiScaleLabel).right().padRight(15f);
    table.add(uiScaleTable).left();

    table.row().padTop(10f);
    table.add(displayModeLabel).right().padRight(15f);
    table.add(displayModeSelect).left();

    // Events on inputs
    uiScaleSlider.addListener(
            (Event event) -> {
              float value = uiScaleSlider.getValue();
              uiScaleValue.setText(String.format("%.2fx", value));
              return true;
            });

    return table;
  }

  private HorizontalGroup createSettingsButtons() {
    HorizontalGroup settingsContainer = new HorizontalGroup();

    TextButton exitBtn = new TextButton("Back", skin);
    exitBtn.addListener(
            new ChangeListener() {
              @Override
              public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Exit button clicked");
                entity.getEvents().trigger("main_menu");
              }
            });
    settingsContainer.addActor(exitBtn);

    TextButton applyBtn = new TextButton("Apply", skin);
    applyBtn.addListener(
            new ChangeListener() {
              @Override
              public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Apply button clicked");
                applyChanges();
              }
            });
    settingsContainer.addActor(applyBtn);

    return settingsContainer;
  }

  private StringDecorator<DisplayMode> getActiveMode(Array<StringDecorator<DisplayMode>> modes) {
    DisplayMode active = Gdx.graphics.getDisplayMode();

    for (StringDecorator<DisplayMode> stringMode : new Array.ArrayIterator<>(modes)) {
      DisplayMode mode = stringMode.getObject();
      if (active.width == mode.width
              && active.height == mode.height
              && active.refreshRate == mode.refreshRate) {
        return stringMode;
      }
    }
    return null;
  }

  private Array<StringDecorator<DisplayMode>> getDisplayModes(Monitor monitor) {
    DisplayMode[] displayModes = Gdx.graphics.getDisplayModes(monitor);
    Array<StringDecorator<DisplayMode>> arr = new Array<>();

    for (DisplayMode displayMode : displayModes) {
      arr.add(new StringDecorator<>(displayMode, this::prettyPrint));
    }

    return arr;
  }

  private String prettyPrint(DisplayMode displayMode) {
    return displayMode.width + "x" + displayMode.height + ", " + displayMode.refreshRate + "hz";
  }

  private void applyChanges() {
    UserSettings.Settings settings = UserSettings.get();

    Integer fpsVal = parseOrNull(fpsText.getText());
    if (fpsVal != null) {
      settings.setFps(fpsVal);
    }
    settings.setFullscreen(fullScreenCheck.isChecked());
    settings.setUiScale(uiScaleSlider.getValue());
    settings.setDisplayMode(new DisplaySettings(displayModeSelect.getSelected().getObject()));
    settings.setVsync(vsyncCheck.isChecked());

    UserSettings.set(settings, true);
  }

  private Integer parseOrNull(String num) {
    try {
      return Integer.parseInt(num, 10);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  @Override
  public void update() {
    stage.act(ServiceLocator.getTimeSource().getDeltaTime());
  }

  public static List<String> getAssets() {
    return new ArrayList<>();
  }

  public static List<String> getAssets(String extension) {
    // Ignore extension
    return new ArrayList<>();
  }

  @Override
  public void dispose() {
    rootTable.clear();
    super.dispose();
  }
}
