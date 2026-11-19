package com.paralives.app.view.overlay;

import com.paralives.app.presenter.AppPresenter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Font;

public class PauseOverlay extends JDialog {
    public PauseOverlay(JFrame owner, AppPresenter presenter) {
        super(owner, "Game Paused", true);
        setResizable(false);
        setSize(420, 360);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        JLabel title = new JLabel("Paused", SwingConstants.CENTER);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 32));
        title.setBounds(60, 30, 300, 50);
        panel.add(title);

        JButton resumeButton = createButton("Resume", 110, 110, 200, 50);
        resumeButton.addActionListener(event -> dispose());
        panel.add(resumeButton);

        JButton settingsButton = createButton("Settings", 110, 170, 200, 50);
        settingsButton.addActionListener(event -> {
            dispose();
            presenter.openSettings();
        });
        panel.add(settingsButton);

        JButton quitButton = createButton("Quit to Main Menu", 110, 230, 200, 50);
        quitButton.addActionListener(event -> {
            dispose();
            presenter.openMainMenu();
        });
        panel.add(quitButton);

        setContentPane(panel);
    }

    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        button.setBounds(x, y, width, height);
        return button;
    }
}
