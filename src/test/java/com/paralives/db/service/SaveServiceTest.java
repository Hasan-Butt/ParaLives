package com.paralives.db.service;

import com.paralives.db.model.Player;
import com.paralives.db.model.SaveSlot;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SaveServiceTest {
    private static SaveService saveService;
    private static PlayerService playerService;
    private static Long testPlayerId;

    @BeforeAll
    static void setUp() {
        saveService = new SaveService();
        playerService = new PlayerService();
        
        String testEmail = "savegame_" + System.currentTimeMillis() + "@test.com";
        Player player = new Player("Save Test Player", testEmail);
        player = playerService.create(player);
        testPlayerId = player.getId();
    }

    @Test
    @Order(1)
    void testSaveToSlot1() {
        String gameState = "{\"puzzle\":1,\"time\":100}";
        SaveSlot slot = saveService.save(testPlayerId, 1, gameState, 1);
        
        assertNotNull(slot);
        assertEquals(1, slot.getSlotNumber());
        assertEquals(gameState, slot.getGameStateJson());
        assertEquals(1, slot.getPuzzleIndex());
    }

    @Test
    @Order(2)
    void testLoadSlot() {
        Optional<SaveSlot> loaded = saveService.load(testPlayerId, 1);
        assertTrue(loaded.isPresent());
        assertEquals(1, loaded.get().getSlotNumber());
    }

    @Test
    @Order(3)
    void testOverwriteExistingSlot() {
        String newGameState = "{\"puzzle\":2,\"time\":200}";
        SaveSlot updated = saveService.save(testPlayerId, 1, newGameState, 2);
        
        assertEquals(newGameState, updated.getGameStateJson());
        assertEquals(2, updated.getPuzzleIndex());
    }

    @Test
    @Order(4)
    void testSaveMultipleSlots() {
        saveService.save(testPlayerId, 2, "{\"puzzle\":3}", 3);
        saveService.save(testPlayerId, 3, "{\"puzzle\":4}", 4);
        
        List<SaveSlot> slots = saveService.listSlots(testPlayerId);
        assertEquals(3, slots.size());
    }

    @Test
    @Order(5)
    void testListSlots() {
        List<SaveSlot> slots = saveService.listSlots(testPlayerId);
        assertFalse(slots.isEmpty());
        assertTrue(slots.size() <= 3);
    }

    @Test
    @Order(6)
    void testInvalidSlotNumber() {
        assertThrows(IllegalArgumentException.class, () -> {
            saveService.save(testPlayerId, 0, "{}", 0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            saveService.save(testPlayerId, 4, "{}", 0);
        });
    }

    @Test
    @Order(7)
    void testDeleteSlot() {
        saveService.deleteSlot(testPlayerId, 2);
        Optional<SaveSlot> deleted = saveService.load(testPlayerId, 2);
        assertFalse(deleted.isPresent());
    }

    @Test
    @Order(8)
    void testLoadNonExistentSlot() {
        Optional<SaveSlot> result = saveService.load(testPlayerId, 2);
        assertFalse(result.isPresent());
    }

    @AfterAll
    static void tearDown() {
        if (testPlayerId != null) {
            playerService.delete(testPlayerId);
        }
    }
}
