package com.paralives.engine.world;

import com.paralives.engine.network.PlayerInput;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameWorldTest {
    @Test
    void movesBothPlayersAndCreatesSnapshot() {
        GameWorld world = new GameWorld();
        PlayerInput inputA = new PlayerInput();
        inputA.setRight(true);
        PlayerInput inputB = new PlayerInput();
        inputB.setLeft(true);

        world.update(inputA, inputB, 1080, 520);

        assertEquals(264, world.getPlayerAX());
        assertEquals(816, world.getPlayerBX());
        assertEquals(1, world.getTick());
    }

    @Test
    void appliesSnapshotToDisplayWorld() {
        GameWorld world = new GameWorld();
        world.applySnapshot(new GameSnapshot(100, 200, 300, 400, 12));

        assertEquals(100, world.getPlayerAX());
        assertEquals(200, world.getPlayerAY());
        assertEquals(300, world.getPlayerBX());
        assertEquals(400, world.getPlayerBY());
        assertTrue(world.getTick() == 12);
    }
}
