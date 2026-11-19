package com.paralives.app.view.screens;

import com.paralives.app.presenter.AppPresenter;
import com.paralives.app.view.BaseScreen;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class RegisterScreen extends BaseScreen {
    public RegisterScreen(AppPresenter presenter) {
        addCenteredTitle("Register", 80, 48);
        addField("Username", 440, 220);
        addField("Email", 440, 300);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(440, 380, 400, 25);
        add(passwordLabel);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(440, 410, 400, 40);
        add(passwordField);

        JButton registerButton = createButton("Create Account", 440, 480, 190, 50);
        registerButton.addActionListener(event -> presenter.completeRegistration());
        add(registerButton);

        JButton backButton = createButton("Back", 650, 480, 190, 50);
        backButton.addActionListener(event -> presenter.logout());
        add(backButton);
    }

    private void addField(String labelText, int x, int y) {
        JLabel label = new JLabel(labelText);
        label.setBounds(x, y, 400, 25);
        add(label);
        JTextField field = new JTextField();
        field.setBounds(x, y + 30, 400, 40);
        add(field);
    }
}
