package com.paralives.engine.settings;

import com.paralives.engine.input.GameCommand;

import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

public class AppSettings {
    private final SettingsStore store;
    private int masterVolume = 80;
    private boolean muted;
    private final Map<GameCommand, Integer> playerAKeyBindings = new EnumMap<>(GameCommand.class);
    private final Map<GameCommand, Integer> playerBKeyBindings = new EnumMap<>(GameCommand.class);

    public AppSettings() {
        this(SettingsStore.defaultStore());
    }

    public AppSettings(SettingsStore store) {
        this.store = store;
        loadDefaults();
        load();
    }

    private void loadDefaults() {
        playerAKeyBindings.put(GameCommand.MOVE_UP, 87);
        playerAKeyBindings.put(GameCommand.MOVE_DOWN, 83);
        playerAKeyBindings.put(GameCommand.MOVE_LEFT, 65);
        playerAKeyBindings.put(GameCommand.MOVE_RIGHT, 68);
        playerAKeyBindings.put(GameCommand.INTERACT, 69);

        playerBKeyBindings.put(GameCommand.MOVE_UP, 38);
        playerBKeyBindings.put(GameCommand.MOVE_DOWN, 40);
        playerBKeyBindings.put(GameCommand.MOVE_LEFT, 37);
        playerBKeyBindings.put(GameCommand.MOVE_RIGHT, 39);
        playerBKeyBindings.put(GameCommand.INTERACT, 10);
    }

    private void load() {
        masterVolume = store.getInt("master_volume", masterVolume);
        muted = store.getBoolean("muted", muted);
        for (GameCommand command : GameCommand.values()) {
            playerAKeyBindings.put(command, store.getInt(
                    "key.player_a." + command.name(),
                    playerAKeyBindings.get(command)));
            playerBKeyBindings.put(command, store.getInt(
                    "key.player_b." + command.name(),
                    playerBKeyBindings.get(command)));
        }
    }

    public void save() {
        store.setInt("master_volume", masterVolume);
        store.setBoolean("muted", muted);
        for (GameCommand command : GameCommand.values()) {
            store.setInt("key.player_a." + command.name(), playerAKeyBindings.get(command));
            store.setInt("key.player_b." + command.name(), playerBKeyBindings.get(command));
        }
        store.save();
    }

    public int getMasterVolume() {
        return masterVolume;
    }

    public void setMasterVolume(int masterVolume) {
        this.masterVolume = Math.max(0, Math.min(100, masterVolume));
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public int getPlayerAKey(GameCommand command) {
        return playerAKeyBindings.get(command);
    }

    public int getPlayerBKey(GameCommand command) {
        return playerBKeyBindings.get(command);
    }

    public boolean rebindPlayerA(GameCommand command, int keyCode) {
        if (isKeyUsedByPlayerA(keyCode, command) || isKeyUsedByPlayerB(keyCode)) {
            return false;
        }
        playerAKeyBindings.put(command, keyCode);
        return true;
    }

    public boolean rebindPlayerB(GameCommand command, int keyCode) {
        if (isKeyUsedByPlayerB(keyCode, command) || isKeyUsedByPlayerA(keyCode)) {
            return false;
        }
        playerBKeyBindings.put(command, keyCode);
        return true;
    }

    public void resetKeyBindings() {
        loadDefaults();
    }

    private boolean isKeyUsedByPlayerA(int keyCode) {
        return playerAKeyBindings.containsValue(keyCode);
    }

    private boolean isKeyUsedByPlayerA(int keyCode, GameCommand ignoredCommand) {
        return playerAKeyBindings.entrySet().stream()
                .anyMatch(entry -> entry.getKey() != ignoredCommand && entry.getValue() == keyCode);
    }

    private boolean isKeyUsedByPlayerB(int keyCode) {
        return playerBKeyBindings.containsValue(keyCode);
    }

    private boolean isKeyUsedByPlayerB(int keyCode, GameCommand ignoredCommand) {
        return playerBKeyBindings.entrySet().stream()
                .anyMatch(entry -> entry.getKey() != ignoredCommand && entry.getValue() == keyCode);
    }

    public Path getConfigPath() {
        return store.getPath();
    }
}
