package com.paralives.engine.world;

import java.io.Serial;
import java.io.Serializable;

public class GameSnapshot implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int playerAX;
    private int playerAY;
    private int playerBX;
    private int playerBY;
    private long tick;

    public GameSnapshot() {
    }

    public GameSnapshot(int playerAX, int playerAY, int playerBX, int playerBY, long tick) {
        this.playerAX = playerAX;
        this.playerAY = playerAY;
        this.playerBX = playerBX;
        this.playerBY = playerBY;
        this.tick = tick;
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
