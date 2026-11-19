package com.paralives.app.view.screens;

import com.paralives.app.presenter.AppPresenter;
import com.paralives.app.view.BaseScreen;
import com.paralives.engine.input.GameCommand;
import com.paralives.engine.input.InputHandler;
import com.paralives.engine.settings.AppSettings;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SettingsScreen extends BaseScreen {
    public SettingsScreen(AppPresenter presenter, AppSettings settings) {
        addCenteredTitle("Settings", 30, 42);

        JLabel volumeLabel = new JLabel("Master Volume");
        volumeLabel.setBounds(440, 115, 400, 25);
        add(volumeLabel);

        JSlider volumeSlider = new JSlider(0, 100, settings.getMasterVolume());
        volumeSlider.setBounds(440, 140, 400, 45);
        volumeSlider.setMajorTickSpacing(20);
        volumeSlider.setPaintTicks(true);
        volumeSlider.addChangeListener(event ->
                settings.setMasterVolume(volumeSlider.getValue()));
        add(volumeSlider);

        JCheckBox muteToggle = new JCheckBox("Mute", settings.isMuted());
        muteToggle.setBounds(440, 185, 400, 30);
        muteToggle.addActionListener(event -> settings.setMuted(muteToggle.isSelected()));
        add(muteToggle);

        JLabel playerALabel = new JLabel("Player A - WASD + E");
        playerALabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        playerALabel.setBounds(180, 235, 400, 30);
        add(playerALabel);

        JLabel playerBLabel = new JLabel("Player B - Arrow Keys + Enter");
        playerBLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        playerBLabel.setBounds(700, 235, 400, 30);
        add(playerBLabel);

        GameCommand[] commands = GameCommand.values();
        for (int index = 0; index < commands.length; index++) {
            GameCommand command = commands[index];
            int y = 280 + index * 55;

            addBindingRow(command, settings, true, 180, y);
            addBindingRow(command, settings, false, 700, y);
        }

        JButton resetButton = createButton("Reset Keys", 405, 570, 180, 45);
        resetButton.addActionListener(event -> {
            settings.resetKeyBindings();
            settings.save();
            SwingUtilities.invokeLater(() ->
                    presenter.openSettings());
        });
        add(resetButton);

        JButton saveButton = createButton("Save & Back", 695, 570, 180, 45);
        saveButton.addActionListener(event -> {
            settings.save();
            presenter.openMainMenu();
        });
        add(saveButton);

        JLabel configLabel = new JLabel("Config: " + settings.getConfigPath());
        configLabel.setBounds(180, 625, 920, 25);
        add(configLabel);
    }

    private void addBindingRow(GameCommand command, AppSettings settings,
                               boolean playerA, int x, int y) {
        JLabel label = new JLabel(command.name().replace('_', ' '));
        label.setBounds(x, y, 180, 40);
        add(label);

        JButton keyButton = new JButton(playerA
                ? InputHandler.keyName(settings.getPlayerAKey(command))
                : InputHandler.keyName(settings.getPlayerBKey(command)));
        keyButton.setBounds(x + 190, y, 180, 40);
        keyButton.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));

        keyButton.addActionListener(event -> {
            JFrame owner = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, this);
            openKeyCapture(owner, command, playerA, settings, keyButton);
        });
        add(keyButton);
    }

    private void openKeyCapture(JFrame owner, GameCommand command, boolean playerA,
                                AppSettings settings, JButton keyButton) {
        JDialog dialog = new JDialog(owner, "Rebind " + command.name().replace('_', ' '), true);
        dialog.setSize(360, 160);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(owner);
        dialog.setFocusable(true);

        JLabel prompt = new JLabel(
                "Press a key for Player " + (playerA ? "A" : "B"),
                javax.swing.SwingConstants.CENTER);
        prompt.setBounds(20, 20, 320, 40);
        prompt.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));

        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setLayout(null);
        panel.add(prompt);
        dialog.setContentPane(panel);
        dialog.requestFocusInWindow();

        dialog.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                int keyCode = event.getKeyCode();
                boolean rebound = playerA
                        ? settings.rebindPlayerA(command, keyCode)
                        : settings.rebindPlayerB(command, keyCode);

                if (!rebound) {
                    JOptionPane.showMessageDialog(dialog,
                            "That key is already used. Choose another key.",
                            "Key Conflict",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                keyButton.setText(InputHandler.keyName(keyCode));
                dialog.dispose();
            }
        });
        dialog.setVisible(true);
    }
}
