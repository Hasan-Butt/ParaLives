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
    private DimensionId playerADimension;
    private DimensionId playerBDimension;
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
        this.playerADimension = activeDimension;
        this.playerBDimension = activeDimension;
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

    public DimensionId getPlayerADimension() {
        return playerADimension == null
                ? getActiveDimension()
                : playerADimension;
    }

    public DimensionId getPlayerBDimension() {
        return playerBDimension == null
                ? getActiveDimension()
                : playerBDimension;
    }

    public void setPlayerDimensions(DimensionId playerADimension,
                                    DimensionId playerBDimension) {
        this.playerADimension = playerADimension;
        this.playerBDimension = playerBDimension;
    }
}
