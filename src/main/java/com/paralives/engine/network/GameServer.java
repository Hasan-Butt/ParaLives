package com.paralives.engine.network;

import com.paralives.engine.world.GameSnapshot;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameServer implements AutoCloseable {
    private final BlockingQueue<PlayerInput> inputQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<GameSnapshot> stateQueue = new LinkedBlockingQueue<>();
    private final AtomicBoolean running = new AtomicBoolean();

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectOutputStream toClient;
    private ObjectInputStream fromClient;
    private volatile IOException connectionError;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        running.set(true);

        Thread.ofVirtual().name("paralives-host").start(() -> {
            try {
                clientSocket = serverSocket.accept();
                clientSocket.setTcpNoDelay(true);
                toClient = new ObjectOutputStream(clientSocket.getOutputStream());
                toClient.flush();
                fromClient = new ObjectInputStream(clientSocket.getInputStream());
                startStateWriter();
                startInputReader();
            } catch (IOException exception) {
                connectionError = exception;
                running.set(false);
            }
        });
    }

    private void startInputReader() {
        Thread.ofVirtual().name("paralives-server-input").start(() -> {
            try {
                while (running.get()) {
                    Object value = fromClient.readObject();
                    if (value instanceof PlayerInput input) {
                        inputQueue.offer(input);
                    }
                }
            } catch (IOException | ClassNotFoundException exception) {
                running.set(false);
            }
        });
    }

    private void startStateWriter() {
        Thread.ofVirtual().name("paralives-server-state").start(() -> {
            try {
                while (running.get()) {
                    GameSnapshot snapshot = stateQueue.take();
                    synchronized (toClient) {
                        toClient.reset();
                        toClient.writeObject(snapshot);
                        toClient.flush();
                    }
                }
            } catch (IOException | InterruptedException exception) {
                if (exception instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
                running.set(false);
            }
        });
    }

    public PlayerInput pollInput() {
        return inputQueue.poll();
    }

    public void enqueueState(GameSnapshot snapshot) {
        stateQueue.offer(snapshot);
    }

    public boolean isRunning() {
        return running.get();
    }

    public IOException getConnectionError() {
        return connectionError;
    }

    public boolean hasClientConnected() {
        return clientSocket != null && clientSocket.isConnected() && !clientSocket.isClosed();
    }

    @Override
    public void close() {
        running.set(false);
        try {
            if (clientSocket != null) {
                clientSocket.close();
            }
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException ignored) {
        }
    }
}
