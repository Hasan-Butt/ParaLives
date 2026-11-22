package com.paralives.engine.network;

import java.io.Serial;
import java.io.Serializable;

public class PlayerInput implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;
    private boolean interact;
    private long timestamp;

    public PlayerInput() {
        this.timestamp = System.currentTimeMillis();
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isInteract() {
        return interact;
    }

    public void setInteract(boolean interact) {
        this.interact = interact;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
