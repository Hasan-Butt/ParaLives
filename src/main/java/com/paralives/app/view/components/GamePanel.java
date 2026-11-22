package com.paralives.app.view.components;

import com.paralives.engine.input.GameCommand;
import com.paralives.engine.input.InputHandler;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.HierarchyEvent;

public class GamePanel extends JPanel {
    public static final int TARGET_FPS = 60;
    private static final int TIMER_DELAY_MS = 1000 / TARGET_FPS;
    private static final int PLAYER_RADIUS = 18;
    private static final int PLAYER_SPEED = 4;

    private final InputHandler inputHandler;
    private final Timer gameLoop;

    private int playerAX = 260;
    private int playerAY = 230;
    private int playerBX = 820;
    private int playerBY = 230;

    public GamePanel(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
        setBackground(new Color(235, 240, 245));
        setDoubleBuffered(true);
        setFocusable(true);

        gameLoop = new Timer(TIMER_DELAY_MS, this::update);
        gameLoop.setCoalesce(true);

        addHierarchyListener(event -> {
            if ((event.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) == 0) {
                return;
            }
            if (isShowing()) {
                inputHandler.attach(this);
                start();
                requestFocusInWindow();
            } else {
                stop();
                inputHandler.detach(this);
                inputHandler.clear();
            }
        });
    }

    public void start() {
        if (!gameLoop.isRunning()) {
            gameLoop.start();
        }
    }

    public void stop() {
        if (gameLoop.isRunning()) {
            gameLoop.stop();
        }
    }

    public void pause() {
        stop();
    }

    public void resume() {
        start();
        requestFocusInWindow();
    }

    private void update(ActionEvent event) {
        GameCommand command;
        while ((command = inputHandler.pollPlayerA()) != null) {
            applyCommand(command, true);
        }
        while ((command = inputHandler.pollPlayerB()) != null) {
            applyCommand(command, false);
        }
        repaint();
    }

    private void applyCommand(GameCommand command, boolean playerA) {
        int[] position = playerA
                ? new int[]{playerAX, playerAY}
                : new int[]{playerBX, playerBY};

        switch (command) {
            case MOVE_UP -> position[1] -= PLAYER_SPEED;
            case MOVE_DOWN -> position[1] += PLAYER_SPEED;
            case MOVE_LEFT -> position[0] -= PLAYER_SPEED;
            case MOVE_RIGHT -> position[0] += PLAYER_SPEED;
            case INTERACT -> {
            }
        }

        position[0] = Math.max(PLAYER_RADIUS, Math.min(getWidth() - PLAYER_RADIUS, position[0]));
        position[1] = Math.max(PLAYER_RADIUS, Math.min(getHeight() - PLAYER_RADIUS, position[1]));

        if (playerA) {
            playerAX = position[0];
            playerAY = position[1];
        } else {
            playerBX = position[0];
            playerBY = position[1];
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        drawGrid(graphics2D);
        drawPlayer(graphics2D, playerAX, playerAY, Color.RED, "A");
        drawPlayer(graphics2D, playerBX, playerBY, Color.BLUE, "B");

        graphics2D.dispose();
    }

    private void drawGrid(Graphics2D graphics) {
        graphics.setColor(new Color(215, 220, 225));
        graphics.setStroke(new BasicStroke(1));
        for (int x = 0; x < getWidth(); x += 40) {
            graphics.drawLine(x, 0, x, getHeight());
        }
        for (int y = 0; y < getHeight(); y += 40) {
            graphics.drawLine(0, y, getWidth(), y);
        }
    }

    private void drawPlayer(Graphics2D graphics, int x, int y,
                            Color color, String label) {
        graphics.setColor(color);
        graphics.fillOval(
                x - PLAYER_RADIUS,
                y - PLAYER_RADIUS,
                PLAYER_RADIUS * 2,
                PLAYER_RADIUS * 2);

        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        graphics.drawString(label, x - 5, y + 5);
    }
}
