package com.paralives.engine.world;

import com.paralives.engine.network.PlayerInput;
import java.util.EnumMap;
import java.util.Map;

public class GameWorld {
    public static final int PLAYER_RADIUS = 18;
    public static final int PLAYER_SPEED = 4;

    private int playerAX = 260;
    private int playerAY = 230;
    private int playerBX = 820;
    private int playerBY = 230;
    private long tick;
    private DimensionId activeDimension = DimensionId.NORMAL;
    private final Map<DimensionId, TileMap> dimensionMaps = new EnumMap<>(DimensionId.class);

    public GameWorld() {
        dimensionMaps.put(DimensionId.NORMAL, createNormalMap());
        dimensionMaps.put(DimensionId.POWERED, createPoweredMap());
    }

    private TileMap createNormalMap() {
        TileMap map = new TileMap(27, 13);
        map.fill(TileType.FLOOR);
        for (int x = 0; x < map.getWidth(); x++) {
            map.setTile(x, 0, TileType.WALL);
            map.setTile(x, map.getHeight() - 1, TileType.WALL);
        }
        for (int y = 0; y < map.getHeight(); y++) {
            map.setTile(0, y, TileType.WALL);
            map.setTile(map.getWidth() - 1, y, TileType.WALL);
        }
        for (int y = 4; y < 9; y++) {
            map.setTile(12, y, TileType.WATER);
        }
        map.setTile(20, 6, TileType.POWERED);
        return map;
    }

    private TileMap createPoweredMap() {
        TileMap map = new TileMap(27, 13);
        map.fill(TileType.POWERED);
        for (int x = 0; x < map.getWidth(); x++) {
            map.setTile(x, 0, TileType.WALL);
            map.setTile(x, map.getHeight() - 1, TileType.WALL);
        }
        for (int y = 0; y < map.getHeight(); y++) {
            map.setTile(0, y, TileType.WALL);
            map.setTile(map.getWidth() - 1, y, TileType.WALL);
        }
        for (int x = 5; x < 22; x++) {
            map.setTile(x, 6, TileType.WALL);
        }
        map.setTile(13, 6, TileType.FLOOR);
        map.setTile(20, 3, TileType.VOID);
        return map;
    }

    public void update(PlayerInput inputA, PlayerInput inputB, int width, int height) {
        int nextAX = moveX(playerAX, inputA.isLeft(), inputA.isRight(), width);
        int nextAY = moveY(playerAY, inputA.isUp(), inputA.isDown(), height);
        int nextBX = moveX(playerBX, inputB.isLeft(), inputB.isRight(), width);
        int nextBY = moveY(playerBY, inputB.isUp(), inputB.isDown(), height);

        if (canOccupy(nextAX, nextAY, width, height)) {
            playerAX = nextAX;
            playerAY = nextAY;
        }
        if (canOccupy(nextBX, nextBY, width, height)) {
            playerBX = nextBX;
            playerBY = nextBY;
        }
        tick++;
    }

    private boolean canOccupy(int x, int y, int panelWidth, int panelHeight) {
        TileMap map = getActiveTileMap();
        int tileWidth = panelWidth / map.getWidth();
        int tileHeight = panelHeight / map.getHeight();
        if (tileWidth <= 0 || tileHeight <= 0) {
            return true;
        }
        int tileX = x / tileWidth;
        int tileY = y / tileHeight;
        return !map.isSolid(tileX, tileY);
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
        return new GameSnapshot(
                playerAX, playerAY, playerBX, playerBY, tick,
                activeDimension);
    }

    public void applySnapshot(GameSnapshot snapshot) {
        playerAX = snapshot.getPlayerAX();
        playerAY = snapshot.getPlayerAY();
        playerBX = snapshot.getPlayerBX();
        playerBY = snapshot.getPlayerBY();
        tick = snapshot.getTick();
        activeDimension = snapshot.getActiveDimension();
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
        return activeDimension;
    }

    public TileMap getActiveTileMap() {
        return dimensionMaps.get(activeDimension);
    }

    public TileMap getTileMap(DimensionId dimensionId) {
        return dimensionMaps.get(dimensionId);
    }

    public void setActiveDimension(DimensionId dimensionId) {
        if (dimensionMaps.containsKey(dimensionId)) {
            activeDimension = dimensionId;
        }
    }

    public void toggleDimension() {
        activeDimension = activeDimension == DimensionId.NORMAL
                ? DimensionId.POWERED
                : DimensionId.NORMAL;
    }
}
