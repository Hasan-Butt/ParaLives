package com.paralives.app.view.components;

import com.paralives.engine.input.InputHandler;
import com.paralives.engine.input.PlayerId;
import com.paralives.engine.network.GameClient;
import com.paralives.engine.network.GameServer;
import com.paralives.engine.network.NetworkRole;
import com.paralives.engine.network.PlayerInput;
import com.paralives.engine.world.GameSnapshot;
import com.paralives.engine.world.GameWorld;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.HierarchyEvent;
import java.awt.Rectangle;
import com.paralives.engine.world.TileMap;

public class GamePanel extends JPanel {
    public static final int TARGET_FPS = 60;
    private static final int TIMER_DELAY_MS = 1000 / TARGET_FPS;
    private static final int PLAYER_RADIUS = 18;
    private static final int PLAYER_SPEED = 4;

    private final InputHandler inputHandler;
    private final Timer gameLoop;
    private final GameWorld world = new GameWorld();
    private final NetworkRole networkRole;
    private final String hostAddress;
    private final int networkPort;
    private GameServer gameServer;
    private GameClient gameClient;
    private final TileRenderer tileRenderer = new TileRenderer();

    public GamePanel(InputHandler inputHandler) {
        this(inputHandler, NetworkRole.LOCAL, "127.0.0.1", 54545);
    }

    public GamePanel(InputHandler inputHandler, NetworkRole networkRole,
                     String hostAddress, int networkPort) {
        this.inputHandler = inputHandler;
        this.networkRole = networkRole;
        this.hostAddress = hostAddress;
        this.networkPort = networkPort;
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
                startNetwork();
                start();
                requestFocusInWindow();
            } else {
                stop();
                stopNetwork();
                inputHandler.detach(this);
                inputHandler.clear();
            }
        });
    }

    private void startNetwork() {
        if (networkRole == NetworkRole.HOST) {
            gameServer = new GameServer();
            try {
                gameServer.start(networkPort);
            } catch (java.io.IOException exception) {
                gameServer = null;
            }
        } else if (networkRole == NetworkRole.CLIENT) {
            gameClient = new GameClient();
            Thread.ofVirtual().name("paralives-connect").start(() -> {
                try {
                    gameClient.connect(hostAddress, networkPort);
                } catch (java.io.IOException exception) {
                    gameClient.close();
                    gameClient = null;
                }
            });
        }
    }

    private void stopNetwork() {
        if (gameServer != null) {
            gameServer.close();
            gameServer = null;
        }
        if (gameClient != null) {
            gameClient.close();
            gameClient = null;
        }
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
        if (networkRole == NetworkRole.HOST) {
            updateHost();
        } else if (networkRole == NetworkRole.CLIENT) {
            updateClient();
        } else {
            updateLocal();
        }
        repaint();
    }

    private void updateLocal() {
        PlayerInput inputA = inputHandler.pollPlayerInput(PlayerId.PLAYER_A);
        PlayerInput inputB = inputHandler.pollPlayerInput(PlayerId.PLAYER_B);
        toggleDimensionForInteract(inputA, inputB);
        world.update(inputA, inputB, getWidth(), getHeight());
    }

    private void updateHost() {
        PlayerInput inputA = inputHandler.pollPlayerInput(PlayerId.PLAYER_A);
        PlayerInput inputB = gameServer == null
                ? new PlayerInput()
                : gameServer.pollInput();
        if (inputB == null) {
            inputB = new PlayerInput();
        }
        toggleDimensionForInteract(inputA, inputB);
        world.update(inputA, inputB, getWidth(), getHeight());
        if (gameServer != null) {
            gameServer.enqueueState(world.toSnapshot());
        }
    }

    private void updateClient() {
        if (gameClient != null) {
            gameClient.sendInput(inputHandler.pollPlayerInput(PlayerId.PLAYER_B));
            GameSnapshot snapshot = gameClient.pollState();
            if (snapshot != null) {
                world.applySnapshot(snapshot);
            }
        }
    }

    private void toggleDimensionForInteract(PlayerInput inputA, PlayerInput inputB) {
        if (inputA.isInteract() || inputB.isInteract()) {
            world.toggleDimension();
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        TileMap tileMap = world.getActiveTileMap();
        tileRenderer.render(
                graphics2D,
                tileMap,
                new Rectangle(0, 0),
                getWidth() / tileMap.getWidth(),
                getHeight() / tileMap.getHeight());
        drawPlayer(graphics2D, world.getPlayerAX(), world.getPlayerAY(), Color.RED, "A");
        drawPlayer(graphics2D, world.getPlayerBX(), world.getPlayerBY(), Color.BLUE, "B");

        graphics2D.dispose();
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
