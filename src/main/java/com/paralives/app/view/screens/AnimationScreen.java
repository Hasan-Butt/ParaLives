package com.paralives.app.view.screens;

import com.paralives.app.view.BaseScreen;
import com.paralives.engine.settings.AppSettings;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.HierarchyEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Full-frame Swing screen backed by an embedded JavaFX MediaPlayer for MP4 animations.
 */
public class AnimationScreen extends BaseScreen {
    public static final String DEFAULT_PROMPT = "Press any key to continue...";
    private static final Path ANIMATIONS_DIR = Path.of("resources", "Animations");
    private static final int INITIAL_WIDTH = 1280;
    private static final int INITIAL_HEIGHT = 720;
    private static final int MAX_PLAYBACK_RETRIES = 3;
    private static final Duration RETRY_DELAY = Duration.millis(250);

    private final AppSettings settings;
    private final JFXPanel jfxPanel;
    private final AtomicBoolean advancing = new AtomicBoolean(false);
    private final KeyEventDispatcher keyEventDispatcher = this::dispatchSwingKeyEvent;

    private volatile Path videoPath;
    private volatile Runnable onComplete;
    private volatile boolean readyToAdvance;
    private boolean dispatcherAttached;

    private MediaView mediaView;
    private MediaPlayer mediaPlayer;
    private Label promptLabel;
    private Label statusLabel;
    private FadeTransition promptPulse;
    private PauseTransition retryTransition;
    private int playbackGeneration;
    private boolean playbackStarted;

    public AnimationScreen(AppSettings settings) {
        this(null, settings, null);
    }

    public AnimationScreen(Path videoPath, AppSettings settings, Runnable onComplete) {
        this.videoPath = videoPath;
        this.settings = Objects.requireNonNull(settings, "settings");
        this.onComplete = onComplete;

        setLayout(new BorderLayout());
        setBackground(java.awt.Color.BLACK);
        setFocusable(true);

        Platform.setImplicitExit(false);
        jfxPanel = new JFXPanel();
        jfxPanel.setBackground(java.awt.Color.BLACK);
        jfxPanel.setFocusable(true);
        add(jfxPanel, BorderLayout.CENTER);

        Platform.runLater(this::initFxScene);

        addHierarchyListener(event -> {
            if ((event.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) == 0) {
                return;
            }
            if (isShowing()) {
                onScreenShown();
            } else {
                onScreenHidden();
            }
        });
    }

    /**
     * Resolves an animation file path inside {@code resources/Animations}.
     */
    public static Path animationPath(String fileName) {
        return ANIMATIONS_DIR.resolve(Objects.requireNonNull(fileName, "fileName"));
    }

    /**
     * Resolves a media URI from disk or, as a fallback, from the classpath {@code /Animations/} folder.
     */
    static String resolveMediaUri(Path path) {
        if (path == null) {
            return null;
        }
        Path normalized = path.toAbsolutePath().normalize();
        if (Files.isRegularFile(normalized)) {
            return normalized.toUri().toString();
        }
        Path fileName = path.getFileName();
        if (fileName != null) {
            URL resource = AnimationScreen.class.getResource("/Animations/" + fileName);
            if (resource != null) {
                return resource.toExternalForm();
            }
        }
        return null;
    }

    public void setVideo(Path videoPath, Runnable onComplete) {
        this.videoPath = videoPath;
        this.onComplete = onComplete;
        this.readyToAdvance = false;
        this.advancing.set(false);
        if (isShowing()) {
            Platform.runLater(() -> startPlayback(0));
        }
    }

    private void onScreenShown() {
        readyToAdvance = false;
        advancing.set(false);
        attachKeyDispatcher();
        SwingUtilities.invokeLater(jfxPanel::requestFocusInWindow);
        Platform.runLater(() -> startPlayback(0));
    }

    private void onScreenHidden() {
        detachKeyDispatcher();
        readyToAdvance = false;
        Platform.runLater(() -> {
            playbackGeneration++;
            cancelRetry();
            disposePlayer();
        });
    }

    private void attachKeyDispatcher() {
        if (!dispatcherAttached) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager()
                    .addKeyEventDispatcher(keyEventDispatcher);
            dispatcherAttached = true;
        }
    }

    private void detachKeyDispatcher() {
        if (dispatcherAttached) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager()
                    .removeKeyEventDispatcher(keyEventDispatcher);
            dispatcherAttached = false;
        }
    }

    private boolean dispatchSwingKeyEvent(KeyEvent event) {
        if (event.getID() == KeyEvent.KEY_PRESSED && readyToAdvance && isShowing()) {
            event.consume();
            triggerComplete();
            return true;
        }
        return false;
    }

    private void initFxScene() {
        StackPane root = new StackPane();
        root.setBackground(new Background(new BackgroundFill(
                Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        mediaView = new MediaView();
        mediaView.setPreserveRatio(true);
        mediaView.fitWidthProperty().bind(root.widthProperty());
        mediaView.fitHeightProperty().bind(root.heightProperty());

        statusLabel = new Label();
        statusLabel.setTextFill(Color.WHITE);
        statusLabel.setFont(Font.font("SansSerif", FontWeight.SEMI_BOLD, 20));
        statusLabel.setWrapText(true);
        statusLabel.setVisible(false);
        StackPane.setAlignment(statusLabel, Pos.CENTER);
        StackPane.setMargin(statusLabel, new Insets(40));

        promptLabel = new Label(DEFAULT_PROMPT);
        promptLabel.setTextFill(Color.WHITE);
        promptLabel.setFont(Font.font("SansSerif", FontWeight.BOLD, 22));
        promptLabel.setPadding(new Insets(10, 28, 10, 28));
        promptLabel.setBackground(new Background(new BackgroundFill(
                Color.rgb(10, 12, 18, 0.72), new CornerRadii(22), Insets.EMPTY)));
        promptLabel.setVisible(false);
        StackPane.setAlignment(promptLabel, Pos.BOTTOM_CENTER);
        StackPane.setMargin(promptLabel, new Insets(0, 0, 44, 0));

        promptPulse = new FadeTransition(Duration.millis(900), promptLabel);
        promptPulse.setFromValue(0.4);
        promptPulse.setToValue(1.0);
        promptPulse.setCycleCount(Animation.INDEFINITE);
        promptPulse.setAutoReverse(true);

        root.getChildren().addAll(mediaView, statusLabel, promptLabel);

        Scene scene = new Scene(root, INITIAL_WIDTH, INITIAL_HEIGHT, Color.BLACK);
        scene.setOnKeyPressed(event -> {
            if (readyToAdvance) {
                event.consume();
                triggerComplete();
            }
        });
        jfxPanel.setScene(scene);
    }

    private void startPlayback(int attempt) {
        playbackGeneration++;
        final int generation = playbackGeneration;
        playbackStarted = false;
        cancelRetry();
        disposePlayer();
        hidePrompt();
        statusLabel.setVisible(false);
        readyToAdvance = false;
        advancing.set(false);

        Path targetPath = this.videoPath;
        String mediaUri = resolveMediaUri(targetPath);
        if (mediaUri == null) {
            showFallback("Animation not found: " + targetPath);
            return;
        }

        try {
            Media media = new Media(mediaUri);
            media.setOnError(() -> handleMediaError(media.getError(), attempt, generation));

            MediaPlayer player = new MediaPlayer(media);
            this.mediaPlayer = player;
            applyAudioSettings(player);
            player.setAutoPlay(true);
            player.setOnReady(() -> {
                if (this.mediaPlayer == player && generation == playbackGeneration) {
                    applyAudioSettings(player);
                    player.play();
                }
            });
            player.setOnPlaying(() -> {
                if (this.mediaPlayer == player && generation == playbackGeneration) {
                    playbackStarted = true;
                }
            });
            player.setOnError(() -> handleMediaError(player.getError(), attempt, generation));
            player.setOnEndOfMedia(() -> {
                if (this.mediaPlayer == player && generation == playbackGeneration) {
                    player.pause();
                    markReadyToAdvance();
                }
            });

            mediaView.setMediaPlayer(player);

            if (jfxPanel.getScene() != null && jfxPanel.getScene().getRoot() != null) {
                jfxPanel.getScene().getRoot().requestFocus();
            }
        } catch (RuntimeException exception) {
            handleMediaError(exception, attempt, generation);
        }
    }

    private void applyAudioSettings(MediaPlayer player) {
        player.setMute(settings.isMuted());
        player.setVolume(settings.getMasterVolume() / 100.0);
    }

    private void handleMediaError(Throwable error, int attempt, int generation) {
        if (generation != playbackGeneration) {
            return;
        }
        if (!playbackStarted && attempt < MAX_PLAYBACK_RETRIES && isShowing()) {
            disposePlayer();
            retryTransition = new PauseTransition(RETRY_DELAY);
            retryTransition.setOnFinished(event -> {
                if (generation == playbackGeneration && isShowing()) {
                    startPlayback(attempt + 1);
                }
            });
            retryTransition.play();
            return;
        }
        String message = error != null && error.getMessage() != null
                ? error.getMessage()
                : "Unable to play animation.";
        showFallback(message);
    }

    private void cancelRetry() {
        if (retryTransition != null) {
            retryTransition.stop();
            retryTransition = null;
        }
    }

    private void showFallback(String message) {
        statusLabel.setText(message);
        statusLabel.setVisible(true);
        markReadyToAdvance();
    }

    private void markReadyToAdvance() {
        readyToAdvance = true;
        promptLabel.setOpacity(1.0);
        promptLabel.setVisible(true);
        promptPulse.playFromStart();
    }

    private void hidePrompt() {
        if (promptPulse != null) {
            promptPulse.stop();
        }
        if (promptLabel != null) {
            promptLabel.setVisible(false);
            promptLabel.setOpacity(1.0);
        }
    }

    private void disposePlayer() {
        if (mediaView != null) {
            mediaView.setMediaPlayer(null);
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }

    private void triggerComplete() {
        if (!readyToAdvance || !advancing.compareAndSet(false, true)) {
            return;
        }
        Platform.runLater(() -> {
            playbackGeneration++;
            cancelRetry();
            disposePlayer();
        });
        Runnable callback = this.onComplete;
        if (callback != null) {
            SwingUtilities.invokeLater(callback);
        }
    }
}
