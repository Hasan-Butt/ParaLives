package com.paralives.engine.world;

import com.paralives.engine.network.PlayerInput;

public class GameWorld {
    public static final int PLAYER_RADIUS = 18;
    public static final int PLAYER_SPEED = 4;

    private int playerAX = 260;
    private int playerAY = 230;
    private int playerBX = 820;
    private int playerBY = 230;
    private long tick;

    public void update(PlayerInput inputA, PlayerInput inputB, int width, int height) {
        playerAX = moveX(playerAX, inputA.isLeft(), inputA.isRight(), width);
        playerAY = moveY(playerAY, inputA.isUp(), inputA.isDown(), height);
        playerBX = moveX(playerBX, inputB.isLeft(), inputB.isRight(), width);
        playerBY = moveY(playerBY, inputB.isUp(), inputB.isDown(), height);
        tick++;
    }

    private int moveX(int position, boolean left, boolean right, int width) {
        int next = position;
        if (left) {
            next -= PLAYER_SPEED;
        }
        if (right) {
            next += PLAYER_SPEED;
        }
        return clamp(next, width);
    }

    private int moveY(int position, boolean up, boolean down, int height) {
        int next = position;
        if (up) {
            next -= PLAYER_SPEED;
        }
        if (down) {
            next += PLAYER_SPEED;
        }
        return clamp(next, height);
    }

    private int clamp(int position, int dimension) {
        return Math.max(PLAYER_RADIUS, Math.min(dimension - PLAYER_RADIUS, position));
    }

    public GameSnapshot toSnapshot() {
        return new GameSnapshot(playerAX, playerAY, playerBX, playerBY, tick);
    }

    public void applySnapshot(GameSnapshot snapshot) {
        playerAX = snapshot.getPlayerAX();
        playerAY = snapshot.getPlayerAY();
        playerBX = snapshot.getPlayerBX();
        playerBY = snapshot.getPlayerBY();
        tick = snapshot.getTick();
    }

    public int getPlayerAX() {
        return playerAX;
    }

    public int getPlayerAY() {
        return playerAY;
    }

    public int getPlayerBX() {
        return playerBX;
    }

    public int getPlayerBY() {
        return playerBY;
    }

    public long getTick() {
        return tick;
    }
}
