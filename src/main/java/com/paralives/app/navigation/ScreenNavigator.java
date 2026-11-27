package com.paralives.app.navigation;

public interface ScreenNavigator {
    void showScreen(String screenName);

    default void showAnimation(String animationFileName, Runnable onComplete) {
        if (onComplete != null) {
            onComplete.run();
        }
    }
}
