package com.paralives.app;

import com.paralives.app.navigation.ScreenNavigator;
import com.paralives.app.presenter.AppPresenter;
import com.paralives.app.view.ScreenNames;
import com.paralives.app.view.screens.GameScreen;
import com.paralives.app.view.screens.LeaderboardScreen;
import com.paralives.app.view.screens.LoginScreen;
import com.paralives.app.view.screens.MainMenuScreen;
import com.paralives.app.view.screens.RegisterScreen;
import com.paralives.app.view.screens.SaveSlotScreen;
import com.paralives.app.view.screens.SettingsScreen;
import com.paralives.engine.settings.AppSettings;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Dimension;

public class MainFrame extends JFrame implements ScreenNavigator {
    public static final int FRAME_WIDTH = 1280;
    public static final int FRAME_HEIGHT = 720;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel screenPanel = new JPanel(cardLayout);
    private final AppPresenter presenter;
    private final AppSettings settings;

    public MainFrame() {
        super("ParaLives");
        settings = new AppSettings();
        presenter = new AppPresenter(this, settings);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setLocationRelativeTo(null);

        registerScreens();
        add(screenPanel);
        showScreen(ScreenNames.LOGIN);
    }

    private void registerScreens() {
        screenPanel.add(new LoginScreen(presenter), ScreenNames.LOGIN);
        screenPanel.add(new RegisterScreen(presenter), ScreenNames.REGISTER);
        screenPanel.add(new MainMenuScreen(presenter), ScreenNames.MAIN_MENU);
        screenPanel.add(new SaveSlotScreen(presenter), ScreenNames.SAVE_SLOTS);
        screenPanel.add(new SettingsScreen(presenter, settings), ScreenNames.SETTINGS);
        screenPanel.add(new GameScreen(presenter, this, settings), ScreenNames.GAME);
        screenPanel.add(new LeaderboardScreen(presenter), ScreenNames.LEADERBOARD);
    }

    @Override
    public void showScreen(String screenName) {
        cardLayout.show(screenPanel, screenName);
    }
}
