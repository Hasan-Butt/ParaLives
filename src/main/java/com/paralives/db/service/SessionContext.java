package com.paralives.db.service;

import com.paralives.db.model.Player;

import java.util.Optional;

public final class SessionContext {
    private static final SessionContext INSTANCE = new SessionContext();
    private Player currentPlayer;

    private SessionContext() {
    }

    // Returns the singleton instance
    public static SessionContext getInstance() {
        return INSTANCE;
    }

    // Sets the currently logged-in player
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    // Returns the currently logged-in player if exists
    public Optional<Player> getCurrentPlayer() {
        return Optional.ofNullable(currentPlayer);
    }

    // Clears the current session
    public void clearSession() {
        this.currentPlayer = null;
    }

    // Checks if a player is currently logged in
    public boolean isLoggedIn() {
        return currentPlayer != null;
    }
}
