package com.paralives.engine.settings;

import com.paralives.engine.input.GameCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppSettingsTest {
    @TempDir
    Path tempDirectory;

    @Test
    void savesAndLoadsVolumeMuteAndBindings() {
        Path configPath = tempDirectory.resolve("settings.properties");
        AppSettings settings = new AppSettings(new SettingsStore(configPath));

        settings.setMasterVolume(42);
        settings.setMuted(true);
        assertTrue(settings.rebindPlayerA(GameCommand.MOVE_UP, 73));
        settings.save();

        AppSettings restored = new AppSettings(new SettingsStore(configPath));
        assertEquals(42, restored.getMasterVolume());
        assertTrue(restored.isMuted());
        assertEquals(73, restored.getPlayerAKey(GameCommand.MOVE_UP));
    }

    @Test
    void rejectsBindingsUsedByTheOtherPlayer() {
        AppSettings settings = new AppSettings(
                new SettingsStore(tempDirectory.resolve("conflicts.properties")));

        assertFalse(settings.rebindPlayerA(GameCommand.MOVE_RIGHT,
                settings.getPlayerBKey(GameCommand.MOVE_UP)));
    }
}
