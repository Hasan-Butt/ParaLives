package com.paralives.app.view.screens;

import com.paralives.app.presenter.AppPresenter;
import com.paralives.app.view.BaseScreen;
import com.paralives.app.view.overlay.PauseOverlay;
import com.paralives.engine.input.InputHandler;
import com.paralives.engine.settings.AppSettings;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.HierarchyEvent;

public class GameScreen extends BaseScreen {
    private final InputHandler inputHandler;

    public GameScreen(AppPresenter presenter, JFrame owner, AppSettings settings) {
        setFocusable(true);
        inputHandler = new InputHandler(settings);
        JLabel status = new JLabel("ParaLives - Game", SwingConstants.LEFT);
        status.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        status.setBounds(40, 20, 400, 40);
        add(status);

        JButton pauseButton = createButton("Pause", 1040, 30, 200, 50);
        pauseButton.addActionListener(event -> new PauseOverlay(owner, presenter).setVisible(true));
        add(pauseButton);

        JLabel gameArea = new JLabel("Game view", SwingConstants.CENTER);
        gameArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 32));
        gameArea.setBounds(100, 150, 1080, 400);
        add(gameArea);

        addHierarchyListener(event -> {
            if ((event.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) == 0) {
                return;
            }

            if (isShowing()) {
                inputHandler.attach(this);
                requestFocusInWindow();
            } else {
                inputHandler.detach(this);
            }
        });
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }
}
