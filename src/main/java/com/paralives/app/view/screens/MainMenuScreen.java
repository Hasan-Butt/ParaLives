package com.paralives.app.view.screens;

import com.paralives.app.presenter.AppPresenter;
import com.paralives.app.view.BaseScreen;
import com.paralives.app.view.overlay.AdminPanelFrame;

import javax.swing.JButton;

public class MainMenuScreen extends BaseScreen {
    public MainMenuScreen(AppPresenter presenter) {
        addCenteredTitle("ParaLives", 110, 48);

        JButton playButton = createButton("Play", 515, 280, 250, 60);
        playButton.addActionListener(event -> presenter.openSaveSlots());
        add(playButton);

        JButton leaderboardButton = createButton("Leaderboard", 515, 360, 250, 60);
        leaderboardButton.addActionListener(event -> presenter.openLeaderboard());
        add(leaderboardButton);

        JButton settingsButton = createButton("Settings", 515, 440, 250, 60);
        settingsButton.addActionListener(event -> presenter.openSettings());
        add(settingsButton);

        JButton adminButton = createButton("Admin Panel", 515, 520, 250, 60);
        adminButton.addActionListener(event -> new AdminPanelFrame().setVisible(true));
        add(adminButton);

        JButton logoutButton = createButton("Logout", 515, 600, 250, 50);
        logoutButton.addActionListener(event -> presenter.logout());
        add(logoutButton);
    }
}
