package com.deco2800.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.files.UserSettings;
import com.deco2800.game.files.UserSettings.DisplaySettings;
import com.deco2800.game.utils.StringDecorator;

/**
 * Settings menu display and logic. If you bork the settings, they can be changed manually in
 * DECO2800Game/settings.json under your home directory (This is C:/users/[username] on Windows).
 */
public class SettingsDisplay extends RetroactiveDisplay {
    private TextField fpsText;
    private CheckBox fullScreenCheck;
    private CheckBox vsyncCheck;
    private Slider uiScaleSlider;
    private SelectBox<StringDecorator<DisplayMode>> displayModeSelect;

    @Override
    public void create() {
        super.create();
        table.setDebug(true);

        Label title = new Label("Settings", skin, "title");
        Table settingsTable = createSettingsTable();

        table.add(title).expandX().top().padTop(20f);

        table.row().padTop(30f);
        table.add(settingsTable).expandX().expandY();

        table.row();
        table.add(createButtons()).width(stage.getWidth() * 0.35f).padBottom(20f);
    }

    private Table createSettingsTable() {
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

    @Override
    protected Table createButtons() {
        buttonTable = new Table();
        traverseBackwards = new int[]{Keys.LEFT, Keys.A};
        traverseForwards = new int[]{Keys.RIGHT, Keys.D};
        enter = new int[]{Keys.ENTER};

        TextButton exitBtn = new TextButton("Back", skin);
        exitBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    logger.debug("Exit button clicked");
                    entity.getEvents().trigger("exit_settings");
                }
            });
        buttonTable.add(exitBtn).left().padRight(20f).growX();

        TextButton applyBtn = new TextButton("Apply", skin);
        applyBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    logger.debug("Apply button clicked");
                    applyChanges();
                }
            });
        buttonTable.add(applyBtn).right().growX();

        triggerHighlight();

        return buttonTable;
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

    @Override
    protected void keyUp(int keycode) {
        super.keyUp(keycode);
        if (keycode == Keys.ESCAPE) {
            entity.getEvents().trigger("exit_settings");
        }
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
    public void loadAssets() {
        logger.debug("   Loading settings display assets");
        super.loadAssets();
    }

    @Override
    public void unloadAssets() {
        logger.debug("    Unloading settings display assets");
        super.unloadAssets();
    }
}
