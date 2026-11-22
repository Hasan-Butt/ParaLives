package com.paralives.engine.world;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameObjectTest {
    @Test
    void createsSupportedPuzzleObjects() {
        GameObject key = new GameObject(
                GameObjectType.KEY, 5, 5, DimensionId.NORMAL);
        GameObject door = new GameObject(
                GameObjectType.DOOR, 20, 6, DimensionId.NORMAL);
        door.setActive(true);
        GameObject box = new GameObject(
                GameObjectType.BOX, 8, 8, DimensionId.NORMAL);

        assertEquals(GameObjectType.KEY, key.getType());
        assertTrue(door.isSolid());
        assertTrue(box.isSolid());
        assertFalse(key.isCollected());
    }

    @Test
    void snapshotIncludesObjectState() {
        GameWorld world = new GameWorld();
        GameObject key = world.getObjects(DimensionId.NORMAL).stream()
                .filter(object -> object.getType() == GameObjectType.KEY)
                .findFirst()
                .orElseThrow();
        key.setCollected(true);

        GameSnapshot snapshot = world.toSnapshot();

        assertTrue(snapshot.getObjects().stream()
                .anyMatch(object -> object.getType() == GameObjectType.KEY
                        && object.isCollected()));

        key.setCollected(false);
        world.applySnapshot(snapshot);
        assertTrue(world.getObjects(DimensionId.NORMAL).stream()
                .anyMatch(object -> object.getType() == GameObjectType.KEY
                        && object.isCollected()));
    }
}
