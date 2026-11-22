package com.paralives.app.view.screens;

import com.paralives.app.presenter.AppPresenter;
import com.paralives.app.view.BaseScreen;
import com.paralives.app.view.overlay.PauseOverlay;
import com.paralives.app.view.components.GamePanel;
import com.paralives.engine.input.InputHandler;
import com.paralives.engine.settings.AppSettings;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;

public class GameScreen extends BaseScreen {
    private final InputHandler inputHandler;
    private final GamePanel gamePanel;

    public GameScreen(AppPresenter presenter, JFrame owner, AppSettings settings) {
        setFocusable(true);
        inputHandler = new InputHandler(settings);
        JLabel status = new JLabel("ParaLives - Game", SwingConstants.LEFT);
        status.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        status.setBounds(40, 20, 400, 40);
        add(status);

        JButton pauseButton = createButton("Pause", 1040, 30, 200, 50);
        add(pauseButton);

        gamePanel = new GamePanel(
                inputHandler,
                presenter.getNetworkRole(),
                presenter.getNetworkHostAddress(),
                presenter.getNetworkPort());
        gamePanel.setBounds(100, 100, 1080, 520);
        add(gamePanel);

        pauseButton.addActionListener(event -> {
            gamePanel.pause();
            new PauseOverlay(
                    owner,
                    presenter,
                    gamePanel::resume,
                    gamePanel::pause).setVisible(true);
        });
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }
}
