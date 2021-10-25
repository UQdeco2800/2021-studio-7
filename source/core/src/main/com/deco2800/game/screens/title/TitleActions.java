package com.deco2800.game.screens.title;

import com.deco2800.game.GdxGame;
import com.deco2800.game.generic.Component;
import com.deco2800.game.generic.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Title Screen and does something when one of the
 * events is triggered.
 */
public class TitleActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(TitleActions.class);
    private final TitleScreen screen;

    public TitleActions(TitleScreen screen) {
        this.screen = screen;
    }
    
    @Override
    public void create() {
    }
}
