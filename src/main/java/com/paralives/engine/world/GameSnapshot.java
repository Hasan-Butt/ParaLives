package com.paralives.engine.world;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameSnapshot implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int playerAX;
    private int playerAY;
    private int playerBX;
    private int playerBY;
    private long tick;
    private DimensionId activeDimension;
    private List<GameObject> objects = new ArrayList<>();

    public GameSnapshot() {
    }

    public GameSnapshot(int playerAX, int playerAY, int playerBX, int playerBY, long tick) {
        this(playerAX, playerAY, playerBX, playerBY, tick, DimensionId.NORMAL);
    }

    public GameSnapshot(int playerAX, int playerAY, int playerBX, int playerBY,
                        long tick, DimensionId activeDimension) {
        this.playerAX = playerAX;
        this.playerAY = playerAY;
        this.playerBX = playerBX;
        this.playerBY = playerBY;
        this.tick = tick;
        this.activeDimension = activeDimension;
    }

    public List<GameObject> getObjects() {
        return objects;
    }

    public void setObjects(List<GameObject> objects) {
        this.objects = objects == null ? new ArrayList<>() : objects;
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

    public DimensionId getActiveDimension() {
        return activeDimension == null ? DimensionId.NORMAL : activeDimension;
    }
}
