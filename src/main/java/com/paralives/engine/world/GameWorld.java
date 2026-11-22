package com.paralives.engine.world;

import com.paralives.engine.network.PlayerInput;
import java.util.EnumMap;
import java.util.ArrayList;
import java.util.List;
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
    private final Map<DimensionId, List<GameObject>> dimensionObjects =
            new EnumMap<>(DimensionId.class);

    public GameWorld() {
        dimensionMaps.put(DimensionId.NORMAL, createNormalMap());
        dimensionMaps.put(DimensionId.POWERED, createPoweredMap());
        dimensionObjects.put(DimensionId.NORMAL, createNormalObjects());
        dimensionObjects.put(DimensionId.POWERED, createPoweredObjects());
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

    private List<GameObject> createNormalObjects() {
        List<GameObject> objects = new ArrayList<>();
        objects.add(new GameObject(GameObjectType.KEY, 5, 5, DimensionId.NORMAL));
        GameObject door = new GameObject(
                GameObjectType.DOOR, 20, 6, DimensionId.NORMAL);
        door.setActive(true);
        objects.add(door);
        objects.add(new GameObject(GameObjectType.BOX, 8, 8, DimensionId.NORMAL));
        return objects;
    }

    private List<GameObject> createPoweredObjects() {
        List<GameObject> objects = new ArrayList<>();
        objects.add(new GameObject(GameObjectType.LEVER, 4, 3, DimensionId.POWERED));
        objects.add(new GameObject(GameObjectType.VALVE, 20, 3, DimensionId.POWERED));
        GameObject door = new GameObject(
                GameObjectType.DOOR, 13, 6, DimensionId.POWERED);
        door.setActive(true);
        objects.add(door);
        objects.add(new GameObject(GameObjectType.BOX, 16, 9, DimensionId.POWERED));
        return objects;
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
        if (inputA.isInteract()) {
            interactPlayer(playerAX, playerAY, width, height);
        }
        if (inputB.isInteract()) {
            interactPlayer(playerBX, playerBY, width, height);
        }
        tick++;
    }

    private void interactPlayer(int playerX, int playerY, int panelWidth, int panelHeight) {
        TileMap map = getActiveTileMap();
        int tileWidth = panelWidth / map.getWidth();
        int tileHeight = panelHeight / map.getHeight();
        if (tileWidth <= 0 || tileHeight <= 0) {
            return;
        }

        int playerTileX = playerX / tileWidth;
        int playerTileY = playerY / tileHeight;
        for (GameObject object : getObjectsForActiveDimension()) {
            int distance = Math.abs(object.getTileX() - playerTileX)
                    + Math.abs(object.getTileY() - playerTileY);
            if (distance > 1) {
                continue;
            }

            switch (object.getType()) {
                case KEY -> object.setCollected(true);
                case LEVER, VALVE -> object.setActive(!object.isActive());
                case DOOR -> {
                    if (hasCollectedKey()) {
                        object.setActive(false);
                    }
                }
                case BOX -> {
                }
            }
        }
    }

    private boolean hasCollectedKey() {
        return dimensionObjects.values().stream()
                .flatMap(List::stream)
                .anyMatch(object -> object.getType() == GameObjectType.KEY
                        && object.isCollected());
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
        if (map.isSolid(tileX, tileY)) {
            return false;
        }
        for (GameObject object : getObjectsForActiveDimension()) {
            if (object.getTileX() == tileX
                    && object.getTileY() == tileY
                    && object.isSolid()) {
                if (object.getType() == GameObjectType.DOOR
                        && hasCollectedKey()) {
                    object.setActive(false);
                    continue;
                }
                return false;
            }
        }
        return true;
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
        GameSnapshot snapshot = new GameSnapshot(
                playerAX, playerAY, playerBX, playerBY, tick,
                activeDimension);
        snapshot.setObjects(copyObjects());
        return snapshot;
    }

    private List<GameObject> copyObjects() {
        List<GameObject> copy = new ArrayList<>();
        for (List<GameObject> dimensionObjects : dimensionObjects.values()) {
            for (GameObject object : dimensionObjects) {
                GameObject copied = new GameObject(
                        object.getType(),
                        object.getTileX(),
                        object.getTileY(),
                        object.getDimensionId());
                copied.setActive(object.isActive());
                copied.setCollected(object.isCollected());
                copy.add(copied);
            }
        }
        return copy;
    }

    public void applySnapshot(GameSnapshot snapshot) {
        playerAX = snapshot.getPlayerAX();
        playerAY = snapshot.getPlayerAY();
        playerBX = snapshot.getPlayerBX();
        playerBY = snapshot.getPlayerBY();
        tick = snapshot.getTick();
        activeDimension = snapshot.getActiveDimension();
        applyObjectState(snapshot.getObjects());
    }

    private void applyObjectState(List<GameObject> snapshotObjects) {
        for (GameObject snapshotObject : snapshotObjects) {
            for (GameObject object : dimensionObjects
                    .getOrDefault(snapshotObject.getDimensionId(), List.of())) {
                if (object.getType() == snapshotObject.getType()
                        && object.getTileX() == snapshotObject.getTileX()
                        && object.getTileY() == snapshotObject.getTileY()) {
                    object.setActive(snapshotObject.isActive());
                    object.setCollected(snapshotObject.isCollected());
                }
            }
        }
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

    public List<GameObject> getObjectsForActiveDimension() {
        return dimensionObjects.getOrDefault(activeDimension, List.of());
    }

    public List<GameObject> getObjects(DimensionId dimensionId) {
        return dimensionObjects.getOrDefault(dimensionId, List.of());
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
