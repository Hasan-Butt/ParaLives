package com.paralives.engine.network;

import com.paralives.engine.world.GameSnapshot;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameClient implements AutoCloseable {
    private final BlockingQueue<PlayerInput> inputQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<GameSnapshot> stateQueue = new LinkedBlockingQueue<>();
    private final AtomicBoolean running = new AtomicBoolean();

    private Socket socket;
    private ObjectOutputStream toHost;
    private ObjectInputStream fromHost;

    public void connect(String hostAddress, int port) throws IOException {
        socket = new Socket(hostAddress, port);
        socket.setTcpNoDelay(true);
        running.set(true);
        toHost = new ObjectOutputStream(socket.getOutputStream());
        toHost.flush();
        fromHost = new ObjectInputStream(socket.getInputStream());
        startInputWriter();
        startStateReader();
    }

    private void startInputWriter() {
        Thread.ofVirtual().name("paralives-client-input").start(() -> {
            try {
                while (running.get()) {
                    PlayerInput input = inputQueue.take();
                    synchronized (toHost) {
                        toHost.writeObject(input);
                        toHost.flush();
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

    private void startStateReader() {
        Thread.ofVirtual().name("paralives-client-state").start(() -> {
            try {
                while (running.get()) {
                    Object value = fromHost.readObject();
                    if (value instanceof GameSnapshot snapshot) {
                        stateQueue.offer(snapshot);
                    }
                }
            } catch (IOException | ClassNotFoundException exception) {
                running.set(false);
            }
        });
    }

    public void sendInput(PlayerInput input) {
        inputQueue.offer(input);
    }

    public GameSnapshot pollState() {
        return stateQueue.poll();
    }

    public boolean isConnected() {
        return running.get() && socket != null
                && socket.isConnected() && !socket.isClosed();
    }

    @Override
    public void close() {
        running.set(false);
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ignored) {
        }
    }
}
