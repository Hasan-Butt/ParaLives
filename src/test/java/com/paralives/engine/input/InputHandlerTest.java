package com.paralives.engine.input;

import org.junit.jupiter.api.Test;

import java.awt.Canvas;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InputHandlerTest {
    @Test
    void dispatchesWASDAndEToPlayerAQueue() {
        InputHandler handler = new InputHandler();
        Canvas component = new Canvas();

        handler.keyPressed(key(component, KeyEvent.VK_W));
        handler.keyPressed(key(component, KeyEvent.VK_D));
        handler.keyPressed(key(component, KeyEvent.VK_E));

        assertEquals(GameCommand.MOVE_UP, handler.pollPlayerA());
        assertEquals(GameCommand.MOVE_RIGHT, handler.pollPlayerA());
        assertEquals(GameCommand.INTERACT, handler.pollPlayerA());
        assertEquals(0, handler.playerBCommands().size());
    }

    @Test
    void dispatchesArrowKeysAndEnterToPlayerBQueue() {
        InputHandler handler = new InputHandler();
        Canvas component = new Canvas();

        handler.keyPressed(key(component, KeyEvent.VK_UP));
        handler.keyPressed(key(component, KeyEvent.VK_RIGHT));
        handler.keyPressed(key(component, KeyEvent.VK_ENTER));

        assertEquals(GameCommand.MOVE_UP, handler.pollPlayerB());
        assertEquals(GameCommand.MOVE_RIGHT, handler.pollPlayerB());
        assertEquals(GameCommand.INTERACT, handler.pollPlayerB());
        assertEquals(0, handler.playerACommands().size());
    }

    private KeyEvent key(Canvas component, int keyCode) {
        return new KeyEvent(component, KeyEvent.KEY_PRESSED,
                0L, 0, keyCode, KeyEvent.CHAR_UNDEFINED);
    }
}
