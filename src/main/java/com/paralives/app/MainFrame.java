package com.paralives.app;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;

public class MainFrame extends JFrame {
    public static final int FRAME_WIDTH = 1280;
    public static final int FRAME_HEIGHT = 720;

    private static final String MAIN_MENU = "MAIN_MENU";
    private static final String PLAY_SCREEN = "PLAY_SCREEN";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel screenPanel = new JPanel(cardLayout);

    public MainFrame() {
        super("ParaLives");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setMaximumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setLocationRelativeTo(null);

        screenPanel.add(createMainMenu(), MAIN_MENU);
        screenPanel.add(createPlayScreen(), PLAY_SCREEN);
        add(screenPanel);

        showScreen(MAIN_MENU);
    }

    public void showScreen(String screenName) {
        cardLayout.show(screenPanel, screenName);
    }

    private JPanel createMainMenu() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBorder(BorderFactory.createEmptyBorder());

        JLabel title = new JLabel("ParaLives", SwingConstants.CENTER);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 48));
        title.setBounds(390, 150, 500, 80);
        panel.add(title);

        JButton playButton = new JButton("Play");
        playButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        playButton.setBounds(515, 330, 250, 60);
        playButton.addActionListener(event -> showScreen(PLAY_SCREEN));
        panel.add(playButton);

        return panel;
    }

    private JPanel createPlayScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel title = new JLabel("Game Screen", SwingConstants.CENTER);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 36));
        title.setBounds(390, 180, 500, 60);
        panel.add(title);

        JButton backButton = new JButton("Back to Main Menu");
        backButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        backButton.setBounds(515, 420, 250, 60);
        backButton.addActionListener(event -> showScreen(MAIN_MENU));
        panel.add(backButton);

        return panel;
    }
}
