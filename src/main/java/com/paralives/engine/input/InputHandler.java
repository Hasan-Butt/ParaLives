package com.paralives.engine.input;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class InputHandler implements KeyListener {
    private final Queue<GameCommand> playerACommands = new ConcurrentLinkedQueue<>();
    private final Queue<GameCommand> playerBCommands = new ConcurrentLinkedQueue<>();

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
        return switch (event.getKeyCode()) {
            case KeyEvent.VK_W -> GameCommand.MOVE_UP;
            case KeyEvent.VK_S -> GameCommand.MOVE_DOWN;
            case KeyEvent.VK_A -> GameCommand.MOVE_LEFT;
            case KeyEvent.VK_D -> GameCommand.MOVE_RIGHT;
            case KeyEvent.VK_E -> GameCommand.INTERACT;
            default -> null;
        };
    }

    private GameCommand mapPlayerB(KeyEvent event) {
        return switch (event.getKeyCode()) {
            case KeyEvent.VK_UP -> GameCommand.MOVE_UP;
            case KeyEvent.VK_DOWN -> GameCommand.MOVE_DOWN;
            case KeyEvent.VK_LEFT -> GameCommand.MOVE_LEFT;
            case KeyEvent.VK_RIGHT -> GameCommand.MOVE_RIGHT;
            case KeyEvent.VK_ENTER -> GameCommand.INTERACT;
            default -> null;
        };
    }
}
