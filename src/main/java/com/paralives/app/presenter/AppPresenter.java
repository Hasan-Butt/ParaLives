package com.paralives.app.presenter;

import com.paralives.app.navigation.ScreenNavigator;

public class AppPresenter {
    private final ScreenNavigator navigator;

    public AppPresenter(ScreenNavigator navigator) {
        this.navigator = navigator;
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
