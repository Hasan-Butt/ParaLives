package com.paralives.app.presenter;

import com.paralives.app.navigation.ScreenNavigator;
import com.paralives.engine.settings.AppSettings;

public class AppPresenter {
    private final ScreenNavigator navigator;
    private final AppSettings settings;

    public AppPresenter(ScreenNavigator navigator) {
        this(navigator, new AppSettings());
    }

    public AppPresenter(ScreenNavigator navigator, AppSettings settings) {
        this.navigator = navigator;
        this.settings = settings;
    }

    public AppSettings getSettings() {
        return settings;
    }

    public void login() {
        navigator.showScreen(com.paralives.app.view.ScreenNames.MAIN_MENU);
    }

    public void register() {
        navigator.showScreen(com.paralives.app.view.ScreenNames.REGISTER);
    }

    public void completeRegistration() {
        navigator.showScreen(com.paralives.app.view.ScreenNames.LOGIN);
    }

    public void openMainMenu() {
        navigator.showScreen(com.paralives.app.view.ScreenNames.MAIN_MENU);
    }

    public void openSaveSlots() {
        navigator.showScreen(com.paralives.app.view.ScreenNames.SAVE_SLOTS);
    }

    public void openSettings() {
        navigator.showScreen(com.paralives.app.view.ScreenNames.SETTINGS);
    }

    public void openGame() {
        navigator.showScreen(com.paralives.app.view.ScreenNames.GAME);
    }

    public void openLeaderboard() {
        navigator.showScreen(com.paralives.app.view.ScreenNames.LEADERBOARD);
    }

    public void logout() {
        navigator.showScreen(com.paralives.app.view.ScreenNames.LOGIN);
    }
}
