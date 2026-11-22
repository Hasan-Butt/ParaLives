package com.paralives.engine.world;

import java.io.Serial;
import java.io.Serializable;

public class TileMap implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int width;
    private final int height;
    private final TileType[][] tiles;

    public TileMap(int width, int height) {
        this.width = width;
        this.height = height;
        tiles = new TileType[height][width];
        fill(TileType.FLOOR);
    }

    public void fill(TileType tileType) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tiles[y][x] = tileType;
            }
        }
    }

    public TileType getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return TileType.WALL;
        }
        return tiles[y][x];
    }

    public void setTile(int x, int y, TileType tileType) {
        if (x >= 0 && y >= 0 && x < width && y < height) {
            tiles[y][x] = tileType;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isSolid(int x, int y) {
        TileType tile = getTile(x, y);
        return tile == TileType.WALL || tile == TileType.WATER;
    }
}
