package com.paralives.engine.world;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TileMapTest {
    @Test
    void supportsConfiguredTileTypesAndSolidTiles() {
        TileMap map = new TileMap(4, 3);
        map.setTile(1, 1, TileType.WATER);
        map.setTile(2, 1, TileType.WALL);
        map.setTile(3, 1, TileType.POWERED);

        assertTrue(map.isSolid(1, 1));
        assertTrue(map.isSolid(2, 1));
        assertFalse(map.isSolid(3, 1));
        assertEquals(TileType.POWERED, map.getTile(3, 1));
    }

    @Test
    void gameWorldCreatesNormalAndPoweredDimensions() {
        GameWorld world = new GameWorld();

        assertEquals(DimensionId.NORMAL, world.getActiveDimension());
        assertEquals(27, world.getActiveTileMap().getWidth());

        world.toggleDimension();

        assertEquals(DimensionId.POWERED, world.getActiveDimension());
        assertTrue(world.getActiveTileMap().getTile(13, 6) == TileType.FLOOR);
    }
}
