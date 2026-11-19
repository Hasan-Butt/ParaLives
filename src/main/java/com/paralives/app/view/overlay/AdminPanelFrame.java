package com.paralives.app.view.overlay;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Font;

public class AdminPanelFrame extends JFrame {
    public AdminPanelFrame() {
        super("ParaLives Admin Panel");
        setSize(1280, 720);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        JLabel title = new JLabel("Admin Panel", SwingConstants.CENTER);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 42));
        title.setBounds(240, 80, 800, 70);
        panel.add(title);

        panel.add(createButton("Manage Users", 390, 250, 500, 60));
        panel.add(createButton("Manage Scores", 390, 330, 500, 60));
        panel.add(createButton("Database Tools", 390, 410, 500, 60));
        add(panel);
    }

    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        button.setBounds(x, y, width, height);
        return button;
    }
}
