package com.paralives.app.view.screens;

import com.paralives.app.presenter.AppPresenter;
import com.paralives.app.view.BaseScreen;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginScreen extends BaseScreen {
    public LoginScreen(AppPresenter presenter) {
        addCenteredTitle("Login", 110, 48);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(440, 260, 400, 30);
        add(usernameLabel);
        JTextField usernameField = new JTextField();
        usernameField.setBounds(440, 290, 400, 40);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(440, 340, 400, 30);
        add(passwordLabel);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(440, 370, 400, 40);
        add(passwordField);

        JButton loginButton = createButton("Login", 440, 440, 190, 50);
        loginButton.addActionListener(event -> presenter.login());
        add(loginButton);

        JButton registerButton = createButton("Create Account", 650, 440, 190, 50);
        registerButton.addActionListener(event -> presenter.register());
        add(registerButton);
    }
}
