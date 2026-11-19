package com.paralives.engine.input;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import com.paralives.engine.settings.AppSettings;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class InputHandler implements KeyListener {
    private final Queue<GameCommand> playerACommands = new ConcurrentLinkedQueue<>();
    private final Queue<GameCommand> playerBCommands = new ConcurrentLinkedQueue<>();
    private final AppSettings settings;

    public InputHandler() {
        this(new AppSettings());
    }

    public InputHandler(AppSettings settings) {
        this.settings = settings;
    }

    public void attach(Component component) {
        component.addKeyListener(this);
    }

    public void detach(Component component) {
        component.removeKeyListener(this);
    }

    public Queue<GameCommand> playerACommands() {
        return playerACommands;
    }

    public Queue<GameCommand> playerBCommands() {
        return playerBCommands;
    }

    public GameCommand pollPlayerA() {
        return playerACommands.poll();
    }

    public GameCommand pollPlayerB() {
        return playerBCommands.poll();
    }

    public void clear() {
        playerACommands.clear();
        playerBCommands.clear();
    }

    @Override
    public void keyTyped(KeyEvent event) {
        // Movement commands are dispatched only by keyPressed.
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (event.isConsumed()) {
            return;
        }

        GameCommand command = mapPlayerA(event);
        if (command != null) {
            playerACommands.offer(command);
            event.consume();
            return;
        }

        command = mapPlayerB(event);
        if (command != null) {
            playerBCommands.offer(command);
            event.consume();
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        // The engine consumes discrete commands on key press.
    }

    private GameCommand mapPlayerA(KeyEvent event) {
        for (GameCommand command : GameCommand.values()) {
            if (settings.getPlayerAKey(command) == event.getKeyCode()) {
                return command;
            }
        }
        return null;
    }

    private GameCommand mapPlayerB(KeyEvent event) {
        for (GameCommand command : GameCommand.values()) {
            if (settings.getPlayerBKey(command) == event.getKeyCode()) {
                return command;
            }
        }
        return null;
    }

    public static String keyName(int keyCode) {
        if (keyCode == 0) {
            return "Unbound";
        }
        return KeyEvent.getKeyText(keyCode);
    }
}
