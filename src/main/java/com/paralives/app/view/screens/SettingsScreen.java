package com.paralives.app.view.screens;

import com.paralives.app.presenter.AppPresenter;
import com.paralives.app.view.BaseScreen;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSlider;

public class SettingsScreen extends BaseScreen {
    public SettingsScreen(AppPresenter presenter) {
        addCenteredTitle("Settings", 100, 42);

        JLabel volumeLabel = new JLabel("Master Volume");
        volumeLabel.setBounds(440, 240, 400, 30);
        add(volumeLabel);
        JSlider masterVolumeSlider = new JSlider(0, 100, 80);
        masterVolumeSlider.setBounds(440, 270, 400, 50);
        masterVolumeSlider.setMajorTickSpacing(20);
        masterVolumeSlider.setPaintTicks(true);
        add(masterVolumeSlider);

        JLabel resolutionLabel = new JLabel("Resolution");
        resolutionLabel.setBounds(440, 340, 400, 30);
        add(resolutionLabel);
        JComboBox<String> resolutionSelector =
                new JComboBox<>(new String[]{"1280 x 720", "1920 x 1080"});
        resolutionSelector.setBounds(440, 370, 400, 40);
        resolutionSelector.setSelectedIndex(0);
        add(resolutionSelector);

        JButton backButton = createButton("Back", 515, 500, 250, 50);
        backButton.addActionListener(event -> presenter.openMainMenu());
        add(backButton);
    }
}
