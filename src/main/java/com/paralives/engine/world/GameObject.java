package com.paralives.engine.world;

import java.io.Serial;
import java.io.Serializable;

public class GameObject implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final GameObjectType type;
    private final int tileX;
    private final int tileY;
    private final DimensionId dimensionId;
    private boolean active;
    private boolean collected;

    public GameObject(GameObjectType type, int tileX, int tileY,
                      DimensionId dimensionId) {
        this.type = type;
        this.tileX = tileX;
        this.tileY = tileY;
        this.dimensionId = dimensionId;
    }

    public GameObjectType getType() {
        return type;
    }

    public int getTileX() {
        return tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public DimensionId getDimensionId() {
        return dimensionId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public boolean isSolid() {
        return type == GameObjectType.DOOR && active
                || type == GameObjectType.BOX;
    }
}
