package com.paralives.app.view;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Font;

public abstract class BaseScreen extends JPanel {
    protected BaseScreen() {
        setLayout(null);
    }

    protected void addCenteredTitle(String text, int y, int fontSize) {
        JLabel title = new JLabel(text, SwingConstants.CENTER);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, fontSize));
        title.setBounds(240, y, 800, 70);
        add(title);
    }

    protected JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        button.setBounds(x, y, width, height);
        return button;
    }
}
