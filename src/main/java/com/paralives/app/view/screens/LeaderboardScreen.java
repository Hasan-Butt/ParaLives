package com.paralives.app.view.screens;

import com.paralives.app.presenter.AppPresenter;
import com.paralives.app.view.BaseScreen;

import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;

public class LeaderboardScreen extends BaseScreen {
    public LeaderboardScreen(AppPresenter presenter) {
        addCenteredTitle("Leaderboard", 80, 42);

        JLabel tableHeader = new JLabel("Rank       Player       Score");
        tableHeader.setFont(new Font(Font.MONOSPACED, Font.BOLD, 24));
        tableHeader.setBounds(420, 220, 500, 40);
        add(tableHeader);

        String[] scores = {"1. Alex - 9840", "2. Sam - 8710", "3. Riley - 7450",
                "4. Jamie - 6820", "5. Taylor - 5910"};
        for (int index = 0; index < scores.length; index++) {
            JLabel row = new JLabel(scores[index]);
            row.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 22));
            row.setBounds(420, 270 + index * 45, 500, 35);
            add(row);
        }

        JButton backButton = createButton("Back", 515, 560, 250, 50);
        backButton.addActionListener(event -> presenter.openMainMenu());
        add(backButton);
    }
}
