package com.paralives.db.service;

import com.paralives.db.model.Player;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthServiceTest {
    private static AuthService authService;
    private static String testEmail;

    @BeforeAll
    static void setUp() {
        authService = new AuthService();
        testEmail = "test_" + System.currentTimeMillis() + "@test.com";
    }

    @Test
    @Order(1)
    void testRegisterSuccess() {
        Player player = authService.register("Test User", testEmail, "password123");
        assertNotNull(player);
        assertNotNull(player.getId());
        assertEquals(testEmail, player.getEmail());
        assertNotNull(player.getPasswordHash());
    }

    @Test
    @Order(2)
    void testRegisterDuplicateEmail() {
        assertThrows(RuntimeException.class, () -> {
            authService.register("Another User", testEmail, "password456");
        });
    }

    @Test
    @Order(3)
    void testRegisterInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            authService.register("Test User", "invalid-email", "password123");
        });
    }

    @Test
    @Order(4)
    void testRegisterWeakPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            authService.register("Test User", "new@test.com", "weak");
        });
    }

    @Test
    @Order(5)
    void testLoginSuccess() {
        Optional<Player> result = authService.login(testEmail, "password123");
        assertTrue(result.isPresent());
        assertEquals(testEmail, result.get().getEmail());
    }

    @Test
    @Order(6)
    void testLoginWrongPassword() {
        Optional<Player> result = authService.login(testEmail, "wrongpassword");
        assertFalse(result.isPresent());
    }

    @Test
    @Order(7)
    void testLoginNonExistentUser() {
        Optional<Player> result = authService.login("nonexistent@test.com", "password123");
        assertFalse(result.isPresent());
    }

    @Test
    @Order(8)
    void testCreateGuest() {
        Player guest = authService.createGuest("Guest Player");
        assertNotNull(guest);
        assertTrue(guest.getIsGuest());
        assertTrue(guest.getEmail().startsWith("guest_"));
    }

    @Test
    @Order(9)
    void testEmailExists() {
        assertTrue(authService.emailExists(testEmail));
        assertFalse(authService.emailExists("nonexistent@test.com"));
    }

    @Test
    @Order(10)
    void testLogout() {
        authService.login(testEmail, "password123");
        assertTrue(SessionContext.getInstance().isLoggedIn());
        authService.logout();
        assertFalse(SessionContext.getInstance().isLoggedIn());
    }
}
