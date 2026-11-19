package com.paralives.app.view.screens;

import com.paralives.app.presenter.AppPresenter;
import com.paralives.app.view.BaseScreen;
import com.paralives.app.view.overlay.PauseOverlay;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;

public class GameScreen extends BaseScreen {
    public GameScreen(AppPresenter presenter, JFrame owner) {
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
    }
}
