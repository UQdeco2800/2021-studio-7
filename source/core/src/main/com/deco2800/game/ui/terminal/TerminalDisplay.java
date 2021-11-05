package com.deco2800.game.ui.terminal;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.deco2800.game.rendering.components.RenderPriority;
import com.deco2800.game.ui.components.UIComponent;

/**
 * A ui component for displaying the debug terminal. The terminal is positioned at the bottom of the
 * screen.
 */
public class TerminalDisplay extends UIComponent {
    private Terminal terminal;
    private Label label;

    public TerminalDisplay() {
        renderPriority = RenderPriority.FRONT.ordinal();
    }

    @Override
    public void create() {
        super.create();
        terminal = entity.getComponent(Terminal.class);

        String message = "";
        label = new Label("> " + message, skin);
        label.setPosition(5f, 0);
        table.add(label);
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (terminal.isOpen()) {
            label.setVisible(true);
            String message = terminal.getEnteredMessage();
            label.setText("> " + message);
        } else {
            label.setVisible(false);
        }
    }
}
