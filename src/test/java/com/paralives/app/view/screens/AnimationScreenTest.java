package com.paralives.app.view.screens;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnimationScreenTest {
    @Test
    void resolvesExistingAnimationsInResourcesFolder() {
        Path introPath = AnimationScreen.animationPath("intro.mp4");
        assertEquals(Path.of("resources", "Animations", "intro.mp4"), introPath);

        if (Files.isRegularFile(introPath)) {
            String uri = AnimationScreen.resolveMediaUri(introPath);
            assertNotNull(uri);
            assertTrue(uri.endsWith("/resources/Animations/intro.mp4"));
        }
    }

    @Test
    void usesPressEnterPromptText() {
        assertEquals("Press Enter to continue...", AnimationScreen.DEFAULT_PROMPT);
    }

    @Test
    void returnsNullForMissingAnimationFile() {
        assertNull(AnimationScreen.resolveMediaUri(Path.of("resources", "Animations", "does-not-exist.mp4")));
        assertNull(AnimationScreen.resolveMediaUri(null));
    }
}

