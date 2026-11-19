package com.paralives.app.view.screens;

import com.paralives.app.presenter.AppPresenter;
import com.paralives.app.view.BaseScreen;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Font;

public class SaveSlotScreen extends BaseScreen {
    public SaveSlotScreen(AppPresenter presenter) {
        addCenteredTitle("Choose Save Slot", 80, 42);

        JPanel slots = new JPanel(new GridLayout(1, 3, 20, 0));
        slots.setBounds(240, 260, 800, 180);
        for (int slot = 1; slot <= 3; slot++) {
            JButton slotButton = new JButton("<html><center>Slot " + slot
                    + "<br><br>No save data</center></html>");
            int selectedSlot = slot;
            slotButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
            slotButton.addActionListener(event -> {
                presenter.openGame();
                Toolkit.getDefaultToolkit().beep();
                System.out.println("Selected save slot " + selectedSlot);
            });
            slots.add(slotButton);
        }
        add(slots);

        JButton backButton = createButton("Back", 515, 500, 250, 50);
        backButton.addActionListener(event -> presenter.openMainMenu());
        add(backButton);
    }
}
