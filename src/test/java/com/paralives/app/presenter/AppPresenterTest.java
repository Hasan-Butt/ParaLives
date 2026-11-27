package com.paralives.app.presenter;

import com.paralives.app.navigation.ScreenNavigator;
import com.paralives.app.view.ScreenNames;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppPresenterTest {
    @Test
    void finishIntroNavigatesToMainMenu() {
        RecordingNavigator navigator = new RecordingNavigator();
        AppPresenter presenter = new AppPresenter(navigator);

        presenter.openIntro();
        presenter.finishIntro();

        assertEquals(List.of(ScreenNames.INTRO, ScreenNames.MAIN_MENU), navigator.screens);
    }

    @Test
    void playAnimationDelegatesToNavigator() {
        RecordingNavigator navigator = new RecordingNavigator();
        AppPresenter presenter = new AppPresenter(navigator);
        AtomicBoolean completed = new AtomicBoolean(false);

        presenter.playAnimation("scene1.mp4", () -> completed.set(true));

        assertEquals("scene1.mp4", navigator.lastAnimation);
        assertTrue(completed.get());
    }

    private static final class RecordingNavigator implements ScreenNavigator {
        private final List<String> screens = new ArrayList<>();
        private String lastAnimation;

        @Override
        public void showScreen(String screenName) {
            screens.add(screenName);
        }

        @Override
        public void showAnimation(String animationFileName, Runnable onComplete) {
            lastAnimation = animationFileName;
            if (onComplete != null) {
                onComplete.run();
            }
        }
    }
}

