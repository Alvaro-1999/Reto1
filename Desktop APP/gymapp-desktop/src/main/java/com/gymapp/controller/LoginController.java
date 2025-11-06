package com.gymapp.controller;

import com.gymapp.model.User;
import com.gymapp.service.UserService;
import com.gymapp.view.LoginView;
import com.gymapp.view.MainView;
import com.gymapp.view.RegisterView;

import javax.swing.*;

public class LoginController {
    private final UserService userService;
    private final LoginView view;

    public LoginController(UserService userService, LoginView view) {
        this.userService = userService;
        this.view = view;

        this.view.btnLogin.addActionListener(e -> login());
        this.view.btnRegister.addActionListener(e -> register());
    }

    private void login() {
        try {
            String login = view.txtLogin.getText().trim();
            String password = new String(view.txtPassword.getPassword()).trim();

            if (login.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(view,
                        "Debes introducir login y contraseña", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            User user = new User();
            user.setLogin(login);
            user.setPassword(password);

            if (userService.checkCredentials(user)) {
                User found = userService.find(user);
                JOptionPane.showMessageDialog(view,
                        "Bienvenido " + found.getName() + " (nivel " + found.getLevel() + ")");
                view.dispose();

                MainView mainView = new MainView(found.getName());
                new MainController(mainView, found);
            } else {
                JOptionPane.showMessageDialog(view,
                        "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view,
                    "Error en login: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void register() {
        RegisterView registerView = new RegisterView();
        new RegisterController(userService, registerView);
        registerView.setVisible(true);
    }
}
