package com.deco2800.game.screens;

import com.deco2800.game.generic.Loadable;
import com.deco2800.game.ui.components.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class RetroactiveWidget extends UIComponent implements Loadable {
    protected static final Logger logger = LoggerFactory.getLogger(RetroactiveWidget.class);

    public RetroactiveWidget() {
        super();
        Z_INDEX = 5f;
    }

    @Override
    public void loadAssets() {
    }

    @Override
    public void unloadAssets() {
    }
}
