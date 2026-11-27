package com.paralives.app.presenter;

import com.paralives.app.navigation.ScreenNavigator;
import com.paralives.engine.network.NetworkRole;
import com.paralives.engine.settings.AppSettings;

public class AppPresenter {
    private final ScreenNavigator navigator;
    private final AppSettings settings;
    private NetworkRole networkRole = NetworkRole.LOCAL;
    private int networkPort = 54545;
    private String networkHostAddress = "127.0.0.1";

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

    public NetworkRole getNetworkRole() {
        return networkRole;
    }

    public int getNetworkPort() {
        return networkPort;
    }

    public String getNetworkHostAddress() {
        return networkHostAddress;
    }

    public void hostGame(int port) {
        networkRole = NetworkRole.HOST;
        networkPort = port;
        openGame();
    }

    public void joinGame(String hostAddress, int port) {
        networkRole = NetworkRole.CLIENT;
        networkHostAddress = hostAddress.trim();
        networkPort = port;
        openGame();
    }

    public void useLocalGame() {
        networkRole = NetworkRole.LOCAL;
    }

    public void openIntro() {
        navigator.showScreen(com.paralives.app.view.ScreenNames.INTRO);
    }

    public void finishIntro() {
        openMainMenu();
    }

    public void playAnimation(String animationFileName, Runnable onComplete) {
        navigator.showAnimation(animationFileName, onComplete);
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
        useLocalGame();
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
