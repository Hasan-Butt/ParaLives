package com.paralives.app;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;

public class MainFrame extends JFrame {
    public static final int FRAME_WIDTH = 1280;
    public static final int FRAME_HEIGHT = 720;

    private static final String LOGIN = "LOGIN";
    private static final String REGISTER = "REGISTER";
    private static final String MAIN_MENU = "MAIN_MENU";
    private static final String SAVE_SLOTS = "SAVE_SLOTS";
    private static final String SETTINGS = "SETTINGS";
    private static final String GAME = "GAME";
    private static final String LEADERBOARD = "LEADERBOARD";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel screenPanel = new JPanel(cardLayout);
    private JSlider masterVolumeSlider;
    private JComboBox<String> resolutionSelector;

    public MainFrame() {
        super("ParaLives");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setLocationRelativeTo(null);

        screenPanel.add(createLoginScreen(), LOGIN);
        screenPanel.add(createRegisterScreen(), REGISTER);
        screenPanel.add(createMainMenuScreen(), MAIN_MENU);
        screenPanel.add(createSaveSlotScreen(), SAVE_SLOTS);
        screenPanel.add(createSettingsScreen(), SETTINGS);
        screenPanel.add(createGameScreen(), GAME);
        screenPanel.add(createLeaderboardScreen(), LEADERBOARD);
        add(screenPanel);

        showScreen(LOGIN);
    }

    public void showScreen(String screenName) {
        cardLayout.show(screenPanel, screenName);
    }

    private JPanel createLoginScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        addCenteredTitle(panel, "Login", 110, 48);
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(440, 260, 400, 30);
        panel.add(usernameLabel);
        JTextField usernameField = new JTextField();
        usernameField.setBounds(440, 290, 400, 40);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(440, 340, 400, 30);
        panel.add(passwordLabel);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(440, 370, 400, 40);
        panel.add(passwordField);

        JButton loginButton = createButton("Login", 440, 440, 190, 50);
        loginButton.addActionListener(event -> showScreen(MAIN_MENU));
        panel.add(loginButton);

        JButton registerButton = createButton("Create Account", 650, 440, 190, 50);
        registerButton.addActionListener(event -> showScreen(REGISTER));
        panel.add(registerButton);

        return panel;
    }

    private JPanel createRegisterScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        addCenteredTitle(panel, "Register", 80, 48);
        panel.add(createLabeledField("Username", 440, 220));
        panel.add(createLabeledField("Email", 440, 300));
        panel.add(createPasswordField("Password", 440, 380));

        JButton registerButton = createButton("Create Account", 440, 460, 190, 50);
        registerButton.addActionListener(event -> showScreen(LOGIN));
        panel.add(registerButton);

        JButton backButton = createButton("Back", 650, 460, 190, 50);
        backButton.addActionListener(event -> showScreen(LOGIN));
        panel.add(backButton);

        return panel;
    }

    private JPanel createMainMenuScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        addCenteredTitle(panel, "ParaLives", 110, 48);

        JButton playButton = createButton("Play", 515, 280, 250, 60);
        playButton.addActionListener(event -> showScreen(SAVE_SLOTS));
        panel.add(playButton);

        JButton leaderboardButton = createButton("Leaderboard", 515, 360, 250, 60);
        leaderboardButton.addActionListener(event -> showScreen(LEADERBOARD));
        panel.add(leaderboardButton);

        JButton settingsButton = createButton("Settings", 515, 440, 250, 60);
        settingsButton.addActionListener(event -> showScreen(SETTINGS));
        panel.add(settingsButton);

        JButton adminButton = createButton("Admin Panel", 515, 520, 250, 60);
        adminButton.addActionListener(event -> openAdminPanel());
        panel.add(adminButton);

        JButton logoutButton = createButton("Logout", 515, 600, 250, 50);
        logoutButton.addActionListener(event -> showScreen(LOGIN));
        panel.add(logoutButton);

        return panel;
    }

    private JPanel createSaveSlotScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        addCenteredTitle(panel, "Choose Save Slot", 80, 42);
        JPanel slots = new JPanel(new GridLayout(1, 3, 20, 0));
        slots.setBounds(240, 260, 800, 180);

        for (int slot = 1; slot <= 3; slot++) {
            JButton slotButton = new JButton("<html><center>Slot " + slot
                    + "<br><br>No save data</center></html>");
            int selectedSlot = slot;
            slotButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
            slotButton.addActionListener(event -> {
                showScreen(GAME);
                Toolkit.getDefaultToolkit().beep();
                System.out.println("Selected save slot " + selectedSlot);
            });
            slots.add(slotButton);
        }
        panel.add(slots);

        JButton backButton = createButton("Back", 515, 500, 250, 50);
        backButton.addActionListener(event -> showScreen(MAIN_MENU));
        panel.add(backButton);
        return panel;
    }

    private JPanel createSettingsScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        addCenteredTitle(panel, "Settings", 100, 42);

        JLabel volumeLabel = new JLabel("Master Volume");
        volumeLabel.setBounds(440, 240, 400, 30);
        panel.add(volumeLabel);

        masterVolumeSlider = new JSlider(0, 100, 80);
        masterVolumeSlider.setBounds(440, 270, 400, 50);
        masterVolumeSlider.setMajorTickSpacing(20);
        masterVolumeSlider.setPaintTicks(true);
        panel.add(masterVolumeSlider);

        JLabel resolutionLabel = new JLabel("Resolution");
        resolutionLabel.setBounds(440, 340, 400, 30);
        panel.add(resolutionLabel);

        resolutionSelector = new JComboBox<>(new String[]{"1280 x 720", "1920 x 1080"});
        resolutionSelector.setBounds(440, 370, 400, 40);
        resolutionSelector.setSelectedIndex(0);
        panel.add(resolutionSelector);

        JButton backButton = createButton("Back", 515, 500, 250, 50);
        backButton.addActionListener(event -> showScreen(MAIN_MENU));
        panel.add(backButton);
        return panel;
    }

    private JPanel createGameScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel status = new JLabel("ParaLives - Game", SwingConstants.LEFT);
        status.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        status.setBounds(40, 20, 400, 40);
        panel.add(status);

        JButton pauseButton = createButton("Pause", 1040, 30, 200, 50);
        pauseButton.addActionListener(event -> openPauseOverlay());
        panel.add(pauseButton);

        JLabel gameArea = new JLabel("Game view", SwingConstants.CENTER);
        gameArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 32));
        gameArea.setBounds(100, 150, 1080, 400);
        panel.add(gameArea);
        return panel;
    }

    private JPanel createLeaderboardScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        addCenteredTitle(panel, "Leaderboard", 80, 42);

        JLabel tableHeader = new JLabel("<html><b>Rank &nbsp;&nbsp;&nbsp;&nbsp; Player &nbsp;&nbsp;&nbsp;&nbsp; Score</b></html>");
        tableHeader.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 24));
        tableHeader.setBounds(420, 220, 500, 40);
        panel.add(tableHeader);

        String[] scores = {"1. Alex - 9840", "2. Sam - 8710", "3. Riley - 7450",
                "4. Jamie - 6820", "5. Taylor - 5910"};
        for (int index = 0; index < scores.length; index++) {
            JLabel row = new JLabel(scores[index]);
            row.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 22));
            row.setBounds(420, 270 + index * 45, 500, 35);
            panel.add(row);
        }

        JButton backButton = createButton("Back", 515, 560, 250, 50);
        backButton.addActionListener(event -> showScreen(MAIN_MENU));
        panel.add(backButton);
        return panel;
    }

    private void openPauseOverlay() {
        JDialog pauseDialog = new JDialog(this, "Game Paused", true);
        pauseDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        pauseDialog.setResizable(false);
        pauseDialog.setSize(420, 360);
        pauseDialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        JLabel title = new JLabel("Paused", SwingConstants.CENTER);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 32));
        title.setBounds(60, 30, 300, 50);
        panel.add(title);

        JButton resumeButton = createButton("Resume", 110, 110, 200, 50);
        resumeButton.addActionListener(event -> pauseDialog.dispose());
        panel.add(resumeButton);

        JButton settingsButton = createButton("Settings", 110, 170, 200, 50);
        settingsButton.addActionListener(event -> {
            pauseDialog.dispose();
            showScreen(SETTINGS);
        });
        panel.add(settingsButton);

        JButton quitButton = createButton("Quit to Main Menu", 110, 230, 200, 50);
        quitButton.addActionListener(event -> {
            pauseDialog.dispose();
            showScreen(MAIN_MENU);
        });
        panel.add(quitButton);

        pauseDialog.setContentPane(panel);
        pauseDialog.setVisible(true);
    }

    private void openAdminPanel() {
        JFrame adminFrame = new JFrame("ParaLives Admin Panel");
        adminFrame.setSize(1280, 720);
        adminFrame.setResizable(false);
        adminFrame.setLocationRelativeTo(this);
        adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        addCenteredTitle(panel, "Admin Panel", 80, 42);

        JButton usersButton = createButton("Manage Users", 390, 250, 500, 60);
        panel.add(usersButton);
        JButton scoresButton = createButton("Manage Scores", 390, 330, 500, 60);
        panel.add(scoresButton);
        JButton dataButton = createButton("Database Tools", 390, 410, 500, 60);
        panel.add(dataButton);

        adminFrame.add(panel);
        adminFrame.setVisible(true);
    }

    private JPanel createLabeledField(String labelText, int x, int y) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(null);
        fieldPanel.setBounds(x, y, 400, 70);

        JLabel label = new JLabel(labelText);
        label.setBounds(0, 0, 400, 25);
        fieldPanel.add(label);

        JTextField field = new JTextField();
        field.setBounds(0, 30, 400, 40);
        fieldPanel.add(field);
        return fieldPanel;
    }

    private JPanel createPasswordField(String labelText, int x, int y) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(null);
        fieldPanel.setBounds(x, y, 400, 70);

        JLabel label = new JLabel(labelText);
        label.setBounds(0, 0, 400, 25);
        fieldPanel.add(label);

        JPasswordField field = new JPasswordField();
        field.setBounds(0, 30, 400, 40);
        fieldPanel.add(field);
        return fieldPanel;
    }

    private void addCenteredTitle(JPanel panel, String text, int y, int fontSize) {
        JLabel title = new JLabel(text, SwingConstants.CENTER);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, fontSize));
        title.setBounds(240, y, 800, 70);
        panel.add(title);
    }

    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        button.setBounds(x, y, width, height);
        return button;
    }
}
